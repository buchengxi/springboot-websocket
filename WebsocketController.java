package com.accp.springmvc.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/{id}")
public class WebsocketController {

	
	//��ſͻ��˶�Ӧ��mywebsocket����
//	private static CopyOnWriteArraySet<WebsocketController> websocketControllers = new CopyOnWriteArraySet<WebsocketController>();
	
	private static java.util.concurrent.ConcurrentHashMap<String,WebsocketController> websocketControllers = new java.util.concurrent.ConcurrentHashMap<String,WebsocketController>();
	
	private  Session session;//�Ự
	
	private String id; //��ʾÿһ��Session
	
	/**
	 * �������ӵ��÷���
	 */
	@OnOpen
	public void onOnpen(Session session,@PathParam("id") String id) {
		System.out.println("����������..."+id);
		this.session=session;
		this.id = id;
		
//		websocketControllers.add(this);//��ŵ�set��
		websocketControllers.put(this.id,this);//��ŵ�set��
		
	}
	
	/**
	 * ���ӹرյ��õķ���
	 */
	@OnClose
	public void onClose(){
		System.out.println("���ӹر�...");
//		websocketControllers.remove(this);  //��set��ɾ��
		websocketControllers.remove(this.id);  //��set��ɾ��
	}
	
	/**
	 * �յ��ͻ�����Ϣ����õķ���
	 * @param message �ͻ��˷��͹�������Ϣ
	 * @param session ��ѡ�Ĳ���
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		//��Ϣ�ĸ�ʽ:{"id":2,"message":"+message+"}
		//new JsonParser()
		//new JSONPObject()
		int i1 = message.indexOf(":");
		int i2 = message.indexOf(",");
		String id=message.substring(i1+1, i2);
		
		int i3 = message.lastIndexOf(":");
		//int i4 = message.lastIndexOf(",");
		String msg=message.substring(i3);
		
		System.out.println(id+"���Կͻ��˵���Ϣ:" + message);
		System.out.println(id+"---------");
		//ȡ�ö�Ӧ��id
		WebsocketController item  = websocketControllers.get(id);
		try {
			item.sendMessage(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Ⱥ����Ϣ
		/*for(WebsocketController item: websocketControllers){
			try {
				item.sendMessage(message);
//				this.session.getAsyncRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}*/
	}
	
	/**
	 * ��������ʱ����
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		System.out.println("��������..");
		error.printStackTrace();
	}
	/**
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException{
		System.out.println("��ʼ������Ϣ");
		//this.session.getBasicRemote().sendText(message);
		this.session.getAsyncRemote().sendText(message);
	}
}
