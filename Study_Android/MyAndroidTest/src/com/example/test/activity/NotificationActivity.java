package com.example.test.activity;

import java.io.File;

import android.app.Activity;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.test.MyApplication;
import com.example.test.R;
import com.example.test.util.BitmapUtil;
import com.example.test.util.FileUtil;
import com.example.test.util.MyLog;
import com.example.test.util.RUtil;
import com.example.test.util.ToastUtil;

public class NotificationActivity extends ListActivity {

	private boolean isPause;
	private MyMusicBroadcastReceiver mMusicBroadcastReceiver;
	private final String ACTION_NOTIFICATION = "com.example.test.ACTIONNOTIFICATION";
	private final String BUTTON_TAG = "button_tag";
	private final int MUSIC_GOPRE = 1; // 上一首音乐
	private final int MUSIC_GONEXT = 2; // 下一首音乐
	private final int MUSIC_DOPAUSEORCONTINUE = 3; // 暂停或继续
	private NotificationManager mNotificationManager; // 系统通知管理对象
	private NotificationManager mcNotificationManager; // 自定义UI的通知管理对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.activitys.add(this);
		// setContentView(R.layout.activity_notifycation);
		setListAdapter(ArrayAdapter.createFromResource(this, R.array.notifycation_main, android.R.layout.simple_list_item_1));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case 0:
			// 普通类型通知
			createNtf1();
			break;
		case 1:
			// 常驻自定义样式的通知
			createNtf2();
			break;
		}
	}

	private void createNtf1() {
		// 这里没用Notification.Builder，因为在build的时候会提示方法不存在
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		// 使用app的图片
		builder.setSmallIcon(RUtil.getAppIcon(this));
		// builder.setSmallIcon(android.R.drawable.ic_dialog_email); //使用安卓邮件图片
		builder.setContentTitle("活动通知");
		builder.setContentText("拍照分享");
		builder.setTicker("您收到了一条消息");

		// 设置跳转页面(可选)，在点击消息时会触发intent，这里模拟启动照相机
		Intent activityIntent = new Intent(this, NotificationCameraActivity.class);
		// 延迟intent， (contex, 请求码, intent, 延迟意图)
		PendingIntent pIntent = PendingIntent.getActivity(this, 100, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pIntent);

		// 创建通知对象
		Notification mNotification = builder.build();
		// 设置消息点击后，自动消失
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		// 添加声音效果
		mNotification.defaults = Notification.DEFAULT_SOUND;
		// 添加震动,需要添加震动权限 : Virbate Permission
		// mNotification.defaults = Notification.DEFAULT_VIBRATE;

		// 获得通知管理器
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// 发送通知，第一个参数决定通知是并列显示还是覆盖显示
		mNotificationManager.notify(0, mNotification); // systemUI进程

		// TODO 如果用Notification直接拉起照相机，没法获取onActivityResult()
	}

	private void createNtf2() {
		// 这里没用Notification.Builder，因为在build的时候会提示方法不存在
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

		// 自定义的通知界面，仿音乐播放器
		RemoteViews contentViews = new RemoteViews(getPackageName(), RUtil.getLayout(this, "custom_notification"));
		contentViews.setImageViewResource(RUtil.getId(this, "notification_artist_image"), RUtil.getDrawable(this, "custom_notification_icon"));
		contentViews.setTextViewText(RUtil.getId(this, "notification_music_title"), "浮夸");
		contentViews.setTextViewText(RUtil.getId(this, "notification_music_Artist"), "陈奕迅");
		// 设置按钮的监听事件
		setListeners(contentViews);

		// 创建通知对象
		Notification mNotification = builder.build();
		mNotification.contentView = contentViews;
		// 注：这里必须设置顶部菜单栏的通知显示的图标，不然不会显示通知
		mNotification.icon = RUtil.getDrawable(this, "custom_notification_icon");
		// 设置为常驻消息
		mNotification.flags = Notification.FLAG_NO_CLEAR;
		// 需要注意的是，作为选项，此处可以设置NotificationActivity的启动模式为singleTop，避免重复新建onCreate()。
		Intent activityIntent = new Intent(this, NotificationActivity.class);
		// 当用户点击通知栏的Notification时候，切换回NotificationActivity。
		PendingIntent pi = PendingIntent.getActivity(this, 200, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.contentIntent = pi;

		// 获得通知管理器
		mcNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// 发送通知
		mcNotificationManager.notify(1, mNotification);
	}

	private void setListeners(RemoteViews contentViews) {
		if (mMusicBroadcastReceiver == null) {
			mMusicBroadcastReceiver = new MyMusicBroadcastReceiver();
			IntentFilter filter = new IntentFilter(ACTION_NOTIFICATION);
			registerReceiver(mMusicBroadcastReceiver, filter);
		}
		// 外部桌面控件的监听要通过BroadcastReceiver来监听
		Intent intent1 = new Intent(ACTION_NOTIFICATION);
		intent1.putExtra(BUTTON_TAG, MUSIC_GOPRE);
		contentViews.setOnClickPendingIntent(RUtil.getId(this, "notification_pre_button"), PendingIntent.getBroadcast(this, MUSIC_GOPRE, intent1, 0));

		intent1.putExtra(BUTTON_TAG, MUSIC_GONEXT);
		contentViews.setOnClickPendingIntent(RUtil.getId(this, "notification_next_button"), PendingIntent.getBroadcast(this, MUSIC_GONEXT, intent1, 0));

		intent1.putExtra(BUTTON_TAG, MUSIC_DOPAUSEORCONTINUE);
		contentViews.setOnClickPendingIntent(RUtil.getId(this, "notification_play_button"), PendingIntent.getBroadcast(this, MUSIC_DOPAUSEORCONTINUE, intent1, 0));
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, MyApplication.activitys.get(0).getClass()));
	}

	@Override
	public void finish() {
		MyLog.d("call NotificationActivity finish...");
		if (mMusicBroadcastReceiver != null) {
			unregisterReceiver(mMusicBroadcastReceiver);
			mMusicBroadcastReceiver = null;
		}
		if (mNotificationManager != null) {
			// 取消显示普通通知
			mNotificationManager.cancel(0);
			mNotificationManager = null;
		}
		if (mcNotificationManager != null) {
			// 取消显示自定义UI的通知
			mcNotificationManager.cancel(1);
			mcNotificationManager = null;
		}
		super.finish();
	}

	/**
	 * 注意添加广播后，外部类中，通过PendingIntent.getActivity拉起的Activity， 要添加外部应用的访问权限
	 * android:exported="true"
	 * 
	 * @author HKW2962
	 */
	class MyMusicBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			MyLog.d("进来了，action=" + intent.getAction());
			Activity mActivity = null;
			if (context instanceof Activity) {
				mActivity = (Activity) context;
			}
			if (ACTION_NOTIFICATION.equals(intent.getAction())) {
				MyLog.d("BUTTON_TAG=" + intent.getIntExtra(BUTTON_TAG, -1));
				switch (intent.getIntExtra(BUTTON_TAG, -1)) {
				case MUSIC_GOPRE:
					if (mActivity != null)
						ToastUtil.showCustomToast(mActivity, "上一首");
					break;
				case MUSIC_GONEXT:
					if (mActivity != null)
						ToastUtil.showCustomToast(mActivity, "下一首");
					break;
				case MUSIC_DOPAUSEORCONTINUE:
					if (!isPause) {
						if (mActivity != null)
							ToastUtil.showCustomToast(mActivity, "暂停");
						isPause = !isPause;
					} else {
						if (mActivity != null)
							ToastUtil.showCustomToast(mActivity, "继续播放");
						isPause = !isPause;
					}
					break;
				default:
					if (mActivity != null)
						ToastUtil.showCustomToast(mActivity, "收到未知监听事件");
					break;
				}
			}
		}
	}

	public static class NotificationCameraActivity extends Activity {

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
				imageView.setImageBitmap(BitmapUtil.getSmallBitmap(getRealPathFromURI(outputFileUri), 720, 1280));
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
		/*@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this, event)) {
				return true;
			}
			return super.onTouchEvent(event);
		}

		private boolean isOutOfBounds(Activity context, MotionEvent event) {
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
			final View decorView = context.getWindow().getDecorView();
			return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop)) || (y > (decorView.getHeight() + slop));
		}*/
	}
}
