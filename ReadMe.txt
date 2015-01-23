File Sync is a client/server program that synchronises a file on the server to a file on the client (or visa versa). 

All message sent between machines are sent using synchronous UDP and are JSON encoded. Each message has a counter to ensure that no messages are received twice.

An initial negotiation message is sent from the client to the server the determines certain variables.

If the client is acting as the source, the client will call check the file to see if any changes have been made at a certain time interval. If so, the client will send an instruction to the server to update the file. The server can reply in 3 ways:

	1. If the server receives the instruction and it is the message it is expecting (determined by the counter), it will send an acknowledgment back to the client. 

	2. If the client times out before receiving an expected acknowledgement from the client, the client will send the instruction again.

	3. If the server receives a message that is our of order, the server will send a message telling the client which message they are expecting and the client will resend that message. 

This process will continue until both files are successfully synchronised and identical. 



The file syncserver.jar must be executed on the server as follows:

	java -jar syncserver.jar -file filename [-p serverport]

-p = port server will listening for incoming messages from client on. Default port is 4144 unless specified.



The file syncclient.jar must be executed on the client as follows:

	java -jar syncclient.jar -file filename -hostname hostname [-p serverport] [-b blocksize] [-d direction]

-p = Port of server client will connect with. Default is 4144 unless specified.

-b = Size of blocks of the file (in bytes) to be sent and synchronised in a message. Default block size is 1024 bytes.

-d = Direction. If direction is push, client will be pushing instructions to the server, if pull, will be receiving instructions from the server.


