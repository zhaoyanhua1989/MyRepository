package org.mobiletrain.fristgame;

import org.cocos2d.actions.interval.CCJumpTo;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

public class GameLayer extends CCLayer{
	
	//声明一个精灵对象
	CCSprite player;
	
	public GameLayer(){
		//初始化精灵对象
		player = CCSprite.sprite("player.png");
		
		//CGPoint对象通常用于表示坐标，或者是表示向量
		CGPoint point = CGPoint.ccp(100, 100);
		//设置精灵对象的位置
		player.setPosition(point);	//也可以在setPosition中直接填写坐标
		
		//将精灵对象添加至布景层当中
		this.addChild(player);
		
		CGPoint target = CGPoint.ccp(400, 100);
		//生成一个CCJumpTo对象，该对象表示一个跳跃的动作
		CCJumpTo jumpTo = CCJumpTo.action(3, target, 200, 50);//跳跃的时间(秒),目标点,每次跳跃高度,跳跃次数
		//使用精灵对象执行该动作
		player.runAction(jumpTo);
		
	}
	
	
	
}
