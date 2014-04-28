Cse 305 Homework 5
=======
-----------------------------Description-----------------------------------

The purpose of this project is to write a distributed chat system consisting
of one server and an arbitrary number of clients. 

* Guidlines:

			--> Chats are between two clients only

			--> Server keeps track of clients who are online

			--> Clients register with server to go online

			--> Clients must register with unique usernames

			--> If the name is in-use then registry fails

			--> Request to chat is sent to server

			--> If a client is unavailable, requesting client is told

			--> If a client is available, they are requested to chat

			--> If request is failed, no connection exists and originating
				client is told

			--> If accepted, the originating client is given the targeted
				client's address/pid

===========================================================================
