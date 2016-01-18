package org.mobiletrain.Action02;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

public class GameLayer extends CCLayer{

	CCSprite sprite;
	
	public GameLayer(){
		sprite = CCSprite.sprite("player.png");
		CGPoint initpoint = CGPoint.ccp(100, 100);
		this.addChild(sprite);
		sprite.setPosition(initpoint);
		
		CGPoint targetPoint = CGPoint.ccp(300, 300);
		CCMoveTo moveTo = CCMoveTo.action(2, targetPoint);
		
//		CCRotateTo ratateTo = CCRotateTo.action(2, 180);
		CCRotateBy rotateBy = CCRotateBy.action(2, 360*5);
		
		CCScaleTo scaleTo = CCScaleTo.action(2, 2);
		
		//Sequence让多个动作先后执行
//		CCSequence sequence = CCSequence.actions(moveTo, ratateTo, scaleTo);
		//Spawn让多个动作同时执行
		CCSpawn spawn = CCSpawn.actions(moveTo, rotateBy, scaleTo);
		CCCallFuncN func = CCCallFuncN.action(this, "onActionFinished");
		CCSequence sequence = CCSequence.actions(spawn, func);
		sprite.runAction(sequence);
	}
	
	public void onActionFinished(Object sender){
		System.out.println("onActionFinished");
	}
	
}
