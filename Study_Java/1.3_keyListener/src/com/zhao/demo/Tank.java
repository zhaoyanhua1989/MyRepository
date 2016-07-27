package com.zhao.demo;

public class Tank {

	protected int x;
	protected int y;
	protected int width;
	protected int high;
	
	public Tank(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.high = h;
	}
	
	public void removeAddX(int n) {
		x += n;
	}
	
	public void removeAddY(int n){
		y += n;
	}
	
	/**
	 * 判断子弹自否打中坦克
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isInTheRange(int x, int y){
		for(int i=0;i<width;i++){
			if(x == this.x+i){
				for(int j=0;j<high;j++){
					if(y == this.y+i) {
						System.out.println("在范围内，point = (" +x+ ","+y+ ")");
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
