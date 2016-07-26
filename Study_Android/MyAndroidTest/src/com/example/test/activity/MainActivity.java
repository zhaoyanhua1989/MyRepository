package com.example.test.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.example.test.MyApplication;
import com.example.test.R;
import com.example.test.model.CompressService;
import com.example.test.model.LanguageConventService;
import com.example.test.util.MessageUtil;
import com.example.test.util.MyLog;
import com.example.test.util.RUtil;
import com.example.test.view.MyAlertDialog;

public class MainActivity extends Activity {

	private Bitmap mBitMap;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 隐藏标题栏，隐藏后看不到右上角的menu */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		MyApplication.activitys.add(this);
		// 设置背景图片
		mBitMap = CompressService.compressAssign(this, RUtil.getDrawable(this, "background3"), 540, 960);
		imageView = (ImageView) findViewById(RUtil.getId(this, "main_backgroundImageView"));
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setImageBitmap(mBitMap);
	}

	@Override
	protected void onRestart() {
		MyLog.i("onRestart, mBitMap=" + mBitMap);
		super.onRestart();
		// 设置背景图片
		mBitMap = CompressService.compressAssign(this, RUtil.getDrawable(this, "background3"), 540, 960);
		imageView.setImageBitmap(mBitMap);
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.main_button1:
			CompressService.releaseBitMap(mBitMap);
			startActivity(new Intent(this, GetRAndImeiActivity.class));
			break;
		case R.id.main_button2:
			CompressService.releaseBitMap(mBitMap);
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
			MyAlertDialog myDialog = new MyAlertDialog(this);
			myDialog.showMyAlertDialog();
			break;
		case R.id.main_button6:
			// 测试ListView和ViewPager
			CompressService.releaseBitMap(mBitMap);
			startActivity(new Intent(this, ViewPagerActivity.class));
			break;
		case R.id.main_button7:
			// 测试notifycation和拉起另一个应用
			CompressService.releaseBitMap(mBitMap);
			startActivity(new Intent(this, NotificationActivity.class));
			break;
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("确认退出吗")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						for (Activity activity : MyApplication.activitys) {
							activity.finish();
						}
						System.exit(0);
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).setMultiChoiceItems(new String[] { "清除记录", "其他" }, new boolean[] { true, false }, null).create();
		dialog.show();
	}

	/*************************** Activity右上角menu *****************************/
	// 重写菜单，显示menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// 监听menu点击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete) {
			Toast.makeText(this, "menu点击事件", Toast.LENGTH_SHORT).show();
		}
		return super.onOptionsItemSelected(item);
	}

}