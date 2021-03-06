package iad.rmi.chat.server;

import iad.rmi.chat.server.jms.ChatJMSConference;
import iad.rmi.chat.server.jms.ChatJMSConferenceImpl;

import java.rmi.registry.*;

public class ChatServer {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	
	public static void main(String[] args) throws Exception {
		try {
			System.out.println("Creating Default ChatConference");
			ChatJMSConference chatConference;
			chatConference = new ChatJMSConferenceImpl("ChatConference", "default");
			chatConference.start();
			System.out.println("Activates Log for Default ChatConference");
			chatConference.activateLog("default");
			System.out.println("Creating Registry");
			Registry reg = LocateRegistry.createRegistry(1099);			
			ConferenceFactory cp = new ConferenceFactoryImpl(reg);		
			
			System.out.println("Server is ready");
			reg.rebind("ChatConference", chatConference);
			reg.rebind("ChatConferenceFactory", cp);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
