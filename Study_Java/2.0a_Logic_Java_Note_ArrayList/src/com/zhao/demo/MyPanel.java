package com.zhao.demo;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * java ArrayList
 * 
 * @author HKW2962
 *
 */
public class MyPanel extends JPanel implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList al = new ArrayList();
	private int cursor = 0;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int x = 0;
		for (int i = 0; i < al.size(); i++) {
			g.drawString("" + al.get(i), 10 + 8 * x, 10);
			x++;
		}
		g.drawLine(10 + 8 * cursor, 0, 10 + 8 * cursor, 10);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9)
				|| (e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z)) {
			al.add(cursor, e.getKeyChar());
			cursor++;
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if (cursor > 0)
				cursor--;
			break;
		case KeyEvent.VK_UP:

			break;
		case KeyEvent.VK_RIGHT:
			if (cursor < al.size())
				cursor++;
			break;
		case KeyEvent.VK_DOWN:

			break;
		case KeyEvent.VK_BACK_SPACE:
			if (cursor == 0)
				break;
			al.remove(cursor - 1);
			cursor--;
			break;
		case KeyEvent.VK_DELETE:
			if (cursor == al.size())
				break;
			al.remove(cursor);
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
