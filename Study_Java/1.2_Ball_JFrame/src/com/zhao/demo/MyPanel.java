package com.zhao.demo;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MyPanel extends JPanel implements Runnable{/**
 * 
 */
	private static final long serialVersionUID = 1L;
	private BallObject[] balls;
	private Color[] ballsColors = new Color[]{Color.blue, Color.white, Color.green, Color.pink, 
			Color.cyan, Color.gray, Color.orange, Color.red, Color.yellow};;
	private int k = 9; //小球数量
	private int removeX = 8; //小球每次移动的x轴距离
	private int removeY = 8; //小球每次移动的y轴距离
	
	public MyPanel(){
		balls = new BallObject[k];
		for(int i=0; i<k ; i++){
			BallObject ball = new BallObject((int)(50+Math.random()*914), (int)(50+Math.random()*658));
			balls[i] = ball;
		}
		//设置小球方向
		for(int i=0; i<k ; i++){
			balls[i].setRemoveOrientation((int) (Math.random()*3));
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
//		g.drawOval(50, 50, 50, 50);
		for(int i=0; i < k ; i++){
			g.setColor(ballsColors[i]);
			g.fillOval(balls[i].x, balls[i].y, 50, 50);
		}
	}

	@Override
	public void run() {
		while(true){
			for(int i=0; i<k ; i++){
				if(balls[i].removeOrientation == 0){
					balls[i].x += removeX;
					balls[i].y += removeY;
				} else if(balls[i].removeOrientation == 1){
					balls[i].x += removeX;
					balls[i].y -= removeY;
				} else if(balls[i].removeOrientation == 2 ){
					balls[i].x -= removeX;
					balls[i].y -= removeY;
				} else if(balls[i].removeOrientation == 3 ){
					balls[i].x -= removeX;
					balls[i].y += removeY;
				}
			}
			for(int i=0; i<k ; i++){
				if(balls[i].x >= 924){
					if(balls[i].removeOrientation == 0){
						balls[i].removeOrientation = 3;
					} else balls[i].removeOrientation = 2;
				} else if (balls[i].y >= 668){
					if(balls[i].removeOrientation == 0){
						balls[i].removeOrientation = 1;
					} else balls[i].removeOrientation = 2;
				} else if (balls[i].y <= 0){
					if(balls[i].removeOrientation == 2){
						balls[i].removeOrientation = 3;
					} else balls[i].removeOrientation = 0;
				} else if (balls[i].x <= 0){
					if(balls[i].removeOrientation == 3){
						balls[i].removeOrientation = 0;
					} else balls[i].removeOrientation = 1;
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

}
