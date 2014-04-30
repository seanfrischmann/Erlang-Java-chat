-module(client).
-export([start/0, loop/1]).

start() ->
	Server = server:start(),
	%spawn(client, loop, [Server]),
	loop(Server).
	

loop(Server) ->
		Message = io:get_line("Enter Something:"),
		Server ! {self(),Message},
		receive
       		{_,Message} -> 
	  		io:format("Server Responded:~p~n",[Message]),
	  		loop(Server)
end.
