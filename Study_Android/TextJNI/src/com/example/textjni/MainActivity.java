package com.example.textjni;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.androidjni.R;

/**
 * 调用了libuninstalled_moniter.so来在C端新建一个子线程，轮询目录data/data/包名，如果不存在则说明应用已被卸载
 * 如果应用已被卸载，可以进行其他操作，比如拉起浏览器弹出调查问卷、删除指定文件等操作
 * 
 * C++实现这里是拉起浏览器，但是存在bug没有弹出浏览器跳转url，而且实现存在一些其他问题，比如覆盖安装、清理缓存等问题
 * @author HKW2962
 *
 */
public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();

	static {
		System.loadLibrary("uninstalled_moniter");
	}

	private native void init();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		initView();
		init();
	}

	private void initView() {
		TextView getIntTV = (TextView) findViewById(R.id.getIntTV);
		TextView getStringTV = (TextView) findViewById(R.id.getStringTV);

		getIntTV.setText("" + TextJNI.getJNIInt());
		getStringTV.setText(TextJNI.getJNIString());
	}
}