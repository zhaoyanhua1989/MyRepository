package com.example.test.activity;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.test.MyApplication;
import com.example.test.R;
import com.example.test.util.AppUtil;
import com.example.test.util.FileUtil;
import com.example.test.util.MyLog;
import com.example.test.util.ToastUtil;

public class GetRAndImeiActivity extends Activity {

	private TextView dirText;
	private TextView nText;
	private TextView imeiText;

	/************************** 测试安卓Data的缓存文件 ***************************/
	// TODO
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_activity_getrandimei);
		MyApplication.activitys.add(this);
		Rinit();
		if (getAccountN(this) == -1) {
			MyLog.i("getAccountN=" + getAccountN(this));
			modifyAccountN(this, 0);
			MyLog.i("init end, getAccountN=" + getAccountN(this));
			nText.setText("" + getAccountN(this));
			dirText.setText(this.getPackageName());
		}
	}

	public void doClick(View v) {
		MyLog.i("login end, AccountN=" + getAccountN(this));
		nText.setText("" + getAccountN(this));
		dirText.setText(this.getPackageName());
		switch (v.getId()) {
		case R.id.imei_button1:
			ToastUtil.showCustomToast(this, "登录成功");
			if (getAccountN(this) == 0) {
				modifyAccountN(this, 1);
				nText.setText("" + getAccountN(this));
				MyLog.i("login modify end, AccountN=" + getAccountN(this));
			}
			break;
		case R.id.imei_button2:
			ToastUtil.showCustomToast(this, "绑定成功");
			modifyAccountN(this, 0);
			nText.setText("" + getAccountN(this));
			MyLog.i("bind end, AccountN=" + getAccountN(this));
			break;
		case R.id.imei_button3:
			String imei = AppUtil.getImei(this);
			imeiText.setText(imei);
			break;
		default:
			break;
		}
	}

	private int getAccountN(Activity activity) {
		// 读取计数
		return FileUtil.getSharedPreferences(activity, "SP").getInt("ACCOUNTN_KEY", -1);
	}

	private void modifyAccountN(Activity activity, int n) {
		// 存入计数
		Editor editor = FileUtil.getSharedPreferences(activity, "SP").edit();
		editor.putInt("ACCOUNTN_KEY", n);
		editor.commit();
	}

	/************************** 测试获取R ***************************/
	// TODO
	private void Rinit() {
		int dirTextId = getR("imei_textView1", "id");
		int nTextId = getR("imei_textView2", "id");
		int imeiTextId = getR("imei_textView3", "id");
		// int iclauncherId = getR("ic_launcher", "drawable");
		// Log.d(HelpUtil.TAG, "dirText R=" + getR("imei_textView1",
		// "id")+"\n"+"id="+dirTextId);
		// Log.d(HelpUtil.TAG, "ic_launcher R=" + getR("ic_launcher",
		// "drawable")+"\n"+"id="+iclauncherId);
		dirText = (TextView) findViewById(dirTextId);
		nText = (TextView) findViewById(nTextId);
		imeiText = (TextView) findViewById(imeiTextId);
	}

	private int getR(String name, String type) {
		return getResources().getIdentifier(name, type, getPackageName());
	}

}
