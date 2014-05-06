%%%-----------------------------------------------------------------------
%%% Homework 5
%%% Client Code
%%% Cse 305
%%% Author: Sean Frischmann
%%%-----------------------------------------------------------------------

-module(client_temp).
-export([start/1, loop/1, goOnline/2]).


start(Server) ->
	spawn(client_temp,loop,[Server]).

loop(Server) ->
	io:format("waiting for message...~n"),
	receive
		{connect,true} ->
			io:format("You are now connected~n"),
			loop(Server);
		{connect,false} ->
			io:format("Unfortunately, that name is taken~n"),
			loop(Server);
		{connect,already,UserName} ->
			io:format("You are already connected as ~p~n", [UserName]),
			loop(Server)
	end.

goOnline(Server,Name) ->
	{frischkro,Server} ! {self(), {connect, Name}}.
