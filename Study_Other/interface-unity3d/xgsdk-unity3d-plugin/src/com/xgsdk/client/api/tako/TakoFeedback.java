package com.xgsdk.client.api.tako;

import com.kingsoft.xg.tako.XGTakoController;
import com.unity3d.player.UnityPlayer;

public class TakoFeedback {    
    public static void showTakoFeedback(String appid){
        XGTakoController.getInstance().showTakoFeedback(UnityPlayer.currentActivity, appid);
    }
}
