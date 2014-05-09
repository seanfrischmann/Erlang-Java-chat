%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Client Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(client).
-export([start/2, loop/5, goOnline/2, goOffline/1, requestChat/2, exitChat/1, sendMessage/2]).


start(Server,Process_Name) ->
	register(Process_Name, spawn(client,loop,[Server,0,false,0,0,false])).

loop(Server,UserName,In_Chat,Friend_Id,Friend_Name,Is_Registered) ->
	io:format("...................................................~n"),
	receive
		{receive_message,Message} ->
			io:format("Receiving chat message From...~p~n",[Friend_Id]),
			io:format("~p~n",[Message]),
			loop(Server,UserName,In_Chat,Friend_Id,Friend_Name,Is_Registered);
		{send_message,Message} ->
			if
				In_Chat ->
					Friend_Id ! {receive_message,Message},
					io:format("Message sent~n"),
					loop(Server,UserName,In_Chat,Friend_Id,Friend_Name,Is_Registered);
				true ->
					io:format("You are not currently in a chat~n"),
					loop(Server,UserName,In_Chat,Friend_Id,Friend_Name,Is_Registered)
			end;
		{From,id_please,online} ->
			From ! {self(),my_id,Server,Is_Registered},
			loop(Server,UserName,In_Chat,Friend_Id,Friend_Name,Is_Registered);
		{From,id_please} ->
			From ! {self(),my_id,Server,UserName},
			loop(Server,UserName,In_Chat,Friend_Id,Friend_Name,Is_Registered);
		{chat,request} ->
			loop(Server,UserName,true,Friend_Id,Friend_Name,Is_Registered);
		{chat,was_disconnected} ->
			io:format("The chat was disconnected by ~p~n",[Friend_Name]),
			loop(Server,UserName,false,0,0,Is_Registered);
		{chat,unavailable} ->
			io:format("User not available~n"),
			loop(Server,UserName,false,0,0,Is_Registered);
		{chat,rejected} ->
			io:format("User rejected request~n"),
			loop(Server,UserName,false,0,0,Is_Registered);
		{chat,already} ->
			io:format("User is in a chat~n"),
			loop(Server,UserName,false,0,Is_Registered);
		{chat,disconnected} ->
			io:format("You have disconnected the chat~n"),
			Friend_Id ! {chat,was_disconnected},
			loop(Server,UserName,false,0,Is_Registered);
		{chat,accepted,From,Name} ->
			io:format("You are now connected with ~p~n",[Name]),
			loop(Server,UserName,true,From,Name,Is_Registered);
		{accept,From,Name} ->
			if
				In_Chat ->
					Friend_accept = "already in chat";
				true ->
					io:format("~p would like to chat ",[Name]),
					Friend_accept = string:strip(io:get_line("[yes/no]: "),both,$\n)
			end,
			case Friend_accept of
				"yes" ->
					{frischkro,Server} ! {self(), {accepted, true, From, UserName}},
					loop(Server,UserName,true,From,Name,Is_Registered);
				"no" ->
					{frischkro,Server} ! {accepted, false, From},
					loop(Server,UserName,false,Friend_Id,Friend_Name,Is_Registered);
				"already in chat" ->
					{frischkro,Server} ! {accepted, already, From},
					loop(Server,UserName,In_Chat,Friend_Id,Friend_Name,Is_Registered);
				_ ->
					io:format("unknown command, chat denied~n"),
					self() ! {accept, From, Name},
					loop(Server,UserName,In_Chat,Friend_Id,Friend_Name,Is_Registered)
			end;
		{connect,true,Name} ->
			io:format("You are now connected~n"),
			loop(Server,Name,In_Chat,Friend_Id,Friend_Name,true);
		{connect,goOffline} ->
			io:format("You are have disconnected from the server~n"),
			if
				In_Chat ->
					self() ! {chat,disconnect},
					loop(Server,0,In_Chat,Friend_Id,Friend_Name,false);
				true ->
					loop(Server,0,false,0,0,false)
			end;
		{connect,false} ->
			io:format("Unfortunately, that name is taken~n"),
			loop(Server,UserName,In_Chat,Friend_Id,Friend_Name,Is_Registered);
		{connect,already,UserName} ->
			io:format("You are already connected as ~p~n", [UserName]),
			loop(Server,UserName,In_Chat,Friend_Id,Friend_Name,Is_Registered)
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
					Process_Name ! {connect,goOffline}
			end;
		_ ->
			io:format("could not communicate with process, terminating program"),
			exit(normal)
	end.

requestChat(Friend,Process_Name) ->
	Process_Name ! {self(),id_please},
	receive
		{From,my_id,Server,Name} ->
			io:format("Requesting Chat with ~p~n",[Friend]),
			Process_Name ! {chat,request},
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
