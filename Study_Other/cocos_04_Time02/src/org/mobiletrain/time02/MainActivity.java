package org.mobiletrain.time02;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	CCGLSurfaceView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new CCGLSurfaceView(this);
		setContentView(view);
		
		CCDirector director = CCDirector.sharedDirector();
		director.attachInView(view);
		director.setDisplayFPS(true);
		director.setAnimationInterval(1 / 60.0);
		
		CCScene scene = CCScene.node();
		GameLayer gameLayer = new GameLayer();
		scene.addChild(gameLayer);
		director.runWithScene(scene);
	}
}
