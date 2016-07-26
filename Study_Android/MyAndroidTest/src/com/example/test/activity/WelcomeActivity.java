package com.example.test.activity;

import com.example.test.model.CompressService;
import com.example.test.model.TextContentService;
import com.example.test.R;
import com.example.test.util.RUtil;
import com.example.test.view.MyTextView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class WelcomeActivity extends Activity {

	private boolean isHangUp; // 加载广告页面，判断此Activity是否被挂起
	private boolean done; // 加载下一个Activity是否已执行
	private Bitmap mBitMap; // 欢迎界面背景图片BitMap，用完需要释放

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		// 显示图片
		showContent();
		// 延迟进入下一个页面
		loadNext();
	}

	private void showContent() {
		ImageView image = (ImageView) findViewById(R.id.welcome_imageView);
		image.setScaleType(ScaleType.FIT_XY);
		// 压缩图片并显示：
		mBitMap = CompressService.compressHalfSize(this, RUtil.getDrawable(this, "welcome"), 540, 960);
		image.setImageBitmap(mBitMap);
		MyTextView wcTextView = (MyTextView) findViewById(R.id.welcome_textView);
		TextContentService textContent = new TextContentService(this, wcTextView, RUtil.getValuesStringArray(
				WelcomeActivity.this, "welcome_textContent"));
		textContent.showContent();

		// 点击进入广告页面
		image.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(RUtil.getValuesString(WelcomeActivity.this, "welcome_advertisement"));
				intent.setData(content_url);
				startActivity(intent);
				isHangUp = true; // 加载广告页面，次Activity被挂起
				return true;
			}
		});
	}

	private void loadNext() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				done = true; // 加载下一个Activity已执行
				// 如果此Activity被挂起，将不会加载下一页面
				if (isHangUp)
					return;
				// 释放图片资源
				CompressService.releaseBitMap(mBitMap);
				finish();
				startActivity(new Intent("android.intent.action.mytest.MAIN"));
			}
		}, 6000);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 广告页面回来，如果从Activity挂起回来，且加载下一个页面已执行的前提下，再加载到下一个Activity
		// 如果不判断done，loadNext未执行时就会执行loadNextImmediately进入下一页面，然后又执行loadNext
		if (isHangUp) {
			isHangUp = false;
			if (!done) {
				return;
			}
			// 立即进入下一页面
			loadNextImmediately();
		}
	}

	private void loadNextImmediately() {
		// 释放图片资源
		CompressService.releaseBitMap(mBitMap);
		finish();
		startActivity(new Intent("android.intent.action.mytest.MAIN"));
	}
	
	@Override
	public void onBackPressed() {
		finish();
		System.exit(0);
	}
	
}
