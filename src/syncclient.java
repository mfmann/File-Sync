import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.kohsuke.args4j.*;

import filesync.SynchronisedFile;


public class syncclient {
	
	public static void main(String[] args){
			
        ClientCommandLine line = new ClientCommandLine();
        CmdLineParser parser = new CmdLineParser(line);
        try {
        	parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(-1);
        }
    
		String fileName = line.filename();
		String hostName = line.hostname();
		int serverport = line.serverport();		
		int blocksize = line.blocksize();	
		String direction = line.direction();
		
		SynchronisedFile file = null;
		try{
			file = new SynchronisedFile(fileName, blocksize);		
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		
		//Creates a socket 
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		} 
		
		//Sends a negotiation message to the server
		NegotiationMessage negotiation = new NegotiationMessage(blocksize, direction);
		
		InetAddress serverAddress = null;
		try {
			serverAddress = InetAddress.getByName(hostName);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		 byte[] sendBuffer = negotiation.ToJSON().getBytes();
		 DatagramPacket toSend = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverport);
		
		 try {
			socket.send(toSend);
		
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		SyncInstructions instructions = new SyncInstructions(socket, file, blocksize, serverport, serverAddress);
		 
		if(direction.equals("push")){
			instructions.SyncAsClient();
		}else if(direction.equals("pull")){
			instructions.SyncAsServer();
		}		
	}
}
