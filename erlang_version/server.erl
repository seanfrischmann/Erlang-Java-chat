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
			io:format("~p was connected~n", [Name]),
			lists:append([Name],List);
		disconnect ->
			io:format("~p was disconnected~n", [Name]),
			lists:delete(Name,List)
	end.

loop(List) ->
		io:format("Server: waiting for a message...~n"),
		receive
			{From, {connect, Name}} ->
				case List /= [] of
					true ->
						case lists:keymember({Name,pid_to_list(From)},1,List) of
							true ->
								From ! {connect,false},
								loop(List);
							false ->
								From ! {connect,true},
								loop(clientManager({Name,pid_to_list(From)},List,connect))
						end;
					false ->
						List_temp = clientManager({Name,pid_to_list(From)},List,connect),
						From ! {connect,true},
						loop(List_temp)
				end;
			{From, {disconnect, Name}} ->
				List_temp = clientManager({Name,pid_to_list(From)},List,disconnect),
				loop(List_temp);
			{From, Other} ->
				io:format("Server: received an unknown message!~n"),
				From ! {self(), {error, Other}},
				loop(List)
		end.
