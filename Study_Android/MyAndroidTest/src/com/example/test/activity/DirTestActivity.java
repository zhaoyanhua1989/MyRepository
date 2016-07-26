package com.example.test.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.example.test.MyApplication;
import com.example.test.R;
import com.example.test.util.HelpUtil;
import com.example.test.util.MyLog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class DirTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.example.test.R.layout.activity_dir_test);
		TextView tv = (TextView) findViewById(R.id.dir_textView1);
		MyApplication.activitys.add(this);

		try {
			// ///////////////////////////////////////////
			// 内部存储
			// /data/data/包名/app_mulu1
			// /data/data/包名/files
			// ///////////////////////////////////////////

			// /data/data/包名/app_mulu1
			File mulu1 = getDir("mulu1", MODE_PRIVATE);
			tv.setText(mulu1.getAbsolutePath());

			// /data/data/包名/files
			File files = getFilesDir();
			tv.append("\n" + files.getAbsolutePath());

			// /data/data/包名/files/wenjian1
			File f1 = getFileStreamPath("wenjian1");
			f1.createNewFile();
			tv.append("\n" + f1.getAbsolutePath());

			// /data/data/包名/files/wenjian2
			FileOutputStream out = openFileOutput("wenjian2", MODE_PRIVATE);
			out.write("abc".getBytes());
			out.close();
			FileInputStream in = openFileInput("wenjian2");
			tv.append("\n" + in.read());
			tv.append("\n" + in.read());
			tv.append("\n" + in.read());
			in.close();
			String[] list = fileList();
			for (String s : list) {
				tv.append("\n" + s);
			}
			deleteFile("wenjian1");
			tv.append("\n wenjian1 已删除");

			// //////////////////////////////////////////
			// 外部存储
			// //////////////////////////////////////////

			// /mnt/sdcard
			File sdcard = Environment.getExternalStorageDirectory();
			tv.append("\n" + sdcard.getAbsolutePath());

			// /mnt/sdcard/Android/data/包名/files/Music
			File music = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
			tv.append("\n" + music.getAbsolutePath());

			// /mnt/sdcard/Download
			File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			tv.append("\n" + downloads.getAbsolutePath());
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				tv.append("\nsd卡可读写");
			}

		} catch (Exception e) {
			Toast.makeText(this, "出错，请查看 logcat 日志异常信息", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	public void doClick(View v) {
		switch(v.getId()){
			case R.id.dir_button1:
				TextView tv = (TextView) findViewById(R.id.dir_textView2);
				MyLog.i("MyApplication.activitys.get(0)="+MyApplication.activitys.get(0));
				tv.append("MainActivity是否被挂起(isBackground)：" + HelpUtil.isBackground(MyApplication.activitys.get(0)));
				tv.append("\nMainActivity是否被挂起(isApplicationBroughtToBackground)：" + HelpUtil.isApplicationBroughtToBackground(MyApplication.activitys.get(0)));
				break;
		}
	}

}
