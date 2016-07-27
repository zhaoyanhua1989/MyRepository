package com.zhao.demo;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import com.zhao.demo.biz.Node;

/**
 * 没有使用安卓的链表类，自己实现
 * @author HKW2962
 *
 */
public class MyPanelTest extends JPanel implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node nowNode = new Node();
	private Node firstNode = nowNode;
	private int cursor = 0;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int x = 0;
		// 遍历每个节点，声明临时变量是MyNode类型，这样才能指向每个节点
		Node temNode = firstNode;
		while (temNode.next != null) {
			System.out.println("temNode.next.value="+temNode.next.value);
			g.drawString(""+temNode.next.value, 10 + 8 * x, 10);
			x++;
			temNode = temNode.next;
		}
		g.drawLine(10 + 8 * cursor, 0, 10 + 8 * cursor, 10);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9)
				|| (e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z)) {
			if(nowNode.next == null){
				Node newNode = new Node();
				newNode.value = e.getKeyChar();
				nowNode.next = newNode;
				nowNode = newNode;
				cursor ++ ;
			} else {
				//当前节点不在末尾时插入新节点
				Node newNode = new Node();
				newNode.value = e.getKeyChar();
				newNode.next = nowNode.next;
				nowNode.next = newNode;
				//光标位置移动了，当前节点位置也要跟着变化
				nowNode = newNode;
				cursor ++ ;
			}
		}
		switch(e.getKeyCode()){
		case KeyEvent.VK_LEFT:	//移动光标。节点需要跟着移动，方便删除和增加
			if(cursor > 0){
				cursor -- ;
				//从第一个节点开始，找到当前节点的前一个节点
				Node tmpNode = firstNode;
				while(tmpNode.next != nowNode){
					tmpNode = tmpNode.next;
				}
				//当前节点定为找到的前一个节点
				nowNode = tmpNode;
			}
			break;
		case KeyEvent.VK_UP:
			
			break;
		case KeyEvent.VK_RIGHT:
			if(nowNode.next != null){ //光标右移判断的条件
				cursor ++ ;
				nowNode = nowNode.next;
			}
			break;
		case KeyEvent.VK_DOWN:
			
			break;
		case KeyEvent.VK_BACK_SPACE: //删除光标做左边的链接
			//从第一个节点开始，找到当前节点的前一个节点
			Node tmpNode = firstNode;
			if(tmpNode.next == null) return;
			while(tmpNode.next != nowNode){
				tmpNode = tmpNode.next;
			}
			//把当前节点的的前一个节点的next链接指向当前节点的next，去掉中间节点
			tmpNode.next = nowNode.next;
			//把修改了表链接的节点赋值给当前节点，然后repaint时重新刷新，就去掉了中间的节点
			nowNode = tmpNode;
			cursor -- ;
			break;
		case KeyEvent.VK_DELETE: //删除光标右边的节点
			//把当前节点的next链接指向next的next，去掉中间的next
			if(nowNode.next != null){
				nowNode.next = nowNode.next.next;
			}
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
