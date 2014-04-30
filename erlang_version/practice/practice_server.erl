%%%-----------------------------------------------------------------------
%%% Practice Server Code
%%% Title: Programming Erlang: Software for a Concurrent World
%%% Author: Joe Armstrong
%%% Page: 140
%%%-----------------------------------------------------------------------

-module(practice_server)
-export([start/0])

start() -> spawn(fun loop/0).

loop() ->
		io:format("Server: waiting for a message...~n"),
		receive
			{From, {rectangle, Width, Height}} ->
				io:format("Server: received a rectangle message!~n"),
				From ! {self(), Width * Height },
				loop();
			{From, {circle, Radius}} ->
				io:format("Server: received a circle message!~n"),
				From ! {self(), 3.14159 * Radius * Radius},
				loop();
			{From, Other} ->
				io:format("Server: received an unknown message!~n"),
				From ! {self(), {error, Other}},
				loop()
end.
