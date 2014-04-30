%%%-----------------------------------------------------------------------
%%% Practice Client Code
%%% Title: Programming Erlang: Software for a Concurrent World
%%% Author: Joe Armstrong
%%% Page: 140
%%%-----------------------------------------------------------------------

-module(practice_client).
-export([start/0, loop/1]).

start() ->
		Server = practice_server:start(),
		spawn(practice_client, loop, [Server]).

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
