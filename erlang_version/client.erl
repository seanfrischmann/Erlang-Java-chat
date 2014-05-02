%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Client Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(client).
-export([start/1, loop/3]).

start(Server) ->
		spawn(client, loop, [Server, 0, false]).

loop(Server, Name, Registered) ->
		case Registered of
			false ->
				User_Name = io:get_line("Enter a username: "),
				{frischkro,Server} ! {self(), {connect, User_Name}}
		end,
		receive
			true ->
				io:format("You are now connected"),
				loop(Server, User_Name, true);
			false ->
				io:format("Unfortunately, that name is taken"),
				loop(Server, 0, false)
		end.
