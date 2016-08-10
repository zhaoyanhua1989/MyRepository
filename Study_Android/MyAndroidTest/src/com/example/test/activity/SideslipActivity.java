package com.example.test.activity;

import com.example.test.MyApplication;
import com.example.test.R;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SideslipActivity extends FragmentActivity implements TabListener {

	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sideslip);
		MyApplication.activitys.add(this);
		// 创建ActionBar
		initActionBar();
	}

	/************************ 创建ActionBar和Tab ************************/
	private void initActionBar() {
		mActionBar = getActionBar();
		// 隐藏logo和icon，不想隐藏可以注释
		// mActionBar.setDisplayShowHomeEnabled(false);
		// 隐藏Label标签，不想隐藏可以注释
		// mActionBar.setDisplayShowTitleEnabled(false);
		// 设置ActionBar展示模式
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mActionBar.addTab(mActionBar.newTab().setText("页面1").setTabListener(this));
		mActionBar.addTab(mActionBar.newTab().setText("页面2").setTabListener(this));
		mActionBar.addTab(mActionBar.newTab().setText("页面3").setTabListener(this));
		mActionBar.addTab(mActionBar.newTab().setText("页面4").setTabListener(this));
		// 设置导航功能(点击返回上一页)
		mActionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO 当Tab选中时
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	/******************** 创建ActionBar的功能按钮(右上角) ********************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.viewpager_activity_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // 左上角ActionBar标签，实现导航跳转回主Activity
			// 获取跳转到MainActivity的intent
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			// 如果需要创建task(不在同一个task中)，则创建task再跳转，否则直接通过navigateUpTo跳转
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
			} else {
				upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		default:
			Toast.makeText(this, "分享-" + item.getTitle(), Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
