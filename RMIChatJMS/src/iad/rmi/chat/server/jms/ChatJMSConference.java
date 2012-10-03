package iad.rmi.chat.server.jms;

import iad.rmi.chat.server.ChatConference;

import java.rmi.RemoteException;

public interface ChatJMSConference extends ChatConference {

	public abstract void activateLog(String pwd) throws RemoteException;
	
	public abstract void desactivateLog(String pwd) throws RemoteException;
	
}
