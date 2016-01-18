package com.xgsdk.client;

import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

import android.os.Bundle;

public class CoCosDemo extends XGCocos2dxActivity {

	static {
		System.loadLibrary("cocos2dcpp");
	}
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

    public Cocos2dxGLSurfaceView onCreateView() {
    	Cocos2dxGLSurfaceView glSurfaceView = new Cocos2dxGLSurfaceView(this);
    	glSurfaceView.setEGLConfigChooser(5, 6, 5, 0, 16, 8);
    	return glSurfaceView;
    }
    
    @Override
    protected void onStart(){
        super.onStart();
    }
	
}
