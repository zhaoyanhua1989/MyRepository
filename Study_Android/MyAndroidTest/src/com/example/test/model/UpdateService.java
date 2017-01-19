package com.example.test.model;

import java.io.File;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.test.activity.NotificationActivity;
import com.example.test.util.AppUtil;
import com.example.test.util.FileUtil;
import com.example.test.util.MyLog;
import com.example.test.util.OverallVariable;
import com.example.test.util.PropertiesUtil;
import com.example.test.util.RUtil;
import com.example.test.util.ToastUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class UpdateService {

	private Activity mContext;
	private Handler mHandler;
	private AsyncHttpClient asyncHttpClient;
	private NotificationManager nm; // 通知管理对象，处理下载进度条
	private ProgressBar pb; // 进度条对象
	private TextView progressTV; // 下载进度textView

	public UpdateService(Activity context, Handler handler) {
		mContext = context;
		mHandler = handler;
		asyncHttpClient = new AsyncHttpClient();
	}

	public void checkUpdateInformation() {
		AppUtil.showProgressDialog(mContext, "正在查询，请稍后......", false);
		asyncHttpClient.get(OverallVariable.HTTP_REQUEST_UPDATEINFORMATION, new AsyncHttpResponseHandler() {

			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				AppUtil.closeProgressDialog();
				if (statusCode == HttpStatus.SC_OK) {
					String bodyJsonStr = new String(responseBody);
					JSONObject bodyJson = null;
					int code = -1;
					try {
						bodyJson = new JSONObject(bodyJsonStr);
						code = bodyJson.getInt("code");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (code == OverallVariable.HTTP_REQUEST_CUSTOMCODE_OK) {
						String updateVersion = bodyJson.optString("updateVersion");
						if (Double.parseDouble(updateVersion) > Double.parseDouble(PropertiesUtil.getVersion())) {
							// 需要升级
							notifyDoUpdate(bodyJson, updateVersion);
						} else {
							// 不需要升级
							mHandler.sendEmptyMessage(OverallVariable.Update.UNDO_UPDATE);
						}
					} else {
						// 返回结果失败，没有找到在线包，不需要更新
						mHandler.sendEmptyMessage(OverallVariable.Update.UNDO_UPDATE);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {
				AppUtil.closeProgressDialog();
				// 发送检查更新请求失败，服务端可能未启动或者没网络，不需要更新
				ToastUtil.showCustomToast(mContext, "请求失败，网络异常或服务端异常...");
			};
		});
	}

	// 处理获取更新信息的需要升级结果
	private void notifyDoUpdate(JSONObject bodyJson, String updateVersion) {
		String apkSize = null;
		String updateContent = null;
		String updateUrl = null;
		String updateDate = null;
		try {
			apkSize = bodyJson.getString("apkSize");
			updateContent = bodyJson.getString("updateContent");
			updateUrl = bodyJson.getString("updateUrl");
			updateDate = bodyJson.getString("updateDate");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString("version", updateVersion);
		data.putString("apkSize", apkSize);
		data.putString("content", updateContent);
		data.putString("updateUrl", updateUrl);
		data.putString("updateDate", updateDate);
		msg.setData(data);
		msg.what = OverallVariable.Update.DO_UPDATE;
		mHandler.handleMessage(msg);
	}

	/**
	 * 开始更新业务，下载apk
	 * 
	 * @param url
	 *            更新包的下载地址
	 */
	public void downloadUpdateApk(final String url) {
		// 显示顶部通知栏UI
		initUpdateProgressUI();
		asyncHttpClient.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String fileName = null;
				if (statusCode == HttpStatus.SC_OK) {
					try {
						fileName = url.substring(url.lastIndexOf("/") + 1);
						FileUtil.writeToSdcard(mContext, OverallVariable.Update.APK_PATH, fileName, responseBody);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						Message msg = new Message();
						msg.what = OverallVariable.Update.INSTALLAPK;
						Bundle bundle = new Bundle();
						bundle.putString("path", OverallVariable.Update.APK_PATH + File.separator + fileName);
						msg.setData(bundle);
						mHandler.sendMessage(msg);
					}
				}
			}

			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				super.onProgress(bytesWritten, totalSize);
				// TODO 显示进度条
				MyLog.d("正在下载，当前进度：bytesWritten=" + bytesWritten + ", totalSize=" + totalSize);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {
				ToastUtil.showCustomToast(mContext, "下载apk失败，请检查网络...");
			}
		});
	}

	private void initUpdateProgressUI() {
		// 这里没用Notification.Builder，因为在build的时候会提示方法不存在
		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

		// 自定义的通知界面，仿音乐播放器
		RemoteViews contentViews = new RemoteViews(mContext.getPackageName(), RUtil.getLayout(mContext, "my_custom_updateProgress"));
		contentViews.setImageViewResource(RUtil.getId(mContext, "notification_artist_image"), RUtil.getDrawable(mContext, "custom_notification_icon"));
		contentViews.setTextViewText(RUtil.getId(mContext, "notification_music_title"), "浮夸");
		contentViews.setTextViewText(RUtil.getId(mContext, "notification_music_Artist"), "陈奕迅");

		// 创建通知对象
		Notification mNotification = builder.build();
		mNotification.contentView = contentViews;
		// 注：这里必须设置顶部菜单栏的通知显示的图标，不然不会显示通知
		mNotification.icon = RUtil.getDrawable(mContext, "custom_notification_icon");
		// 设置为常驻消息
		mNotification.flags = Notification.FLAG_NO_CLEAR;
		// 需要注意的是，作为选项，此处可以设置NotificationActivity的启动模式为singleTop，避免重复新建onCreate()。
		Intent activityIntent = new Intent(mContext, NotificationActivity.class);
		// 当用户点击通知栏的Notification时候，切换回NotificationActivity。
		PendingIntent pi = PendingIntent.getActivity(mContext, 200, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.contentIntent = pi;

		// 获得通知管理器
		nm = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
		// 发送通知
		nm.notify(1, mNotification);
	}
}