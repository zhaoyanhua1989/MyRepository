package com.example.test.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 重写的RelativeLayout，用来显示顶部弹窗
 * @author HKW2962
 */
public class MyTopRelativeLayout extends RelativeLayout {

	public Context context;
	private TextView textview;

	public MyTopRelativeLayout(Context context) {
		super(context);
		this.context = context;
		BaseMethod.setContext(context);
		// 设置this的边距，左上右下
		setPadding(BaseMethod.getScaleSize(10), BaseMethod.getScaleSize(10), 0, 0);
		// 设置this的背景颜色，透明度可以调节最后一个参数
		setBackgroundColor(BaseMethod.colorWithAlpha(0, 0, 0, 0.7F));
		// 设置this的Layout参数，-1会自动盖满宽/高
		RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, BaseMethod.getScaleSize(40));
		// 将this的位置设置为顶部之上位置，以便开始动画
		localLayoutParams.topMargin = (-BaseMethod.getScaleSize(40));
		setLayoutParams(localLayoutParams);

		// 创建弹窗小图片
		ImageView localImageView = new ImageView(context);
		localImageView.setImageResource(BaseMethod.getDrawable("icon_top"));
		RelativeLayout.LayoutParams imageLayoutParams = new RelativeLayout.LayoutParams(BaseMethod.getScaleSize(20),
				BaseMethod.getScaleSize(20));
		localImageView.setLayoutParams(imageLayoutParams);
		// this添加图片
		addView(localImageView);

		// 创建textView
		textview = new TextView(context);
		textview.setTextColor(-1);
		RelativeLayout.LayoutParams textviewLayoutParams = new RelativeLayout.LayoutParams(BaseMethod.getScreenSize().x
				- BaseMethod.getScaleSize(40), BaseMethod.getScaleSize(20));
		textviewLayoutParams.leftMargin = BaseMethod.getScaleSize(40);
		textview.setLayoutParams(textviewLayoutParams);
		// this添加textView
		addView(textview);
	}

	public void showAnimatedMsg(String paramString) {
		textview.setText(paramString);
		// 显示顶部顶部消息
		showMsg(BaseMethod.getScaleSize(40), 300);
		// 使顶部消息消失
		postDelayed(new RemoveViewRunnable(this), 3000L);
	}

	public void showMsg(int n, int millisecond) {
		TranslateAnimation showMsgAnimation = new TranslateAnimation(0.0F, 0.0F, 0.0F, n);
		// 设置动画的持续时间，这里要传入long类型，自动装箱
		showMsgAnimation.setDuration(millisecond);
		// 当动画结束时，保持动画的位置
		showMsgAnimation.setFillAfter(true);
		// 设置动画监听(重写动画开始、重复、结束时逻辑)
		// 这里在动画结束时，将显示的view位置参数改变，不然上述setFillAfter无法在动画结束后定格view(view会跳回原始位置)。
		showMsgAnimation.setAnimationListener(new MyAnimationListener(this, n));
		/* 开始执行显示顶部消息的动画 */
		startAnimation(showMsgAnimation);
	}
}

class BaseMethod {

	private static Context mContext;
	private static float d;
	private static Point mPoint;

	public static void setContext(Context context) {
		mContext = context;
	}

	public static int getScaleSize(int paramInt) {
		int n = (int) (paramInt * getScale());
		return n;
	}

	// 获取像素密度(这个值一般是1或者2，也有其他小数值)
	private static float getScale() {
		if (d == 0.0F)
			d = mContext.getResources().getDisplayMetrics().density;
		return d;
	}

	public static int getDrawable(String paramString) {
		return mContext.getResources().getIdentifier(paramString, "drawable", mContext.getPackageName());
	}

	public static int colorWithAlpha(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
		int i1 = Color.argb((int) (255.0F * paramFloat), paramInt1, paramInt2, paramInt3);
		return i1;
	}

	// 获取屏幕尺寸要分辨版本
	public static Point getScreenSize() {
		WindowManager localWindowManager = (WindowManager) mContext.getSystemService("window");
		Display localDisplay = localWindowManager.getDefaultDisplay();
		mPoint = new Point();
		if (getAPIVersion() >= 13)
			localDisplay.getSize(mPoint);
		else
			mPoint.set(localDisplay.getWidth(), localDisplay.getHeight());
		return mPoint;
	}

	// 获取build的版本号
	public static int getAPIVersion() {
		return Build.VERSION.SDK_INT;
	}
}

class RemoveViewRunnable implements Runnable {

	public MyTopRelativeLayout mMrl;

	RemoveViewRunnable(MyTopRelativeLayout mrl) {
		mMrl = mrl;
	}

	public void run() {
		ViewGroup localViewGroup1 = (ViewGroup) mMrl.getParent();
		ViewGroup localViewGroup2 = (ViewGroup) localViewGroup1.getParent();
		localViewGroup2.removeView(localViewGroup1);
	}
}

class MyAnimationListener implements Animation.AnimationListener {

	private ViewGroup localObject;
	private MyTopRelativeLayout mMrl;
	private int paramInt;

	MyAnimationListener(MyTopRelativeLayout mrl, int n) {
		mMrl = mrl;
		paramInt = n;
	}

	// 当动画开始时
	public void onAnimationStart(Animation paramAnimation) {
	}

	// 当动画重新开始时
	public void onAnimationRepeat(Animation paramAnimation) {
	}

	public void onAnimationEnd(Animation paramAnimation) {
		// 移除当前view的所有动画
		mMrl.clearAnimation();
		try {
			RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams) mMrl.getLayoutParams();
			localLayoutParams.topMargin += paramInt;
			mMrl.setLayoutParams(localLayoutParams);
			localObject = (ViewGroup) mMrl.getParent();
			((ViewGroup) localObject).updateViewLayout(mMrl, localLayoutParams);
		} catch (Exception localException) {
			Object localObject = (FrameLayout.LayoutParams) mMrl.getLayoutParams();
			((FrameLayout.LayoutParams) localObject).topMargin += paramInt;
			mMrl.setLayoutParams((ViewGroup.LayoutParams) localObject);
			ViewGroup localViewGroup = (ViewGroup) mMrl.getParent();
			localViewGroup.updateViewLayout(mMrl, (ViewGroup.LayoutParams) localObject);
		}
	}
}
