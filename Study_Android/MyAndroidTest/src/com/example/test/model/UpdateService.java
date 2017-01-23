package com.example.test.model;

import java.io.File;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.example.test.base.AsyncHttpClientCancelTaskListener;
import com.example.test.util.AppUtil;
import com.example.test.util.FileUtil;
import com.example.test.util.MyLog;
import com.example.test.util.OverallVariable;
import com.example.test.util.PropertiesUtil;
import com.example.test.util.RUtil;
import com.example.test.util.ToastUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

public class UpdateService {

	private Activity mContext;
	private Handler mHandler;
	private AsyncHttpClient asyncHttpClient;
	private NotificationManager nm; // 通知管理对象，处理下载进度条
	private Notification mNotification; // 通知对象，更新UI用
	private int update_progressBarId; // 进度条对象
	private int update_progressTVId; // 下载进度textView
	private int update_done_AppnameTVId; // 下载完成后显示app名字的textView
	private int update_done_timeTVId; // 下载完成后显示完成时间的textView
	private final int RESPONDETIMEOUT = 5000; // 请求响应超时时间
	// 用于控制限制重复下载同一文件
	private HashMap<String, RequestHandle> requestHandles = new HashMap<String, RequestHandle>();
	private AsyncHttpClientCancelTaskListener cancelTaskListener;

	public UpdateService(Activity context, Handler handler) {
		mContext = context;
		mHandler = handler;
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.setTimeout(RESPONDETIMEOUT);
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
				mHandler.sendEmptyMessage(OverallVariable.Update.UNDO_UPDATE);
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
	 * 开始更新业务，适合下载小文件
	 * 
	 * @param url
	 *            更新包的下载地址
	 */
	public void downloadUpdateApk(final String url) {
		if (!checkDownLoadTask(url)) {
			// 如果没有相同下载任务，注册监听
			cancelTaskListener = new AsyncHttpClientCancelTaskListener() {
				@Override
				public void cancelTask() {
					// 取消旧任务，开始新任务
					requestHandles.get(url).cancel(true);
					requestHandles.remove(url);
					doUpdateApk(url);
				}
			};
		}
		if (requestHandles.get(url) == null) {
			// 如果没有任务，开始一项新任务
			doUpdateApk(url);
		}
	}

	private void doUpdateApk(final String url) {
		// 显示顶部通知栏UI
		final RemoteViews contentView = new RemoteViews(mContext.getPackageName(), RUtil.getLayout(mContext, "my_custom_updateprogress"));
		initUpdateProgressUI(contentView);
		RequestHandle requestHandler = asyncHttpClient.get(url, new AsyncHttpResponseHandler() {

			private int flagNum;
			private final int flagNumStandard = 1;

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				String fileName = null;
				String filePath = null;
				if (statusCode == HttpStatus.SC_OK) {
					try {
						// 移除缓存检查重复下载的任务
						requestHandles.remove(url);
						cancelTaskListener = null;
						// 文件名
						fileName = url.substring(url.lastIndexOf("/") + 1);
						// 将文件写入SD卡
						FileUtil.writeToSdcardFromBytes(OverallVariable.Update.APK_PATH, fileName, responseBody);
						// 文件路径
						filePath = OverallVariable.Update.APK_PATH + File.separator + fileName;
						// 下载完成更新状态栏UI
						dealWithUpdateDoneUI(contentView, filePath, fileName, AppUtil.getTimeFormatAm_Pm_HH_mm());
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						// 弹出安装界面
						Message msg = new Message();
						msg.what = OverallVariable.Update.INSTALLAPK;
						Bundle bundle = new Bundle();
						bundle.putString("path", filePath);
						msg.setData(bundle);
						mHandler.sendMessage(msg);
					}
				}
			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				super.onProgress(bytesWritten, totalSize);
				// 实时更新ProgressBar的进度，这里每次增长1%
				if (flagNum == 0 || (bytesWritten * 100D / totalSize - flagNumStandard) >= flagNum) {
					flagNum += flagNumStandard;
					updateProgressBar(contentView, flagNum);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable throwable) {
				ToastUtil.showCustomToast(mContext, "下载apk失败，请检查网络...");
			}
		});
		requestHandles.put(url, requestHandler);
	}

	/**
	 * 开始更新业务，适合下载大文件
	 * 
	 * @param url
	 *            更新包的下载地址
	 */
	public void downloadBigUpdateApk(final String url) {
		if (!checkDownLoadTask(url)) {
			// 如果没有相同下载任务，注册监听
			cancelTaskListener = new AsyncHttpClientCancelTaskListener() {
				@Override
				public void cancelTask() {
					// 取消旧任务，开始新任务
					requestHandles.get(url).cancel(true);
					requestHandles.remove(url);
					doBigUpdateApk(url);
				}
			};
		}
		if (requestHandles.get(url) == null) {
			// 如果没有任务，开始一项新任务
			doBigUpdateApk(url);
		}
	}

	private void doBigUpdateApk(final String url) {
		// 显示顶部通知栏UI
		final RemoteViews contentView = new RemoteViews(mContext.getPackageName(), RUtil.getLayout(mContext, "my_custom_updateprogress"));
		initUpdateProgressUI(contentView);
		// 文件名
		final String fileName = url.substring(url.lastIndexOf("/") + 1);
		// 文件路径
		final String filePath = OverallVariable.Update.APK_PATH + File.separator + fileName;
		asyncHttpClient.setTimeout(10 * 60 * 1000);
		asyncHttpClient.setURLEncodingEnabled(false);
		RequestHandle requestHandler = asyncHttpClient.get(url, new FileAsyncHttpResponseHandler(new File(filePath)) {

			private int flagNum;
			private final int flagNumStandard = 1;

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
				ToastUtil.showCustomToast(mContext, "下载apk失败，请检查网络...");
			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				super.onProgress(bytesWritten, totalSize);
				// 实时更新ProgressBar的进度，这里每次增长1%
				if (flagNum == 0 || (bytesWritten * 100D / totalSize - flagNumStandard) >= flagNum) {
					flagNum += flagNumStandard;
					MyLog.d("已下载：" + flagNum + "%");
					updateProgressBar(contentView, flagNum);
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, File file) {
				if (statusCode == HttpStatus.SC_OK) {
					try {
						// 移除缓存检查重复下载的任务
						requestHandles.remove(url);
						cancelTaskListener = null;
						// 下载完成更新状态栏UI
						dealWithUpdateDoneUI(contentView, filePath, fileName, AppUtil.getTimeFormatAm_Pm_HH_mm());
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						// 弹出安装界面
						Message msg = new Message();
						msg.what = OverallVariable.Update.INSTALLAPK;
						Bundle bundle = new Bundle();
						bundle.putString("path", filePath);
						msg.setData(bundle);
						mHandler.sendMessage(msg);
					}
				}
			}
		});
		requestHandles.put(url, requestHandler);
	}

	/**
	 * 检查是否存在相同的下载任务
	 * 
	 * @param url
	 *            key
	 * @param requestHandler
	 *            新的任务
	 * @return 如果存在则返回true
	 */
	private synchronized boolean checkDownLoadTask(final String url) {
		if (requestHandles.get(url) == null) {
			return false;
		} else if (requestHandles.get(url) != null && !requestHandles.get(url).isFinished()) {
			AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("已经存在同一个下载任务，是否重新开始下载？").setNegativeButton("否", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).setPositiveButton("是", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (cancelTaskListener != null) {
						cancelTaskListener.cancelTask();
					}
					dialog.dismiss();
				}
			}).create();
			dialog.show();
			return true;
		}
		return false;
	}

	/**
	 * 初始化状态栏下载进度UI
	 * 
	 * @param contentView
	 */
	private void initUpdateProgressUI(RemoteViews contentView) {
		update_progressBarId = RUtil.getId(mContext, "updateProgress_ProgressBar");
		update_progressTVId = RUtil.getId(mContext, "updateProgress_Progress_text");
		update_done_AppnameTVId = RUtil.getId(mContext, "updateProgress_Done_AppName");
		update_done_timeTVId = RUtil.getId(mContext, "updateProgress_Done_Time");

		// 这里没用Notification.Builder，因为在build的时候会提示方法不存在
		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
		// 创建通知对象
		mNotification = builder.build();
		mNotification.contentView = contentView;
		// 注：这里必须设置顶部菜单栏的通知显示的图标，不然不会显示通知
		mNotification.icon = RUtil.getDrawable(mContext, "my_ic_launcher");
		mNotification.tickerText = "您开始了一项下载任务";
		// 设置为常驻消息
		mNotification.flags = Notification.FLAG_NO_CLEAR;
		// 获得通知管理器
		nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		// 发送通知
		nm.notify(1, mNotification);
	}

	/**
	 * 实时更新ProgressBar的进度
	 * 
	 * @param contentView
	 * @param n
	 *            需要更新的ProgressBar的进度
	 */
	private void updateProgressBar(RemoteViews contentView, int n) {
		contentView.setTextViewText(update_progressTVId, n + "%");
		contentView.setProgressBar(update_progressBarId, 100, n, false);
		nm.notify(1, mNotification);
	}

	/**
	 * 处理下载完状态栏的UI
	 * 
	 * @param contentView
	 * @param filePath
	 *            文件路径
	 * @param name
	 *            文件名
	 * @param time
	 *            下载完成时间
	 */
	private void dealWithUpdateDoneUI(RemoteViews contentView, String filePath, String name, final String time) {
		contentView.setViewVisibility(RUtil.getId(mContext, "updateProgress_Progress_iconZone"), View.GONE);
		contentView.setViewVisibility(RUtil.getId(mContext, "updateProgress_Done_icon"), View.VISIBLE);
		contentView.setViewVisibility(update_progressBarId, View.GONE);
		contentView.setViewVisibility(RUtil.getId(mContext, "updateProgress_Done_icon"), View.VISIBLE);
		contentView.setViewVisibility(RUtil.getId(mContext, "updateProgress_Done_InformationZone"), View.VISIBLE);
		contentView.setTextViewText(update_done_AppnameTVId, name);
		contentView.setTextViewText(update_done_timeTVId, time);

		// 设置点击后安装
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri data = Uri.fromFile(new File(filePath));
		// type:表示的是文件 类型, mime:定义文件类型 text/html ,text/xml
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(data, type);
		// 延迟intent， (contex, 请求码, intent, 延迟意图)
		PendingIntent pIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.contentIntent = pIntent;

		// 设置消息点击后，自动消失
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		nm.notify(1, mNotification);
	}
}