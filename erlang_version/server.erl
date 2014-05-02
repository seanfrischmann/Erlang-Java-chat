%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Server Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(server)
-export([start/0])

start() ->
	List = [],
	register(frischkro,spawn(server,loop,[List])).

clientManager(Name,List,Action) ->
	case Action of
		connect ->
			lists:append(Name,List);
		disconnect ->
			lists:delete(Name,List)
	end.

loop(List) ->
		io:format("Server: waiting for a message...~n"),
		receive
			{From, {connect, Name}} ->
				case lists:keymember({Name,From},List) of
					true ->
						From ! false,
						loop(List);
					false ->
						From ! true,
						loop(List)
				end;
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
