%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Server Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(server).
-export([start/0, loop/1]).
-import(lists,[keymember/3]).

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
						clientManager({Name,From},List,connect),
						From ! true,
						loop(List)
				end;
			{From, Other} ->
				io:format("Server: received an unknown message!~n"),
				From ! {self(), {error, Other}},
				loop(List)
		end.
