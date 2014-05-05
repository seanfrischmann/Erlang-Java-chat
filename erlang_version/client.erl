%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Client Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(client).
-export([start/1, loop/3]).

start(Server) ->
		spawn(client,loop,[Server, 0, false]).

loop(Server,Name,Registered) ->
		case Registered of
			false ->
				User_Name = string:strip(io:get_line("Enter a username: "),both,$\n),
				{frischkro,Server} ! {self(), {connect, User_Name}},
				receive
					{connect,true} ->
						io:format("You are now connected~n"),
						loop(Server, User_Name, true);
					{connect,false} ->
						io:format("Unfortunately, that name is taken~n"),
						loop(Server, 0, false)
				end;
			true ->
				Command = string:strip(io:get_line("Enter a command: "),both,$\n),
				case Command of
					"quit" ->
						{frischkro,Server} ! {self(), {disconnect, User_Name}},
						exit(normal);
					_ ->
						io:format("Did not understand command, please try again~n"),
						loop(Server,Name,Registered)
				end;
			_ ->
				io:format("Unexpected error, program exiting"),
				exit(normal)
		end.
