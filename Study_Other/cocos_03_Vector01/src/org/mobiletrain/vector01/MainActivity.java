package org.mobiletrain.vector01;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	//Cocos2d会把图形绘制在该view对象上
	CCGLSurfaceView view = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new CCGLSurfaceView(this);
		setContentView(view);
		
		//创建导演对象
		CCDirector director = CCDirector.sharedDirector();
		//设置游戏使用的view对象
		director.attachInView(view);
		//设置游戏是否显示帧数
		director.setDisplayFPS(true);
		//设置每一帧数所显示的时间
		director.setAnimationInterval(1 / 60.0);
		
		//创建场景对象
		CCScene scene = CCScene.node();
		//创建一个布景层对象
		GameLayer gameLayer = new GameLayer();
		scene.addChild(gameLayer);
		director.runWithScene(scene);
	}
}
