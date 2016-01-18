package org.mobiletrain.vector01;

import org.cocos2d.actions.interval.CCJumpBy;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

public class GameLayer extends CCLayer {
	
	CCSprite spriteA;
	CCSprite spriteB;
	
	public GameLayer(){
		spriteA = CCSprite.sprite("player.png");
		spriteB = CCSprite.sprite("player.png");
		this.addChild(spriteA);
		this.addChild(spriteB);
		
		CGPoint initPoint = CGPoint.ccp(100, 400);
		spriteA.setPosition(initPoint);
		spriteB.setPosition(initPoint);
		
		CGPoint pos = CGPoint.ccp(500, 0);
		CCJumpBy jumpBy = CCJumpBy.action(3, pos, 200, 5);
		spriteB.runAction(jumpBy);
//		CCMoveBy moveBy = CCMoveBy.action(3, pos);
//		spriteB.runAction(moveBy);
//		CGPoint detaPoint = CGPoint.ccp(0, 200);
//		//1.向量的加法
//		CGPoint targetPoint = CGPoint.ccpAdd(initPoint, detaPoint);
//		//2.向量的减法
//		CGPoint subPoint = CGPoint.ccpSub(initPoint, detaPoint);
//		//3.向量的乘法
//		CGPoint newPoint = CGPoint.ccpMult(initPoint, 1.5F);
//		//4.计算单位向量
//		CGPoint newPoint2 = CGPoint.ccpNormalize(initPoint);
//		spriteB.setPosition(newPoint2);
	}
	
}
