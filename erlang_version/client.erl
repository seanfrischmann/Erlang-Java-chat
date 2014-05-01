%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Client Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(client).
-export([start/0, loop/1]).

start() ->
		Name = io:get_line("Enter a username: ")
		Server = server:serverSocket(), % We want to connect an already running server
		spawn(client, loop, [Server]). % This creates the process client socket

loop(Server) ->
		receive
			{r, W, H} ->
				Server ! { self(), {rectangle, W, H} },
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
