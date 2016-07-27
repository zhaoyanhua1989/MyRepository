package com.zhao.demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.zhao.demo.utils.ConstantUtil;
import com.zhao.demo.utils.SQLUtil;
import com.zhao.demo.utils.SocketClient;

public class QQLoginServer {

	public static void main(String[] args) {
		HashMap<String, SocketClient> hm = new HashMap<String, SocketClient>();
		ServerSocket ss;
		try {
			ss = new ServerSocket(8000);
			// ss.setSoTimeout(10*1000); //设置延迟
			while (true) {
				/**
				 * ss.accept()监听，等待连接，一旦有client端连接便创建socket实例，的这一方法可以说是阻
				 * 塞式的,没有client端连接就一直监听着,等待连接.直到有client端连接进来才通过socket实例 与
				 * client端进行交互,一个server端可以被多个client端连接,每连接一次都会创建一个socket
				 * 实例,派发服务线程.
				 */
				Socket s = ss.accept();
				if (s != null) {
					SocketClient sc = new SocketClient(s);
					MyService ms = new MyService(sc);
					ms.setHashMap(hm);
					ms.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class MyService extends Thread {

	private HashMap<String, SocketClient> hm = new HashMap<String, SocketClient>();
	private SocketClient sc;

	public MyService(SocketClient sc) {
		this.sc = sc;
	}

	public void setHashMap(HashMap<String, SocketClient> hm) {
		this.hm = hm;
	}

	@Override
	public void run() {
		try {
			checkAndSend();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//检查登录
	private void checkAndSend() throws IOException {
		String acceptMsg = sc.getBufferedReader().readLine();
		String uid = null;
		String passWord = null;
		if (acceptMsg.startsWith(ConstantUtil.LOGINACCOUNT)) {
			// 服务端验证客户端传来的账号密码,这里有个bug，用"|"分割split出来的异常？？
			uid = acceptMsg.split("%")[1];
			passWord = acceptMsg.split("%")[2];
			System.out.println("uid=" + uid + ",passWord=" + passWord);
			// 返回校验结果给客户端
			if (SQLUtil.DoLoginQueryAndCheck(uid, passWord)) {
				sc.getPrintWriter().println("true");
				System.out.println("login check success");

				//刷新联系人
				dealRefreshAccountMsg(uid);

				// 不断接收客户端发过来的消息
				dealSendMsg(uid);
			} else {
				sc.getPrintWriter().println("false");
			}
		}
	}

	// 刷新联系人
	private void dealRefreshAccountMsg(String uid) {
		//将本人上线消息发给其他人
		for (SocketClient qs : hm.values()) {
			qs.getPrintWriter().println(ConstantUtil.REFRESH_CONTACTS + "%" + uid);
		}

		//将其他人在线信息发给自己
		StringBuilder sb = new StringBuilder();
		sb.append(ConstantUtil.REFRESH_CONTACTS);
		for (String id : hm.keySet()) {
			sb.append("%" + id);
		}
		sc.getPrintWriter().println(sb.toString());

		// 将本人的用户名和SocketClient存入HashMap
		hm.put(uid, sc);
	}

	//处理发送消息
	private void dealSendMsg(String uid) throws IOException {
		boolean loop = true;
		while (loop) {
			String msg = sc.getBufferedReader().readLine();
			System.out.println("接收到客户端消息：" + msg);
			if(msg.startsWith(ConstantUtil.EXITACCOUNT)) {
				// 处理退出刷新联系人消息
				// 移除hm中的uid
				System.out.println("收到exit");
				hm.remove(uid);
				// 将本人退出的消息发给其他人
				for (SocketClient qs : hm.values()) {
					qs.getPrintWriter().println(ConstantUtil.EXITACCOUNT + "%" + uid);
				}
				sc = null;
				loop = false;
			} else if (msg.startsWith(ConstantUtil.PRIVATE_CHAT)){
				//处理私聊消息
				String sendToUid = msg.split("%")[1];
				for(String quid : hm.keySet()) {
					if(quid.equals(sendToUid)) {
						hm.get(quid).getPrintWriter().println(msg);
					}
				}
			}
		}
	}
}
