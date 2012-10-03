package iad.rmi.chat.client.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSClientPush {
	
	/**
	 * entry point
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		Queue _queue;
		QueueConnection _queueConection;
		QueueReceiver _queueReceiver = null;
		QueueSession _queueSession = null;
		
		
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
			JMSPushListener jmsl = new JMSPushListener();
			_queueReceiver.setMessageListener(jmsl);
			_queueConection.start();
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
