%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Client Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(client).
-export([start/2, loop/4, goOnline/2, goOffline/2, requestChat/3, exitChat/1, sendMessage/2]).


start(Server,Process_Name) ->
	register(Process_Name, spawn(client,loop,[Server,false,0,false])).

loop(Server,In_Chat,Friend_Name,Is_Registered) ->
	io:format("waiting for message...~n"),
	receive
		{receive_message,Message} ->
			io:format("Receiving chat message......~n"),
			io:format("~p~n",[Message]),
			loop(Server,In_Chat,Friend_Name,Is_Registered);
		{send_message,Message} ->
			if
				In_Chat ->
					Friend_Name ! {receive_message,Message},
					io:format("Message sent~n"),
					loop(Server,In_Chat,Friend_Name,Is_Registered);
				true ->
					io:format("You are not currently in a chat~n"),
					loop(Server,In_Chat,Friend_Name,Is_Registered)
			end;
		{From,id_please,online} ->
			From ! {self(),my_id,Server,Is_Registered},
			loop(Server,In_Chat,Friend_Name,Is_Registered);
		{From,id_please} ->
			From ! {self(),my_id,Server},
			loop(Server,In_Chat,Friend_Name,Is_Registered);
		{chat,was_disconnected} ->
			io:format("The chat was disconnected by other party~n"),
			loop(Server,false,0,Is_Registered);
		{chat,disconnected} ->
			io:format("You have disconnected the chat~n"),
			Friend_Name ! {chat,was_disconnected},
			loop(Server,false,0,Is_Registered);
		{chat,accepted,From,Name} ->
			io:format("You are now connected with ~p~n",[Name]),
			loop(Server,true,From,Is_Registered);
		{accept,From,Name} ->
			if
				In_Chat ->
					Friend_accept = "already in chat";
				true ->
					io:format("~p would like to chat ",[Name]),
					Friend_accept = string:strip(io:get_line("[yes/no]"),both,$\n)
			end,
			case Friend_accept of
				"yes" ->
					{frischkro,Server} ! {self(), {accepted, true, From, Name}},
					loop(Server,true,From,Is_Registered);
				"no" ->
					{frischkro,Server} ! {accepted, false, From},
					loop(Server,In_Chat,Friend_Name,Is_Registered);
				"already in chat" ->
					{frischkro,Server} ! {accepted, already, From},
					loop(Server,In_Chat,Friend_Name,Is_Registered);
				_ ->
					io:format("unknown command, chat denied~n"),
					{frischkro,Server} ! {accepted, false, From, Name},
					loop(Server,In_Chat,Friend_Name,Is_Registered)
			end;
		{connect,true} ->
			io:format("You are now connected~n"),
			loop(Server,In_Chat,Friend_Name,true);
		{connect,false} ->
			io:format("Unfortunately, that name is taken~n"),
			loop(Server,In_Chat,Friend_Name,Is_Registered);
		{connect,already,UserName} ->
			io:format("You are already connected as ~p~n", [UserName]),
			loop(Server,In_Chat,Friend_Name,Is_Registered)
	end.

goOnline(Name,Process_Name) ->
	Process_Name ! {self(),id_please,online},
	receive
		{From,my_id,Server,Status} ->
			if
				Status ->
					io:format("already registered~n");
				true ->
					{frischkro,Server} ! {From, {connect, Name}}
			end;
		_ ->
			io:format("could not communicate with process, terminating program"),
			exit(normal)
	end.

goOffline(Name,Process_Name) ->
	Process_Name ! {self(),id_please},
	receive
		{From,my_id,Server} ->
			{frischkro,Server} ! {From, {disconnect, Name}};
		_ ->
			io:format("could not communicate with process, terminating program"),
			exit(normal)
	end.

requestChat(Name,Friend,Process_Name) ->
	Process_Name ! {self(),id_please},
	receive
		{From,my_id,Server} ->
			{frischkro,Server} ! {From, {request, Name, Friend}};
		_ ->
			io:format("could not communicate with process, terminating program"),
			exit(normal)
	end.

exitChat(Process_Name) ->
	io:format("Attempting to exit chat~n"),
	Process_Name ! {chat,disconnected}.

sendMessage(Message,Process_Name) ->
	io:format("Attempting to send message~n"),
	Process_Name ! {send_message,Message}.
