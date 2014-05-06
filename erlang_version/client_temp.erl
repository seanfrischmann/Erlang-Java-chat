%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Client Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(client_temp).
-export([start/1, loop/0, goOnline/2]).


start(Server) ->
	spawn(client,loop,[Server,0,false,false,0]).

loop() ->
	receive
		{connect,true} ->
			io:format("You are now connected~n");
		{connect,false} ->
			io:format("Unfortunately, that name is taken~n");
		{connect,already,UserName} ->
			io:format("You are already connected as ~p~n", [UserName])
	end.

goOnline(Server,Name) ->
	{frischkro,Server} ! {self(), {connect, Name}}.
