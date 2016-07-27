package com.zhao.demo;

import java.awt.*;
import java.awt.event.*;
import java.net.*;

import javax.swing.*;

import com.zhao.demo.utils.ConstantUtil;
import com.zhao.demo.utils.SocketClient;
import com.zhao.demo.utils.StringUtil;

/**
 * 登录模块
 * 
 * @author HKW2962
 * 
 *         遇到问题：从QQLogin加载QQMain正常，然后从QQMain加载QQPrivateChat异常，显示不了内容
 *
 */
public class QQLogin extends JFrame implements ActionListener/** 按钮类型的接口 */
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static JPanel panLogin;
	private static JPanel panButton;
	private static JTextField account;
	private static JPasswordField passWord;
	private static SocketClient sc;
	private static String uid;

	public static JFrame getFrameInstance() {
		return frame;
	}

	public static void main(String[] args) {
		frame = new JFrame("QQ");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				System.exit(0);
			}
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				if(!StringUtil.isNull(uid) && sc != null) {
					System.out.println("拿到了exit");
					sc.getPrintWriter().println(ConstantUtil.EXITACCOUNT);
				}
			}
		});
		frame.setSize(250, 125);
		frame.setLocationRelativeTo(null); // 设置frame的屏幕位置，当参数是null时在屏幕中间

		panLogin = new JPanel();
		account = new JTextField();
		passWord = new JPasswordField();
		panLogin.setLayout(new GridLayout(2, 2));
		panLogin.add(new JLabel("账号："), BorderLayout.EAST);
		panLogin.add(account);
		panLogin.add(new JLabel("密码："), BorderLayout.EAST);
		panLogin.add(passWord);

		panButton = new JPanel();
		JButton loginBt = new JButton("登录");
		;
		JButton cancelBt = new JButton("取消");
		JButton registerBt = new JButton("注册");
		panButton.setLayout(new FlowLayout());
		panButton.add(loginBt);
		panButton.add(cancelBt);
		panButton.add(registerBt);

		// 添加监听begin
		QQLogin m = new QQLogin();
		loginBt.addActionListener(m);
		cancelBt.addActionListener(m);
		registerBt.addActionListener(m);
		// 添加监听end

		frame.setLayout(new BorderLayout());
		frame.add(panLogin, BorderLayout.CENTER);
		frame.add(panButton, BorderLayout.SOUTH);
		frame.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("监听到按键 action = " + e.getActionCommand());
		switch (e.getActionCommand()) {
		case "登录":
			login();
			break;
		case "取消":
			System.exit(0);
			break;
		case "注册":

			break;
		}
	}

	private void login() {
		// 拿到账号密码，交给服务端验证
		uid = account.getText().toString();
		String pass = new String(passWord.getPassword()).toString();
		if (StringUtil.isNull(uid) || StringUtil.isNull(pass)) {
			JOptionPane.showMessageDialog(this, "请确保用户名和密码不为空");
			return;
		}
		String token = ConstantUtil.LOGINACCOUNT + "%" + uid + "%" + pass;
		try {
			// 将用户名和密码封装传给服务端
			Socket s = new Socket(InetAddress.getLocalHost(), 8000);
			sc = new SocketClient(s);
			sc.getPrintWriter().println(token);

			// 从服务端获取校验结果
			String result = sc.getBufferedReader().readLine();
			System.out.println("result = " + result);
			if (result.equalsIgnoreCase("true")) {
				sc.setUid(uid);
				loginSuccess();
			} else {
				JOptionPane.showMessageDialog(this, "账号或密码有误，请重新输入");
			}
		} catch (ConnectException ce) {
			System.out.println("请确保服务端已打开");
			JOptionPane.showMessageDialog(this, "链接异常，请确保服务端启动");
		} catch (SocketException se) {
			System.out.println("传入的用户名或密码为空");
			JOptionPane.showMessageDialog(this, "传入的用户名或密码为空");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loginSuccess() {
		frame.remove(panLogin);
		frame.remove(panButton);
		frame.add(new QQPrivateChat(sc));
	}

}
