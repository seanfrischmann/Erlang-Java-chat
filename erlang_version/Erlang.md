Erlang Version
==============
-----------------------------Commands-----------------------------------
*Using erlang interpreter

    --> start erlang interpreter by erl -sname server -setcookie chocolateChip
    --> in a seperate interpreter, erl -sname client1 -setcookie chocolateChip
    --> repeat the above line (changing client by incrementing number) for as many clients as you want

* server:start().

      --> this command is in the interpreter with sname of server
      --> starts the server end and waits for messages from clients
      --> to exit cntrl c followed by cntrl d

* client:start(SERVER,client1)

      --> this command is in the interpreter with sname of client1
      --> Your Server is going to be server@someaddress should appear in the interpreter in the paranthesis
      
* client:goOnline("Name",client1)

      --> Connects you to the server@someaddress with a unique username
      --> "Name" can be replace by a name of your choice, should be unique per client ex. "Sean"
      --> If name is in use will send error message and you'll have to pick a different name
      
* client:goOffline(client1)

      --> will remove your "Name" from the server
      --> Error if you are already offline
      
* client:requestChat("Name",client1)

      --> sends request to server to chat with "Name"
      --> If they are not online or reject your chat you will receive notification
      --> If they accept you will be connected to a chat with "Name"
      --> If you are connected with one person, you cannot connect with another
      --> If they are connected with someone, they will reject your request
      
* client:sendMessage("Message",client1)

      --> replace Message with what you want to send to your connected client ex. "hello, how are you"
      --> If you are not connected to someone, you will be notified
      
* client:quitChat(client1)

      --> quits the chat your in and send a message to notify other client that you quit the chat
