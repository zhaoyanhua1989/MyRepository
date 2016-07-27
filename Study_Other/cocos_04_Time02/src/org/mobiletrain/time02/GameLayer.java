package org.mobiletrain.time02;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;

import android.view.MotionEvent;

public class GameLayer extends CCLayer{

	CCSprite sprite;
	
	public GameLayer(){
		sprite = CCSprite.sprite("player.png");
		this.addChild(sprite);
		sprite.setPosition(100, 100);
		this.setIsTouchEnabled(true);
		//每个1秒钟调用一次fun函数
		this.schedule("fun", 1);
	}
	
	//delta往往用来表示增量，在当前的例子当中，该变量的值为上次调用fun函数与这次调用fun函数之间的时间差
	public void fun(float delta){
		System.out.println("调用了fun函数， delta的值为：" + delta);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		this.unschedule("fun");
		return super.ccTouchesBegan(event);
	}
	
}
