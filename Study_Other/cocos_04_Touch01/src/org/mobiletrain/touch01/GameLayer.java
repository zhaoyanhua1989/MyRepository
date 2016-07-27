package org.mobiletrain.touch01;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

import android.view.MotionEvent;

public class GameLayer extends CCLayer{

	CCSprite sprite;
	
	public GameLayer(){
		sprite = CCSprite.sprite("player.png");
		this.addChild(sprite);
		sprite.setPosition(100, 100);
		//接受用户的触摸事件，首先必须对当前图层进行设置
		//设置当前图层是否接收触摸事件
		this.setIsTouchEnabled(true);
		
	}

	//当用户开始触摸屏幕时，执行该方法
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		CGPoint p1 = CGPoint.ccp(x, y);
		//convertToGL可以将左上角为原点的坐标转换为左下角为原点的坐标
		CGPoint p2 = CCDirector.sharedDirector().convertToGL(p1);
		sprite.setPosition(p2);
		System.out.println("began");
		System.out.println("p1.x:" + p1.x + ",p1.y:" + p1.y);
		System.out.println("p2.x:" + p2.x + ",p2.y:" + p2.y);
		return super.ccTouchesBegan(event);
	}
	
	//当用户的手指离开屏幕时，执行该方法
	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		System.out.println("ended");
		return super.ccTouchesEnded(event);
	}

	//当用户的手指在屏幕上移动时，执行该方法
	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		System.out.println("moved");
		return super.ccTouchesMoved(event);
	}
	
	
	
}
