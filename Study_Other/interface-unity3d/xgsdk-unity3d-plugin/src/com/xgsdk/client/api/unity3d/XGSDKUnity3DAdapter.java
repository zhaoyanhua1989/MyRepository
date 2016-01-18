package com.xgsdk.client.api.unity3d;

import android.app.Activity;

import com.unity3d.player.UnityPlayer;

public class XGSDKUnity3DAdapter implements IUnity3DAdapter{

    @Override
	public void sendMessage(String functionName, String params) {
		UnityPlayer.UnitySendMessage("XGSDKCallbackWrapper", functionName, params);
	}

    @Override
	public Activity getCurrentActivity() {
		return UnityPlayer.currentActivity;
	}
}
