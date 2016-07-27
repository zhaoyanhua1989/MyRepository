package com.zhao.demo;

public class Letter {

	protected char l;	//代表字符
	protected int x;	//代表x坐标
	protected int y;	//代表y坐标
	
	public Letter(char l, int x, int y){
		this.l = l;
		this.x = x;
		this.y = y;
	}
	
	public void removeX(int n){
		this.x += n;
	}
	
	public void removeY(int n){
		this.y += n;
	}
	
	public boolean isOverY(int n){
		if(this.y >= n){
			return true;
		}
		return false;
	}
	
}
