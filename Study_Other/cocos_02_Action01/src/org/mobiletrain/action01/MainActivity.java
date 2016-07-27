package org.mobiletrain.action01;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	//Cocos2d引擎将会把图形绘制在该view对象上
	private CCGLSurfaceView view = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new CCGLSurfaceView(this);
		setContentView(view);
		
		//得到CCDirector对象
		CCDirector director = CCDirector.sharedDirector();
		//设置应用程序相关的属性
		//	设置当前游戏程序当中所使用的view对象
		director.attachInView(view);
		//	设置游戏是否显示FPS值
		director.setDisplayFPS(true);
		//	设置游戏渲染一帧数据所需要的时间
		director.setAnimationInterval(1 / 60.0);
		
		//生成一个游戏场景对象
		CCScene scene = CCScene.node();
		//生成布景层对象
		GameLayer gameLayer =  new GameLayer();
		//将布景层对象添加到游戏场景中
		scene.addChild(gameLayer);
		//运行游戏场景
		director.runWithScene(scene);
	}
	
}
