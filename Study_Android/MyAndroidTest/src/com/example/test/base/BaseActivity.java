package com.example.test.base;

import com.example.test.MyApplication;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.activitys.add(this);
		initVariables();
		initViews(savedInstanceState);
		loadData();
	}

	/**
	 * 初始化变量，包括Intent带的数据
	 */
	protected abstract void initVariables();

	/**
	 * 加载layout布局文件，初始化控件，为控件挂上事件方法
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void initViews(Bundle savedInstanceState);

	/**
	 * 调用业务处理数据
	 */
	protected abstract void loadData();

	@Override
	public void onBackPressed() {
		if (doOnBackPressed()) {
			releaseResource();
			super.onBackPressed();
		}
	}

	/**
	 * 在onBackPressed中实现自己的逻辑，并决定要不要实现正常的onBackPressed事件
	 * 
	 * @return 如果需要拦截onBackPressed事件，这里返回false
	 */
	protected boolean doOnBackPressed() {
		return true;
	}

	/**
	 * 释放资源
	 */
	protected void releaseResource() {

	}
}
