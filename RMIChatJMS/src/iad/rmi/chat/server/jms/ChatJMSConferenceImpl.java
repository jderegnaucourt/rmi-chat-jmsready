package iad.rmi.chat.server.jms;

import iad.rmi.chat.ChatMessage;
import iad.rmi.chat.client.ChatParticipant;
import iad.rmi.chat.server.ChatConferenceImpl;

import java.rmi.RemoteException;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ChatJMSConferenceImpl extends ChatConferenceImpl implements ChatJMSConference {

	private static final long serialVersionUID = 1L;
	protected boolean _bActiveLog = false;
	protected String _strPassword = "default";
	
	protected Queue _queue;
	protected QueueConnection _queueConection;
	protected QueueSession _queueSession;
	protected QueueSender _queueSender;
	protected ActiveMQConnectionFactory _connectionFactory;
	
	/**
	 * Class constructor
	 * @param name
	 * @param password
	 * @throws RemoteException
	 * @throws NamingException
	 */
	
	public ChatJMSConferenceImpl(String name, String password) throws RemoteException, NamingException {
		super(name);
		_strPassword = password;
	}
	
	
	public void addParticipant(ChatParticipant p) throws RemoteException  {
		super.addParticipant(p);
		if(_bActiveLog) {
			MapMessage newMsg;
			try {
				newMsg = _queueSession.createMapMessage();
				Date date = new Date();
				newMsg.setStringProperty("Type", "connection");
				newMsg.setString("type", "connection");
				newMsg.setString("date", date.toLocaleString());
				newMsg.setString("user", p.getName());
				newMsg.setString("text", this.getDescription());
				_queueSender.send(newMsg);
			} catch (JMSException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public void broadcast(ChatMessage message) throws RemoteException {
		super.broadcast(message);
		if(_bActiveLog) {
			MapMessage newMsg;
			try {
				newMsg = _queueSession.createMapMessage();
				Date date = new Date();
				newMsg.setStringProperty("Type", "broadcast");
				newMsg.setString("type", "broadcast");
				newMsg.setObject("user", message.getEmitter());
				newMsg.setString("date", date.toLocaleString());
				newMsg.setString("text", message.getContent());
				_queueSender.send(newMsg);
			} catch (JMSException e) {
				e.printStackTrace();
			}			
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void activateLog(String pwd) throws RemoteException {
		if(pwd.matches(_strPassword) && _bActiveLog == false) {
			_connectionFactory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD,
					ActiveMQConnection.DEFAULT_BROKER_URL
					);
			try {
				_queueConection = _connectionFactory.createQueueConnection();
				_queueSession = _queueConection.createQueueSession(false, _queueSession.AUTO_ACKNOWLEDGE);
				_queue = _queueSession.createQueue("myQueue");
				_queueSender = _queueSession.createSender(_queue);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		_bActiveLog = true;
		}
	}

	@Override
	public void desactivateLog(String pwd) throws RemoteException {
		try {
			_queueConection.close();
			_queueSender.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
}
