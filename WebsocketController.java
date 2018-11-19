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

	
	//存放客户端对应的mywebsocket对象
//	private static CopyOnWriteArraySet<WebsocketController> websocketControllers = new CopyOnWriteArraySet<WebsocketController>();
	
	private static java.util.concurrent.ConcurrentHashMap<String,WebsocketController> websocketControllers = new java.util.concurrent.ConcurrentHashMap<String,WebsocketController>();
	
	private  Session session;//会话
	
	private String id; //表示每一个Session
	
	/**
	 * 建立连接调用方法
	 */
	@OnOpen
	public void onOnpen(Session session,@PathParam("id") String id) {
		System.out.println("建立了连接..."+id);
		this.session=session;
		this.id = id;
		
//		websocketControllers.add(this);//存放到set中
		websocketControllers.put(this.id,this);//存放到set中
		
	}
	
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(){
		System.out.println("连接关闭...");
//		websocketControllers.remove(this);  //从set中删除
		websocketControllers.remove(this.id);  //从set中删除
	}
	
	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 * @param session 可选的参数
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		//消息的格式:{"id":2,"message":"+message+"}
		//new JsonParser()
		//new JSONPObject()
		int i1 = message.indexOf(":");
		int i2 = message.indexOf(",");
		String id=message.substring(i1+1, i2);
		
		int i3 = message.lastIndexOf(":");
		//int i4 = message.lastIndexOf(",");
		String msg=message.substring(i3);
		
		System.out.println(id+"来自客户端的消息:" + message);
		System.out.println(id+"---------");
		//取得对应的id
		WebsocketController item  = websocketControllers.get(id);
		try {
			item.sendMessage(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//群发消息
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
	 * 发生错误时调用
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		System.out.println("发生错误..");
		error.printStackTrace();
	}
	/**
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException{
		System.out.println("开始发送消息");
		//this.session.getBasicRemote().sendText(message);
		this.session.getAsyncRemote().sendText(message);
	}
}
