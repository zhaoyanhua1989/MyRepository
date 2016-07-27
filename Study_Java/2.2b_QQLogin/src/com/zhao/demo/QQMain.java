package com.zhao.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.zhao.demo.utils.SocketClient;

/**
 * QQ主面板
 * @author HKW2962
 *
 */
public class QQMain extends JPanel implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton button = new JButton();
	private JLabel label = new JLabel(" 或许");
	private JFrame frame;
	private JPanel panContacts;
	private SocketClient sc;
	
	public QQMain(SocketClient sc) {
		this.sc = sc;
		frame = QQLogin.getFrameInstance();
		frame.setSize(300, 600);
		showContent();
		frame.setVisible(true);
	}

	private void showContent() {
		int screenX = Toolkit.getDefaultToolkit().getScreenSize().width;
		int frameX = frame.getWidth();
		System.out.println("screenX=" + screenX);
		System.out.println("frameX=" + frameX);
		frame.setLocation(screenX - frameX, 0);
		
		panContacts = new JPanel();
		panContacts.setLayout(new GridLayout(1, 3));
		button.setBackground(Color.blue);
		panContacts.add(button);
		panContacts.add(label);
		
		panContacts.addMouseListener(this);
		
		frame.add(panContacts, BorderLayout.NORTH);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		frame.remove(panContacts);
		frame.add(new QQPrivateChat(sc));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
