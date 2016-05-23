package com.zhao.demo;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class MyPanelTest extends JPanel implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private char[] c = new char[1000];
	private int size = 0; // 字符数组的实际字符数
	private int yIndex = 0;
	private int xIndex = 0;
	private int xCursor = 0; // 光标的位置

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		/****************** 记事本样式 **********************/
		// paintNote(g);

		/****************** 测试 **********************/
		doTest(g);
	}

	private void doTest(Graphics g) {
		for (int i = 0; i < size; i++) {
			g.drawString("" + c[i], 10 + 8 * i, 10);
		}
		g.drawLine(10 + 8 * xCursor, 0, 10 + 8 * xCursor, 10);
	}

	private void paintNote(Graphics g) {
		for (int i = 0; i < size; i++) {
			g.drawString("" + c[i], 10 + 8 * xIndex, 10 + 8 * yIndex);
			xIndex++;
			System.out.println("index=" + xIndex + ",size=" + size + ",y=" + yIndex);
			if (xIndex % 33 == 0) { //换行
				yIndex = (i + 1) / 33;
				xIndex = 0;
				System.out.println("index>33,index=" + xIndex + ",y=" + yIndex);
			}
			if (i == (size - 1)) { //结尾
				xIndex = 0;
				yIndex = 0;
				System.out.println("index被赋值0");
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z)
				|| (e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9)) {

			for (int i = size; i > xCursor; i--) {
				c[i] = c[i - 1];
			}
			c[xCursor] = e.getKeyChar();
			size++;
			xCursor++;
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if (xCursor > 0)
				xCursor--;
			break;
		case KeyEvent.VK_UP:
			break;
		case KeyEvent.VK_RIGHT:
			if (xCursor < size)
				xCursor++;
			break;
		case KeyEvent.VK_DOWN:
			break;
		case KeyEvent.VK_BACK_SPACE:
			for (int i = xCursor; i < size; i++) {
				c[i - 1] = c[i];
			}
			xCursor--;
			size--;
			break;
		case KeyEvent.VK_DELETE:
			for (int i = xCursor; i < size; i++) {
				c[i] = c[i + 1];
			}
			size--;
			break;
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
