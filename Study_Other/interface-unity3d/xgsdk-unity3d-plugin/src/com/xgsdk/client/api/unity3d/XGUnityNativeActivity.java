
package com.xgsdk.client.api.unity3d;

import com.unity3d.player.UnityPlayerNativeActivity;
import com.xgsdk.client.api.XGSDK;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class XGUnityNativeActivity extends UnityPlayerNativeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XGSDK.getInstance().onCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        XGSDK.getInstance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        XGSDK.getInstance().onPause(this);
    }

    protected void onStart() {
        super.onStart();
        XGSDK.getInstance().onStart(this);
    }

    protected void onRestart() {
        super.onRestart();
        XGSDK.getInstance().onRestart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        XGSDK.getInstance().onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XGSDK.getInstance().onDestroy(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        XGSDK.getInstance().onNewIntent(this, intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XGSDK.getInstance().onActivityResult(this, requestCode, resultCode,
                data);
    }
    
    @Override
	public void onBackPressed() {
    	XGSDK.getInstance().onBackPressed(this);
		super.onBackPressed();
	}

    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		XGSDK.getInstance().onConfigurationChanged(this, newConfig);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		XGSDK.getInstance().onSaveInstanceState(this, outState);
	}
	
	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && KeyEvent.ACTION_DOWN == event.getAction()){
            backPressed();
            return true;
        }
        Log.i("xgsdk","XGUnityActivity dispatchKeyEvent is called");
        return super.dispatchKeyEvent(event);
    }
    
    public void backPressed(){
        Log.i("xgsdk","XGUnityActivity backPressed is called");
        XGSDKUnity3DWrapper.getInstance().exit("");
    }
}
