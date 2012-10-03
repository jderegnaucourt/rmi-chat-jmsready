package iad.rmi.chat.client.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

public class MessageParser {
	
	public static void ParseAndPrint(Message msg) {
		MapMessage mpmesg = (MapMessage) msg;
		
		try {
			System.out.println("<new message>");
			System.out.println(mpmesg.getString("type"));
			System.out.println(mpmesg.getString("date"));
			System.out.println(mpmesg.getString("user"));
			System.out.println(mpmesg.getString("text"));
			System.out.println("</new message>");
		} catch (JMSException e) {
			e.printStackTrace();
		}	
	}
}
