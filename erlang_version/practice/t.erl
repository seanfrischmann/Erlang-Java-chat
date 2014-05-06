-module(t).
-export([con/1]).

con(X) ->
	case X of
		"sean" ->
			io:format("Sean is Awesome~n");
		_ ->
			false
	end,
	io:format("gabe is alright~n").
