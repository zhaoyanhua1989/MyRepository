package com.example.test.util;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.example.test.view.MyTopRelativeLayout;

/**
 * 动画效果显示顶部消息，自动退出
 * 要求：minSdkVersion大于3(屏幕密度算法不一样)
 * @author HKW2962
 */
public class MessageUtil {

	public static void showFloatingMessage(Context context, String paramString) {
		RelativeLayout localRelativeLayout = new RelativeLayout(context);
		MyTopRelativeLayout localaz = new MyTopRelativeLayout(context);
		localaz.showAnimatedMsg(paramString);
		ViewGroup localViewGroup = (ViewGroup) ((Activity) context).findViewById(android.R.id.content);
		localRelativeLayout.addView(localaz);
		localViewGroup.addView(localRelativeLayout);
	}
}