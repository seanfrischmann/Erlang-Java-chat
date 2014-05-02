%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Client Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(client).
-export([start/0, loop/1]).

start(Server) ->
		spawn(client, loop, [Server, 0, false]).

loop(Server, Name, Registered) ->
		if Registered == false ->
				User_Name = io:get_line("Enter a username: ")
				{frischkro,Server} ! {self(), {connect, User_Name}},
				receive
					true ->
						io:format("You are now connected"),
						start(Server, User_Name, true);
					false ->
						io:format("Unfortunately, that name is taken"),
						start(Server, 0, false)
				end;
		receive
			{r, W, H} ->
				{frischkro,Server} ! { self(), {rectangle, W, H} },
				io:format("Message sent.~n")
				loop(Server);
			{c, R} ->
				Server ! { self(), {circle, R} },
				io:format("Message sent.~n")
				loop(Server);
			{Server, Area} ->
				io:format("Message received.~n")
				loop(Server);
			_ ->
				io:format("I'm sorry, could you repeat that?~n"),
				loop(Server)
end.
