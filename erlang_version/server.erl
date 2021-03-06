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
			{From, {accepted, true, Friend_Request, Name}} ->
				io:format("Server: Chat was acccepted~n"),
				Friend_Request ! {chat,accepted,From,Name},
				loop(List);
			{accepted, false, Friend_temp} ->
				io:format("Server: Chat was rejected~n"),
				Friend_temp ! {chat,rejected},
				loop(List);
			{accepted, already, Friend_temp} ->
				io:format("Server: Client is busy~n"),
				Friend_temp ! {chat,already},
				loop(List);
			{From, {request, Name, Friend_Request}} ->
				case lists:keymember(Friend_Request,1,List) of
					true ->
						io:format("sending chat request~n"),
						Temp = lists:keyfind(Friend_Request,1,List),
						{_,Friend} = Temp,
						list_to_pid(Friend) ! {accept,From,Name},
						loop(List);
					false ->
						io:format("User not available~n"),
						From ! {chat,unavailable},
						loop(List)
				end;
			{From, {connect, Name}} ->% This is used to check for username %
				case List /= [] of
					true ->
						case lists:keymember(pid_to_list(From),2,List) of
							false ->
								case lists:keymember(Name,1,List) of
									true ->
										From ! {connect,false},
										io:format("Server: Username already in use~n"),
										loop(List);
									false ->
										From ! {connect,true,Name},
										loop(clientManager({Name,pid_to_list(From)},List,connect))
								end;
							true ->
								Temp = lists:keyfind(pid_to_list(From),2,List),
								{UserName,_} = Temp,
								From ! {self(), {connect,already,UserName}},
								loop(List)
						end;
					false ->
						List_temp = clientManager({Name,pid_to_list(From)},List,connect),
						From ! {connect,true,Name},
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
