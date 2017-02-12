package com.example.test.activity;

import java.io.File;

import android.R.anim;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

import com.example.test.R;
import com.example.test.base.BaseActivity;
import com.example.test.model.AlertDialogService;
import com.example.test.model.LanguageConventService;
import com.example.test.util.MessageUtil;
import com.example.test.util.MyLog;
import com.example.test.util.OverallVariable;
import com.example.test.util.ToastUtil;

public class MainActivity extends BaseActivity {

	private Button buttonsBT1, buttonInitial, buttonsBT2, buttonsBT3, buttonsBT4, buttonsBT5, buttonsBT6;
	private Animation animationTranslate, animationRotate, animationScale;
	private static int width, height; // 悬浮按钮的宽高
	private LayoutParams params = new LayoutParams(0, 0);
	private static Boolean isClick = false;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OverallVariable.Update.DO_UPDATE:
				Bundle bundle = msg.getData();
				AlertDialogService.showUpdateVersionDialog(MainActivity.this, OverallVariable.Update.DO_UPDATE, bundle);
				break;
			case OverallVariable.Update.UNDO_UPDATE:
				AlertDialogService.showUpdateVersionDialog(MainActivity.this, OverallVariable.Update.UNDO_UPDATE, null);
				break;
			case OverallVariable.Update.INSTALLAPK:
				Bundle bundle2 = msg.getData();
				String path = bundle2.getString("path");
				installApk(path);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/* 隐藏标题栏，隐藏后看不到右上角的menu */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_activity_main);
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.main_button1:
			startActivity(new Intent(this, GetRAndImeiActivity.class));
			break;
		case R.id.main_button2:
			startActivity(new Intent(this, DirTestActivity.class));
			break;
		case R.id.main_button3:
			MessageUtil.showFloatingMessage(this, "测试顶部消息");
			break;
		case R.id.main_button4:
			// 汉字转拼音实现类似于手机通讯录首字母搜索(导航条)
			// 方法一，工具类：
			// String pinyin = LanguageConvent.getPinYin("本来");
			// 方法二，pinyin4j-2.5.0.jar：
			String pinyin = LanguageConventService.cn2Spell("本来");
			MessageUtil.showFloatingMessage(this, "汉字转拼音：\"本来\"->" + pinyin);
			break;
		case R.id.main_button5:
			// 测试自定义AlertDialog
			showDialogs(v);
			break;
		case R.id.main_button6:
			// 测试ListView和ViewPager
			startActivity(new Intent(this, ViewPagerActivity.class));
			break;
		case R.id.main_button7:
			// 测试notifycation和拉起另一个应用
			startActivity(new Intent(this, NotificationActivity.class));
			break;
		case R.id.main_button8:
			// 测试侧滑菜单
			startActivity(new Intent(this, SideslipActivity.class));
			break;
		case R.id.main_button9:
			// 测试登录
			startActivity(new Intent(this, LoginActivity.class));
			break;
		case R.id.main_button10:
			// 测试webView
			startActivity(new Intent(this, WebViewActivity.class));
			break;
		}
	}

	// 显示Popup弹窗，展示自定义view
	private void showDialogs(View v) {
		// 显示PopupMenu(不能设置background)
		// AlertDialogService.showPopupMenu(this, v);
		// 显示PopupWindow(可以设置background)
		AlertDialogService.showPopupWindow(this, handler, v);
	}

	@Override
	public void onBackPressed() {
		// 自定义退出弹窗1
		AlertDialogService.showSimpleCustomExitDialog(MainActivity.this);
	}

	/*************************** Activity右上角menu *****************************/
	// 重写菜单，显示menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_main_activity_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// 监听menu点击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete) {
			ToastUtil.showCustomToast(this, "menu点击事件");
		}
		return super.onOptionsItemSelected(item);
	}

	protected void installApk(String path) {
		// 用代码安装apk
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri data = Uri.fromFile(new File(path));
		// type:表示的是文件 类型, mime:定义文件类型 text/html ,text/xml
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(data, type);
		startActivity(intent);
	}

	@Override
	protected void initVariables() {

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initViews(Bundle savedInstanceState) {
		isClick = false;
		Display display = getWindowManager().getDefaultDisplay();
		height = display.getHeight();
		width = display.getWidth();
		MyLog.d("width  & height is:" + String.valueOf(width) + ", " + String.valueOf(height));

		params.height = 125;
		params.width = 125;
		// 设置边距 (int left, int top, int right, int bottom)
		params.setMargins(10, height - 170, 0, 0);

		// 最开始的按钮
		buttonInitial = (Button) findViewById(R.id.my_activity_main_buttonsBTInitial);
		buttonInitial.setLayoutParams(params);

		// 其他按钮
		buttonsBT6 = (Button) findViewById(R.id.my_activity_main_buttonsBT6);
		buttonsBT6.setLayoutParams(params);

		buttonsBT5 = (Button) findViewById(R.id.my_activity_main_buttonsBT5);
		buttonsBT5.setLayoutParams(params);

		buttonsBT4 = (Button) findViewById(R.id.my_activity_main_buttonsBT4);
		buttonsBT4.setLayoutParams(params);

		buttonsBT3 = (Button) findViewById(R.id.my_activity_main_buttonsBT3);
		buttonsBT3.setLayoutParams(params);

		buttonsBT2 = (Button) findViewById(R.id.my_activity_main_buttonsBT2);
		buttonsBT2.setLayoutParams(params);

		buttonsBT1 = (Button) findViewById(R.id.my_activity_main_buttonsBT1);
		buttonsBT1.setLayoutParams(params);

		buttonInitial.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyLog.d("left=" + buttonInitial.getLeft() + ", top=" + buttonInitial.getTop());
				// TODO 每次点击时button都会往下移，原因未知，这里限制向下移动（分辨率不同偏移量不同）
				setButtonLayoutParams(buttonInitial, buttonInitial.getLeft(), buttonInitial.getTop() - 30);
				ToastUtil.showCustomToast(MainActivity.this, "buttonInitial 被点击了...");
				if (isClick == false) {
					isClick = true;
					buttonInitial.startAnimation(animRotate(-45.0f, 0.5f, 0.5f));
					buttonsBT1.startAnimation(animTranslate(0.0f, -300.0f, buttonInitial.getLeft(), buttonInitial.getTop() - 430, buttonsBT1, 80, false));
					buttonsBT2.startAnimation(animTranslate(80.0f, -290.0f, buttonInitial.getLeft() + 120, buttonInitial.getTop() - 410, buttonsBT2, 100, false));
					buttonsBT3.startAnimation(animTranslate(155.0f, -265.0f, buttonInitial.getLeft() + 235, buttonInitial.getTop() - 355, buttonsBT3, 120, false));
					buttonsBT4.startAnimation(animTranslate(245.0f, -175.0f, buttonInitial.getLeft() + 325, buttonInitial.getTop() - 260, buttonsBT4, 140, false));
					buttonsBT5.startAnimation(animTranslate(290.0f, -120.0f, buttonInitial.getLeft() + 380, buttonInitial.getTop() - 145, buttonsBT5, 160, false));
					buttonsBT6.startAnimation(animTranslate(300.0f, -0.0f, buttonInitial.getLeft() + 400, buttonInitial.getTop() - 20, buttonsBT6, 180, false));
				} else {
					isClick = false;
					buttonInitial.startAnimation(animRotate(45.0f, 0.5f, 0.5f));
					buttonsBT1.startAnimation(animTranslate(0.0f, 300.0f, buttonInitial.getLeft(), buttonInitial.getTop() - 20, buttonsBT1, 180, true));
					buttonsBT2.startAnimation(animTranslate(-80.0f, 290.0f, buttonInitial.getLeft(), buttonInitial.getTop() - 20, buttonsBT2, 160, true));
					buttonsBT3.startAnimation(animTranslate(-155.0f, 265.0f, buttonInitial.getLeft(), buttonInitial.getTop() - 20, buttonsBT3, 140, true));
					buttonsBT4.startAnimation(animTranslate(-245.0f, 175.0f, buttonInitial.getLeft(), buttonInitial.getTop() - 20, buttonsBT4, 120, true));
					buttonsBT5.startAnimation(animTranslate(-280.0f, 120.0f, buttonInitial.getLeft(), buttonInitial.getTop() - 20, buttonsBT5, 100, true));
					buttonsBT6.startAnimation(animTranslate(-0.0f, 0.0f, buttonInitial.getLeft(), buttonInitial.getTop() - 20, buttonsBT6, 100, true));
				}
			}
		});
		buttonInitial.setOnTouchListener(new OnTouchListener() {

			// 悬浮按钮移动相关
			private int x1, y1; // 当按下手指时的坐标
			private int x2, y2; // 当移动手指时的坐标
			private float pointX, pointY;
			private boolean isMovedFlag = false;

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 手指落下的起始位置
					x1 = (int) event.getRawX();
					y1 = (int) event.getRawY();
					pointX = buttonInitial.getX();
					pointY = buttonInitial.getY();
					MyLog.d("初始坐标：(" + pointX + ", " + pointY + ")");
					isMovedFlag = false;
					break;
				case MotionEvent.ACTION_MOVE:
					x2 = (int) event.getRawX();
					y2 = (int) event.getRawY();
					if (Math.abs(y1 - y2) > 50 || Math.abs(x1 - x2) > 50) {
						// 计算出手指在x和y的偏移量
						int dex = x2 - x1;
						int dey = y2 - y1;
						// 实时计算出button的上下左右位置，相对于父元素
						int l = v.getLeft();
						int t = v.getTop();
						int r = v.getRight();
						int b = v.getBottom();
						// button在手指移动后应该在的新位置
						int newl = l + dex;
						int newr = r + dex;
						int newt = t + dey;
						int newb = b + dey;
						// 将button布局到新位置
						v.layout(newl, newt, newr, newb);
						v.postInvalidate();
						// 重置手指的起始位置
						x1 = (int) event.getRawX();
						y1 = (int) event.getRawY();
					}
				case MotionEvent.ACTION_UP:
					if (pointX != buttonInitial.getX() || pointY != buttonInitial.getY()) {
						MyLog.d("改变后坐标：(" + buttonInitial.getX() + ", " + buttonInitial.getY() + ")");
						pointX = buttonInitial.getX();
						pointY = buttonInitial.getY();
						isMovedFlag = true;
					}
				}
				return isMovedFlag;
			}
		});
		buttonsBT1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtil.showCustomToast(MainActivity.this, "button1 被点击了...");
				buttonsBT1.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonsBT2.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT3.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT4.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT5.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT6.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonInitial.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonsBT2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtil.showCustomToast(MainActivity.this, "button2 被点击了...");
				buttonsBT2.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonsBT1.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT3.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT4.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT5.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT6.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonInitial.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonsBT3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtil.showCustomToast(MainActivity.this, "button3 被点击了...");
				buttonsBT3.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonsBT2.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT1.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT4.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT5.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT6.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonInitial.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonsBT4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtil.showCustomToast(MainActivity.this, "button4 被点击了...");
				buttonsBT4.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonsBT3.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT2.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT1.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT5.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT6.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonInitial.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonsBT5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtil.showCustomToast(MainActivity.this, "button5 被点击了...");
				buttonsBT5.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonsBT3.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT2.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT1.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT4.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT6.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonInitial.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
		buttonsBT6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtil.showCustomToast(MainActivity.this, "button6 被点击了...");
				buttonsBT6.startAnimation(setAnimScale(2.5f, 2.5f));
				buttonsBT3.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT2.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT1.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT4.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonsBT5.startAnimation(setAnimScale(0.0f, 0.0f));
				buttonInitial.startAnimation(setAnimScale(0.0f, 0.0f));
			}
		});
	}

	private void setButtonLayoutParams(Button button, int left, int top) {
		params.height = 125;
		params.width = 125;
		params.setMargins(left, top, 0, 0);
		button.setLayoutParams(params);
	}

	@Override
	protected void loadData() {

	}

	/**
	 * 缩放动画
	 * 
	 * @param toX
	 *            动画结束时 X坐标上的伸缩尺寸
	 * @param toY
	 *            动画结束时Y坐标上的伸缩尺寸
	 * @return Animation
	 */
	protected Animation setAnimScale(float toX, float toY) {
		/*
		 * fromX 动画起始时 X坐标上的伸缩尺寸 
		 * toX 动画结束时 X坐标上的伸缩尺寸 
		 * fromY 动画起始时Y坐标上的伸缩尺寸 
		 * toY 动画结束时Y坐标上的伸缩尺寸 
		 * pivotXType 动画在X轴相对于物件位置类型 
		 * pivotXValue 动画相对于物件的X坐标的开始位置 
		 * pivotYType 动画在Y轴相对于物件位置类型 
		 * pivotYValue 动画相对于物件的Y坐标的开始位置 
		 */
		animationScale = new ScaleAnimation(1f, toX, 1f, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.45f);
		animationScale.setInterpolator(MainActivity.this, anim.accelerate_decelerate_interpolator);
		animationScale.setDuration(2000);
		animationScale.setFillAfter(false);
		return animationScale;
	}

	/**
	 * 转动动画
	 * 
	 * @param toDegrees
	 *            旋转结束的角度
	 * @param pivotXValue
	 *            X轴起始点值
	 * @param pivotYValue
	 *            Y轴起始点值
	 * @return Animation
	 */
	protected Animation animRotate(float toDegrees, float pivotXValue, float pivotYValue) {
		/*
		 * fromDegrees：旋转的开始角度
		 * toDegrees：旋转的结束角度
		 * pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT
		 * pivotXValue：X轴起始点值
		 * pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT
		 * pivotYValue：Y轴起始点值
		 */
		animationRotate = new RotateAnimation(0, toDegrees, Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, pivotYValue);
		animationRotate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				animationRotate.setFillAfter(true);
			}
		});
		return animationRotate;
	}

	/**
	 * 平移动画
	 * 
	 * @param toX
	 *            这个参数表示动画结束的点离当前View X坐标上的差值
	 * @param toY
	 *            这个参数表示动画结束的点离当前View Y坐标上的差值
	 * @param lastX
	 *            最终button移动到的X坐标值
	 * @param lastY
	 *            最终button移动到的Y坐标值
	 * @param button
	 * @param durationMillis
	 *            动画执行的时间
	 * @param isHide
	 *            是否隐藏执行动画的组件
	 * @return Animation
	 */
	protected Animation animTranslate(float toX, float toY, final int lastX, final int lastY, final Button button, long durationMillis, final boolean isHide) {
		button.setVisibility(View.VISIBLE);
		if (!isHide) {
			// 当主要悬浮球移动时，重制其他悬浮球位置
			setButtonLayoutParams(button, buttonInitial.getLeft(), buttonInitial.getTop() - 20);
		}
		/*
		 * TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)   
		 * float fromXDelta:这个参数表示动画开始的点离当前View X坐标上的差值；
		 * float toXDelta, 这个参数表示动画结束的点离当前View X坐标上的差值；
		 * float fromYDelta, 这个参数表示动画开始的点离当前View Y坐标上的差值；
		 * float toYDelta)这个参数表示动画开始的点离当前View Y坐标上的差值；
		 */
		animationTranslate = new TranslateAnimation(0, toX, 0, toY);
		animationTranslate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				params = new LayoutParams(0, 0);
				setButtonLayoutParams(button, lastX, lastY);
				button.clearAnimation();
				if (isHide) {
					button.setVisibility(View.GONE);
				}
			}
		});
		animationTranslate.setDuration(durationMillis);
		return animationTranslate;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		initViews(null);
	}
}