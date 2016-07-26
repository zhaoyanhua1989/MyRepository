package com.example.test.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.util.BitmapBiz;
import com.example.test.util.FileUtil;
import com.example.test.util.MyLog;

public class NotificationCameraActivity extends Activity {

	private Uri outputFileUri; // 拍照照片存储路径

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notification_camera);
	}

	public void doClick(View v) {
		// 设置跳转页面(可选)，在点击消息时会触发intent，这里模拟启动照相机
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		// 设置照片储存目录
		File file = FileUtil.createImageFile();
		outputFileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, 100);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 默认保存目录传回data，指定保存目录传回的data为null
		MyLog.d("onActivityResult, requestCode=" + requestCode + ", resultCode=" + resultCode);
		if (requestCode == 100 && resultCode == -1) {
			// 显示图片名字
			TextView textView = (TextView) findViewById(R.id.notification_camera_textView);
			textView.setText(new File(outputFileUri.getPath()).getName());
			// 显示图片
			ImageView imageView = (ImageView) findViewById(R.id.notification_camera_imageView);
			// 如果是默认保存目录，可能会OOM
			// ToastUtil.showToast(this, "获取照片");
			// Object obj = data.getExtras().get("data");
			// setContentView(R.layout.activity_notification);
			// imageView.setImageBitmap((Bitmap)obj);
			// 如果是指定保存目录
			imageView.setImageBitmap(BitmapBiz.getSmallBitmap(getRealPathFromURI(outputFileUri), 720, 1280));
			// 动态改变Activity的大小，使button靠底位置，显示上面的ScrollView
			Button button = (Button) findViewById(R.id.notification_camera_button);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			button.setLayoutParams(layoutParams);
		}
	}

	private String getRealPathFromURI(Uri contentURI) {
		MyLog.d("getRealPathFromURI...");
		String result;
		Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
		if (cursor == null) {
			// Source is Dropbox or other similar local file path
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		MyLog.d("result=" + result);
		return result;
	}

	@Override
	public void onBackPressed() {
		MyLog.d("onBackPressed");
		// 直接返回，文件目录outputFileUri未赋值
		if (outputFileUri != null) {
			String filePath = outputFileUri.getPath();
			final File file = new File(filePath.substring(0, filePath.lastIndexOf("/")));
			MyLog.d("fileDir path is:" + filePath.substring(0, filePath.lastIndexOf("/")));
			// 指定了文件目录，但是照相没拍照，文件没保存可能也没目录，所以要检查目录是否存在
			if (file.exists()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 因为这里是内置存储根目录，所以不删除文件夹
						FileUtil.deleteSDFile(file, "MYJPEG_", false);
					}
				}).start();
			}
		}
		this.finish();
	}

	/**
	 * 设置窗口Activity点击窗口外的地方，不会使Activity finish掉，用于 API Level<11。 当API
	 * Level>=11时，用自定义theme实现，见style.xml (本工程实现采用的方法)。
	 */
	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this,
	// event)) {
	// return true;
	// }
	// return super.onTouchEvent(event);
	// }
	//
	// private boolean isOutOfBounds(Activity context, MotionEvent event) {
	// final int x = (int) event.getX();
	// final int y = (int) event.getY();
	// final int slop =
	// ViewConfiguration.get(context).getScaledWindowTouchSlop();
	// final View decorView = context.getWindow().getDecorView();
	// return (x < -slop) || (y < -slop)|| (x > (decorView.getWidth() + slop))||
	// (y > (decorView.getHeight() + slop));
	// }
}
