package com.zhao.demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * Java规定，所有移动的都是MouseMotionListener，所有静止的都是MouseListener;
 * 
 * @author HKW2962
 */
public class MyPanel extends JPanel implements MouseMotionListener,
		MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x = 200;
	private int y = 300;
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		g.fillOval(x, y, 30, 30);
	}

	// ///////////////分隔线，mouseMotionListener////////////////////////

	@Override
	public void mouseDragged(MouseEvent e) {
		// 鼠标拖动
		x = e.getX();
		y = e.getY();
		System.out.println("鼠标拖动到坐标：(" + x + "," + y + ")");
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// ///////////////分隔线，mouseListener////////////////////////

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		// 鼠标点击
		x = e.getX();
		y = e.getY();
		System.out.println("鼠标点击坐标：(" + x + "," + y + ")");
		repaint();
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
