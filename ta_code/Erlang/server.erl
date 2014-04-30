-module(server).
-export([start/0]).

start() -> spawn(fun loop/0).

loop() ->
       receive
		{From, Message} ->
        	io:format("Server: received a message!~p~n",[Message]),
	  		From ! {self(), Message },
	  		loop()
end.
