package com.zhao.demo;

import java.awt.Point;

public class BallObject {

	protected int x;
	protected int y;
	protected int removeOrientation; //小球方向
	
	public BallObject(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setPoint(int x, int y){
		this.x += x;
		this.y += y;
	}
	
	public Point getPoint(){
		return new Point(x, y);
	}
	
	public void setRemoveOrientation(int att){
		this.removeOrientation = att;
	}
	
}
