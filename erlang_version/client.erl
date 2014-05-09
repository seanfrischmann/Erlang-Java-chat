%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Client Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(client).
-export([start/2, loop/5, goOnline/2, goOffline/1, requestChat/2, exitChat/1, sendMessage/2]).


start(Server,Process_Name) ->
	register(Process_Name, spawn(client,loop,[Server,0,false,0,false])).

loop(Server,UserName,In_Chat,Friend_Name,Is_Registered) ->
	io:format("waiting for message...~n"),
	receive
		{receive_message,Message} ->
			io:format("Receiving chat message......~n"),
			io:format("~p~n",[Message]),
			loop(Server,UserName,In_Chat,Friend_Name,Is_Registered);
		{send_message,Message} ->
			if
				In_Chat ->
					Friend_Name ! {receive_message,Message},
					io:format("Message sent~n"),
					loop(Server,UserName,In_Chat,Friend_Name,Is_Registered);
				true ->
					io:format("You are not currently in a chat~n"),
					loop(Server,UserName,In_Chat,Friend_Name,Is_Registered)
			end;
		{From,id_please,online} ->
			From ! {self(),my_id,Server,Is_Registered},
			loop(Server,UserName,In_Chat,Friend_Name,Is_Registered);
		{From,id_please} ->
			From ! {self(),my_id,Server,Name},
			loop(Server,UserName,In_Chat,Friend_Name,Is_Registered);
		{chat,was_disconnected} ->
			io:format("The chat was disconnected by other party~n"),
			loop(Server,UserName,false,0,Is_Registered);
		{chat,disconnected} ->
			io:format("You have disconnected the chat~n"),
			Friend_Name ! {chat,was_disconnected},
			loop(Server,UserName,false,0,Is_Registered);
		{chat,accepted,From,Name} ->
			io:format("You are now connected with ~p~n",[Name]),
			loop(Server,UserName,true,From,Is_Registered);
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
					loop(Server,UserName,true,From,Is_Registered);
				"no" ->
					{frischkro,Server} ! {accepted, false, From},
					loop(Server,UserName,In_Chat,Friend_Name,Is_Registered);
				"already in chat" ->
					{frischkro,Server} ! {accepted, already, From},
					loop(Server,UserName,In_Chat,Friend_Name,Is_Registered);
				_ ->
					io:format("unknown command, chat denied~n"),
					{frischkro,Server} ! {accepted, false, From, Name},
					loop(Server,UserName,In_Chat,Friend_Name,Is_Registered)
			end;
		{connect,true,Name} ->
			io:format("You are now connected~n"),
			loop(Server,Name,In_Chat,Friend_Name,true);
		{connect,goOffline} ->
			io:format("You are have disconnected from the server~n"),
			if
				In_Chat ->
					self() ! {chat,disconnect},
					loop(Server,0,In_Chat,Friend_Name,false);
				true ->
					loop(Server,0,false,0,false)
			end;
		{connect,false} ->
			io:format("Unfortunately, that name is taken~n"),
			loop(Server,UserName,In_Chat,Friend_Name,Is_Registered);
		{connect,already,UserName} ->
			io:format("You are already connected as ~p~n", [UserName]),
			loop(Server,UserName,In_Chat,Friend_Name,Is_Registered)
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

goOffline(Process_Name) ->
	Process_Name ! {self(),id_please},
	receive
		{From,my_id,Server,Name} ->
			if
				Name == 0 ->
					io:format("You are not connected");
				true ->
					{frischkro,Server} ! {From, {disconnect, Name}},
					Process_Name ! {connect,goOffline};
		_ ->
			io:format("could not communicate with process, terminating program"),
			exit(normal)
	end.

requestChat(Friend,Process_Name) ->
	Process_Name ! {self(),id_please},
	receive
		{From,my_id,Server,Name} ->
			io:format("Requesting Chat with ~p~n",[Friend]),
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
