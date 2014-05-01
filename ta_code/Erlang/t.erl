-module(t).
-export([con/2]).

co(X,Y) ->
	case {X,Y} of
		{3,4} ->
			io:format("Hooray~n"),
			7;
		{_,_} ->
			io:format("oops~n")
	end.

con(X,Y) ->
	case lists:member(X,Y) of
		true ->
			case X of
				sean ->
					io:format("Sean is Awesome~n"),
					io:format("Sorry already member");
				_ ->
					io:format("Sorry already member")
			end;
		false ->
			lists:append([X],Y),
			t:con(sean,Y)
	end.
