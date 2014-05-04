-module(t).
-export([con/2]).

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
			lists:append([X],Y)
	end.
