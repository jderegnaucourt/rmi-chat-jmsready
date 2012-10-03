package iad.rmi.chat.client;

import iad.rmi.chat.ChatMessage;
import iad.rmi.chat.server.ChatConference;
import iad.rmi.chat.server.ConferenceFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ChatClientConsole extends Thread {
	
	protected ChatConference conference;
	protected ChatParticipant participant;
	protected String conferenceName;
	protected ConferenceFactory factory;
	protected BufferedReader reader; 
	
	public ChatClientConsole(ConferenceFactory cp, ChatParticipant p) {
		participant = p;
		factory = cp;
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void run() {
		String line; 
		printUsage();
	    while(true) {
	       try {
				printNextMessage();	
			    line = reader.readLine();
			    handleLine(line);
			    
		   } catch (Exception e) {
				e.printStackTrace();
		   }
	    }
	}
	
	private void handleLine(String line) throws IOException {
		String[] content = line.split(" ");
		if (line.equals("/stop")) {
	    	participant.leave(conference);
			reader.close(); 
			System.exit(0);
	    }
	    else if (line.equals("/list")) {
	    	System.out.println("*** Chat conferences List :");
	    	listChatRooms();
	    }
	    else if (line.equals("/status")) {
	    	if(participant.isConnected()) System.out.println("*** connected to : "+conferenceName);
	    	else System.out.println("*** you are not connected to any chat conference");
	    }
	    else if (line.equals("/leave")) {
	    	if(participant.isConnected()) participant.leave(conference);
	    	else System.out.println("*** you are not connected to any chat conference");
	    }
	    else if (line.equals("/help")) {
	    	printUsage();
	    }
	    else if (line.equals("/desc")) {
	    	if(participant.isConnected()) System.out.println("*** "+conference.getDescription());
	    	else System.out.println("*** you are not connected to any chat conference");
	    }	
		else if(content[0].matches("/create")) {
			if(!factory.newConference(content[1], content[2])) System.out.println("*** could not create conference : "+content[1]+", wrong password");
		}
		else if(content[0].matches("/join")) {
	    	ChatConference cf;
			try {
				if(java.rmi.Naming.lookup("//172.27.161.160:1099/"+content[1]) instanceof ChatConference) {
					cf = (ChatConference) java.rmi.Naming.lookup("//172.27.161.160:1099/"+content[1]);
					conference = cf;
		    		participant.join(conference);
			    	conferenceName = content[1];
				}
				else System.out.println("*** oops, this is not a chat conference");
			} catch (NotBoundException e) {
				System.out.println("*** conference "+content[1]+" doesnt exist");
			}
		}
		else participant.send(line);
	}
	
	private void printUsage() {
		System.out.println("*** how to use this console :");
    	System.out.println("/stop -> stops the app");
    	System.out.println("/list -> shows remote objects list (only conferences can be joined)");
    	System.out.println("/desc -> shows the description of the conference");
    	System.out.println("/create [name] [password] -> creates a new chatconference (default password is 'mypsd')");
    	System.out.println("/join [name] -> join the conference named in parameter");
    	System.out.println("/help -> shows this help");
    	System.out.println("*** have fun !");
	}

	private void listChatRooms() {
		try {
			String[] noms = Naming.list("rmi://172.27.161.160:1099");
			int l = noms.length;
			for(int i = 0; i<l ; i++) {
				System.out.println(noms[i]);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void printNextMessage() throws IOException {
		while (! reader.ready()) {
			   ChatMessage msg = null;
		        try {
					msg = participant.next();
		    	}
		    	catch(RemoteException e) {
		    		e.printStackTrace();
		    	}
				if (msg != null) {
					if(msg.getEmitter().matches("system")) System.out.println("*** "  + msg.getContent());
					else System.out.println("["+msg.getEmitter() + "] "  + msg.getContent());
				}
		}
	}
}

