package iad.rmi.chat.client.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSClientPull {
	
	
	/**
	 * entry point
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		Queue _queue;
		QueueConnection _queueConection;
		QueueSession _queueSession = null;
		QueueReceiver _queueReceiver = null;
		ActiveMQConnectionFactory _connectionFactory;
	
		_connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				ActiveMQConnection.DEFAULT_BROKER_URL
				);
		try {
			_queueConection = _connectionFactory.createQueueConnection();
			_queueSession = _queueConection.createQueueSession(false, _queueSession.AUTO_ACKNOWLEDGE);
			_queue = _queueSession.createQueue("myQueue");
			_queueReceiver = _queueSession.createReceiver(_queue);
			_queueConection.start();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		while(true) {
			try {
				Message msg = _queueReceiver.receive();
				if(msg != null) MessageParser.ParseAndPrint(msg);
			} catch (JMSException e) {
				e.printStackTrace();
			}			
		}
	}

}
