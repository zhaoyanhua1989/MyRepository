package com.example.test.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.test.MyApplication;
import com.example.test.R;
import com.example.test.model.AlertDialogService;
import com.example.test.model.LanguageConventService;
import com.example.test.util.MessageUtil;
import com.example.test.util.ToastUtil;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 隐藏标题栏，隐藏后看不到右上角的menu */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		MyApplication.activitys.add(this);
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
		}
	}

	// 显示Popup弹窗，展示自定义view
	private void showDialogs(View v) {
		// 显示PopupMenu(不能设置background)
		// AlertDialogService.showPopupMenu(this, v);
		// 显示PopupWindow(可以设置background)
		AlertDialogService.showPopupWindow(this, v);
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
		getMenuInflater().inflate(R.menu.main_activity_menu, menu);
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

}
