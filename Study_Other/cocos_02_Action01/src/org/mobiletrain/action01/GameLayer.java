package org.mobiletrain.action01;

import org.cocos2d.actions.instant.CCFlipX;
import org.cocos2d.actions.instant.CCFlipY;
import org.cocos2d.actions.instant.CCHide;
import org.cocos2d.actions.interval.CCBlink;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

public class GameLayer extends CCLayer{

	CCSprite spriteX;
	CCSprite spriteY;
	CCSprite sprite;
	
	public GameLayer(){
	//测试瞬时动作
		spriteX = CCSprite.sprite("android_player.png");
		this.addChild(spriteX);
		spriteX.setPosition(100,100);
		//1.生成动作对象
		CCFlipX flipX = CCFlipX.action(true);
		//2.使用精灵对象执行动作对象
		spriteX.runAction(flipX);
		
		spriteY = CCSprite.sprite("player.png");
		this.addChild(spriteY);
		spriteY.setPosition(300, 100);
		CCFlipY flipY = CCFlipY.action(true);
		spriteY.runAction(flipY);
		
		//测试隐藏精灵
		CCHide hide = CCHide.action();
		spriteY.runAction(hide);
		
	/////////////////////////////////////////////////////////////////////////
		
	//测试延迟动作	移动
		sprite = CCSprite.sprite("player.png");
		this.addChild(sprite);
		sprite.setPosition(200, 100);
		CGPoint point = CGPoint.ccp(600,  500);
		CCMoveTo moveTo = CCMoveTo.action(3, point);
		sprite.runAction(moveTo);
		
		//旋转
		CCSprite spriteRote = CCSprite.sprite("player.png");
		this.addChild(spriteRote);
		spriteRote.setPosition(1000, 100);
		CCRotateTo rotate = CCRotateTo.action(3, 180);
		spriteRote.runAction(rotate);
		
		//缩放
		CCSprite spriteScale = CCSprite.sprite("player.png");
		this.addChild(spriteScale);
		spriteScale.setPosition(200, 600);
		CCScaleTo scaleTo = CCScaleTo.action(3, 5, 2);
		spriteScale.runAction(scaleTo);
		
		//闪烁
		CCSprite spriteBlink = CCSprite.sprite("player.png");
		this.addChild(spriteBlink);
		spriteBlink.setPosition(1000, 600);
		CCBlink blink = CCBlink.action(3, 3);
		spriteBlink.runAction(blink);
	}
	
}

