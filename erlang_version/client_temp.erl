%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Client Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(client_temp).
-export([start/1, loop/5]).

start(Server) ->
		loop(Server,0,false,false,0).

loop(Server,Name,Registered,Chat_Connected,Friend) ->
		if
			Chat_Connected ->
				Chat_cmd = string:strip(io:get_line("Enter a chat command: "),both,$\n),
				case Chat_cmd of
					"send message" ->
						Chat_msg = string:strip(io:get_line("Enter a message: "),both,$\n),
						Friend ! {message,Chat_msg},
						loop(Server,Name,Registered,true,Friend);
					"quit chat" ->
						Friend ! quit_chat,
						io:format("Exiting chat...~n"),
						loop(Server,Name,Registered,false,0);
					"check messages" ->
						false;
					_ ->
						io:format("unknown chat command~n"),
						loop(Server,Name,Registered,true,Friend)
				end,
				receive
					quit_chat ->
						io:format("Chat has been disconnected...~n"),
						loop(Server,Name,Registered,false,0);
					{message,Msg} ->
						io:format("~p~n",[Msg]),
						loop(Server,Name,Registered,Chat_Connected,Friend);
					_ ->
						io:format("You have no messages~n"),
						loop(Server,Name,Registered,Chat_Connected,Friend)
				end;
			true ->
				false
		end,
		case Registered of
			false ->
				User_Name = string:strip(io:get_line("Enter a username: "),both,$\n),
				{frischkro,Server} ! {self(), {connect, User_Name}},
				receive
					{connect,true} ->
						io:format("You are now connected~n"),
						loop(Server, User_Name, true,false,0);
					{connect,false} ->
						io:format("Unfortunately, that name is taken~n"),
						loop(Server, 0, false,false,0)
				end;
			true ->
				Command = string:strip(io:get_line("Enter a command: "),both,$\n),
				case Command of
					"quit" ->
						{frischkro,Server} ! {self(), {disconnect, Name}},
						exit(normal);
					"request chat" ->
						Friend_Request = string:strip(io:get_line("With who: "),both,$\n),
						{frischkro,Server} ! {self(), {request, Name, Friend_Request}};
					"check requests" ->
						false;
					_ ->
						io:format("Did not understand command, please try again~n"),
						loop(Server,Name,Registered,Chat_Connected,Friend)
				end,
				receive
					{accept,Friend_temp,FriendName} ->
						io:format("~p would like to chat ",[FriendName]),
						Friend_accept = string:strip(io:get_line("[yes/no]"),both,$\n),
						case Friend_accept of
							"yes" ->
								{frischkro,Server} ! {self(), {accepted, true, Friend_temp}},
								loop(Server,Name,Registered,true,Friend_temp);
							"no" ->
								{frischkro,Server} ! {accepted, false, Friend_temp},
								loop(Server,Name,Registered,false,Friend);
							_ ->
								io:format("unknown command, chat denied~n"),
								{frischkro,Server} ! {accepted, false, Friend_temp},
								loop(Server,Name,Registered,false,Friend)
						end;
					{chat,accepted,Friend_temp} ->
						io:format("You are connected to a chat~n"),
						loop(Server,Name,Registered,true,Friend_temp);
					{chat,rejected} ->
						io:format("Sorry that user is not online or unavailable~n"),
						loop(Server,Name,Registered,false,Friend)
				end;
			_ ->
				io:format("Unexpected error, program exiting"),
				exit(normal)
		end.
