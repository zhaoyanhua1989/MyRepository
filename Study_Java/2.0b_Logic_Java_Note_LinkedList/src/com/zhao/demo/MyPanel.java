package com.zhao.demo;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import javax.swing.JPanel;

/**
 * java LinkedList
 * @author HKW2962
 *
 */
public class MyPanel extends JPanel implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedList ll = new LinkedList();
	private int cursor = 0;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int x = 0;
		for (int i = 0; i < ll.size(); i++) {
			g.drawString("" + ll.get(i), 10 + 8 * x, 10);
			x++;
		}
		g.drawLine(10 + cursor * 8, 0, 10 + cursor * 8, 10);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9)
				|| (e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z)) {
			ll.add(cursor, e.getKeyChar());
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
			if (cursor < ll.size())
				cursor++;
			break;
		case KeyEvent.VK_DOWN:

			break;
		case KeyEvent.VK_BACK_SPACE:
			if (cursor == 0)
				return;
			ll.remove(cursor - 1);
			cursor--;
			break;
		case KeyEvent.VK_DELETE:
			if (cursor == ll.size())
				return;
			ll.remove(cursor);
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
