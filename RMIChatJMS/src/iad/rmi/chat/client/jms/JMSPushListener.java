package iad.rmi.chat.client.jms;

import javax.jms.Message;
import javax.jms.MessageListener;

public class JMSPushListener implements MessageListener {

	@Override
	public void onMessage(Message arg0) {
		MessageParser.ParseAndPrint(arg0);
	}

}
