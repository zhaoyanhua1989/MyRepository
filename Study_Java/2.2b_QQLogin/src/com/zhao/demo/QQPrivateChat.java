package com.zhao.demo;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.zhao.demo.utils.ConstantUtil;
import com.zhao.demo.utils.SocketClient;
import com.zhao.demo.utils.StringUtil;

/**
 * 私人聊天模块
 * 
 * @author HKW2962
 *
 */
public class QQPrivateChat extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton sendBt = new JButton("发送");
	private JTextField txtMess = new JTextField();
	private JTextArea txtContent = new JTextArea();
	private JComboBox<String> cmbUser = new JComboBox<String>();
	private JFrame frame;
	private SocketClient sc;
	private String selfUid;

	public QQPrivateChat(SocketClient sc) {
		this.sc = sc;
		frame = QQLogin.getFrameInstance();
		if (sc.getUid() != null) {
			selfUid = sc.getUid();
			frame.setTitle(selfUid);
		}
		frame.setSize(300, 400);
		frame.setLocationRelativeTo(null);
		showContent();
		frame.setVisible(true);
	}

	private void showContent() {
		JPanel panChat_button = new JPanel();
		panChat_button.setLayout(new GridLayout(1, 2));
		panChat_button.add(cmbUser);
		panChat_button.add(sendBt);

		sendBt.addActionListener(this);

		JPanel panChat = new JPanel();
		panChat.setLayout(new GridLayout(2, 1));
		panChat.add(txtMess);
		panChat.add(panChat_button);

		JScrollPane spContent = new JScrollPane(txtContent);
		showChatMsg();

		frame.setLayout(new BorderLayout());
		frame.add(panChat, BorderLayout.NORTH);
		frame.add(spContent, BorderLayout.CENTER);

		// 监听从服务端返回的消息
		ListenMsg();
	}

	private void ListenMsg() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean loop = true;
				while (loop) {
					try {
						if (sc.getBufferedReader() != null) {
							String msg = sc.getBufferedReader().readLine();
							System.out.println("收到消息：" + msg);
							switch (StringUtil.getType(msg)) {
							case ConstantUtil.PRIVATE_CHAT:// 处理私聊信息
								showPrivateChatMsg(msg);
								break;
							case ConstantUtil.REFRESH_CONTACTS:// 刷新所有联系人信息
								refreshAllContacts(msg);
								break;
							case ConstantUtil.EXITACCOUNT:// 有其他用户离线时刷新联系人信息
								refreshRemoveContacts(msg);
								break;
							default:
								JOptionPane.showMessageDialog(QQPrivateChat.this, "消息接收异常");
								break;
							}
						}
					} catch (SocketException se) {
						JOptionPane.showMessageDialog(QQPrivateChat.this, "抱歉,服务端异常");
						loop = false;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	protected void refreshRemoveContacts(String msg) {
		cmbUser.removeItem(msg.split("%")[1]);
	}

	protected void refreshAllContacts(String msg) {
		String[] uids = msg.split("%");
		for (int i = 1; i < uids.length; i++) {
			cmbUser.addItem(uids[i]);
		}
	}

	protected void showPrivateChatMsg(String msg) {
		System.out.println("收到消息：" + msg);
		saveChatMsg(msg.split("%")[2] + ":" + msg.split("%")[3]);
		txtContent.setText("");
		showChatMsg();
	}

	/**
	 * 点击发送消息按钮，将消息存入本地文件并发送消息
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "发送":
			String sendToUid = cmbUser.getSelectedItem().toString();
			String inputMsg = txtMess.getText().toString();
			String msg = selfUid + ":" + inputMsg;
			// 保存聊天记录并显示
			saveChatMsg(msg);
			txtMess.setText("");
			txtContent.setText("");
			msg = ConstantUtil.PRIVATE_CHAT + "%" + sendToUid + "%" + selfUid + "%" + inputMsg;
			System.out.println("客户端发送消息:" + msg);
			showChatMsg();

			// 发送消息
			try {
				sc.getPrintWriter().println(msg);
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			break;
		}
	}

	/**
	 * 读取聊天记录
	 */
	private void showChatMsg() {
		try {
			File f = new File("E:/HM/temp/" + selfUid + "QQ连天记录.qq");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				txtContent.append(br.readLine() + "\n"); // 用追加模式，而不是用setText()
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 储存聊天记录
	 */
	private void saveChatMsg(String msg) {
		try {
			File f = new File("E:/HM/temp/" + selfUid + "QQ连天记录.qq");
			FileWriter fw = new FileWriter(f, true); // ****true表明是追加模式
			PrintWriter pw = new PrintWriter(fw); // ****这里追加true，表明自动刷新
			pw.println(msg); // 这里用换行写入println，不用write
			pw.flush();
			fw.close();
			pw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
