package com.example.test.activity;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.test.MyApplication;
import com.example.test.R;
import com.example.test.util.MyLog;

public class NotificationActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.activitys.add(this);
		// setContentView(R.layout.activity_notifycation);
		setListAdapter(ArrayAdapter.createFromResource(this, R.array.notifycation_main,
				android.R.layout.simple_list_item_1));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		MyLog.d("选中了item，position=" + position);
		switch (position) {
		case 0:
			createNtf1();
			break;
		case 1:
			// TODO 启动另一个app
			break;
		}
	}

	private void createNtf1() {
		// 这里没用Notification.Builder，因为在build的时候会提示方法不存在
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(android.R.drawable.ic_dialog_email);
		builder.setContentTitle("活动通知");
		builder.setContentText("拍照分享");
		builder.setTicker("您收到了第一条消息");

		// 设置跳转页面(可选)，在点击消息时会触发intent，这里模拟启动照相机
		Intent intent = new Intent(this, NotificationCameraActivity.class);
		// 延迟intent， (contex, 请求码, intent, 延迟意图)
		PendingIntent pIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pIntent);

		// 创建通知对象
		Notification ntf = builder.build();
		// 设置消息点击后，自动消失
		ntf.flags = Notification.FLAG_AUTO_CANCEL;
		// 获得通知管理器
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// 发送通知
		nm.notify(0, ntf);// systemUI进程
		
		// TODO 如果用Notification直接拉起照相机，没法获取onActivityResult()
	}

}
