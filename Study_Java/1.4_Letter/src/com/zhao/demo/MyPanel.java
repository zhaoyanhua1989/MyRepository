package com.zhao.demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class MyPanel extends JPanel implements Runnable, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int k = 10; // 规定字母数量
	private Letter[] letters;
	private int grade = 1000;

	public MyPanel() {
		this.letters = new Letter[k];
		for (int i = 0; i < k; i++) {
			letters[i] = new Letter((char) (Math.random() * 26 + 97),
					(int) (Math.random() * 300), -(int) (Math.random() * 400));
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.WHITE);
		g.drawString("你的成绩是：" + grade, 80, 15);
		for (int i = 0; i < k; i++) {
			if (!letters[i].isOverY(30)) {
				continue;
			}
			g.drawString(new Character(letters[i].l).toString(), letters[i].x,
					letters[i].y);
		}
	}

	@Override
	public void run() {
		while (true) {
			for (int i = 0; i < k; i++) {
				letters[i].removeY(2);
				if (letters[i].isOverY(400)) {
					letters[i] = new Letter((char) (Math.random() * 26 + 97),
							(int) (Math.random() * 300), 30);
					grade -= 100;
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		boolean mark = false;
		int indexValue = 30;	//设置标记
		int index = -1;		//设置标记
		for (int i = 0; i < k; i++) {
			if (letters[i].l == e.getKeyChar()) {
				if(letters[i].y > indexValue){
					indexValue = letters[i].y;
					index = i;
				}
			}
		}
		if (index != -1) {
			letters[index] = new Letter((char) (Math.random() * 26 + 97),
					(int) (Math.random() * 300), 30);
			grade += 10;
		}else{
			grade -= 10;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
