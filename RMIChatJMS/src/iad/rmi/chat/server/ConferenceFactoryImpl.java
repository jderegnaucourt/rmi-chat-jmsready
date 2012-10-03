package iad.rmi.chat.server;

import iad.rmi.chat.server.jms.ChatJMSConference;
import iad.rmi.chat.server.jms.ChatJMSConferenceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import javax.naming.NamingException;

public class ConferenceFactoryImpl extends UnicastRemoteObject implements ConferenceFactory {
	protected Registry _registry = null;
	protected String _password = "mypsd";
	
	protected ConferenceFactoryImpl(Registry reg) throws RemoteException {
		super();
		_registry = reg;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public boolean newConference(String name, String password)
			throws RemoteException {
		System.out.println("creating new conference on server side : "+name);
		if(password.matches(_password)) {
			ChatJMSConference chatConference;
			try {
				chatConference = new ChatJMSConferenceImpl(name,  "default");
				chatConference.start();		
				chatConference.activateLog("default");
				System.out.println("conference "+name+" started.");
				_registry.rebind(name, chatConference);
			} catch (NamingException e) {
				e.printStackTrace();
			}
			return true;
		}
		else return false;	
	}
}
