%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Server Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(server).
-export([start/0, loop/1]).

start() ->
	List = [],
	register(frischkro,spawn(server,loop,[List])).

clientManager(Name,List,Action) ->
	case Action of
		connect ->
			lists:append([Name],List);
		disconnect ->
			lists:delete(Name,List)
	end.

loop(List) ->
		io:format("Server: waiting for a message...~n"),
		receive
			{From, {connect, Name}} ->
				case List /= [] of
					true ->
						case lists:keymember({Name,pid_to_list(From)},List) of
							true ->
								From ! false,
								loop(List);
							false ->
								From ! true,
								loop(clientManager({Name,pid_to_list(From)},List,connect))
						end;
					false ->
						List_temp = clientManager({Name,pid_to_list(From)},List,connect),
						From ! true,
						loop(List_temp)
				end;
			{From, Other} ->
				io:format("Server: received an unknown message!~n"),
				From ! {self(), {error, Other}},
				loop(List)
		end.
