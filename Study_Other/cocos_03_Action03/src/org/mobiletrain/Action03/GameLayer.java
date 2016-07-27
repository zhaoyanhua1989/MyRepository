package org.mobiletrain.Action03;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRepeat;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCTintBy;
import org.cocos2d.actions.interval.CCTintTo;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

public class GameLayer extends CCLayer{
	
	CCSprite sprite;
	
	public GameLayer(){
		sprite = CCSprite.sprite("player.png");
		this.addChild(sprite);
		CGPoint initPoint = CGPoint.ccp(100, 100);
		sprite.setPosition(initPoint);
		//CCFadeOut使精灵淡出
		CCFadeOut fadeOut = CCFadeOut.action(1);
		//CCFadeIn使精灵淡入
		CCFadeIn fadeIn = CCFadeIn.action(1);
		
		//ccColor3B，基于RGB定义的颜色
		//ccColor4B，基于ARGB定义的颜色，A是透明度
		ccColor3B color3b = ccColor3B.ccc3(-12, -100, 10);
		//CCTintTo能使精灵颜色变化到指定值
		CCTintTo tintTo = CCTintTo.action(1, color3b);
		//CCTintBy使精灵在原来颜色RGB值得基础上变化(加上)指定的RGB值
		CCTintBy tintBy = CCTintBy.action(1, color3b);
		
		CCSequence sequence = CCSequence.actions(fadeOut, fadeIn, tintBy);
		
		CGPoint targetPoint = CGPoint.ccp(400, 300);
		CCMoveTo moveTo1 = CCMoveTo.action(2, targetPoint);
		CCMoveTo moveTo2 = CCMoveTo.action(2, initPoint);
		CCSequence sequence2 = CCSequence.actions(moveTo1, moveTo2);
		//CCRepeat用于动作重复的次数
		CCRepeat repeat = CCRepeat.action(sequence2, 2);
		//CCRepeatF
		CCRepeatForever forever = CCRepeatForever.action(sequence2);
		
		sprite.runAction(forever);
	}
}
