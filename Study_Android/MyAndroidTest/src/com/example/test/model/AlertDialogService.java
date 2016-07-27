package com.example.test.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.MyApplication;
import com.example.test.R;
import com.example.test.util.RUtil;
import com.example.test.util.ScreenUtil;
import com.example.test.util.ToastUtil;
import com.example.test.view.MyCustomDialog;

/**
 * 处理AlertDialog的业务类
 * 
 * @author HKW2962
 *
 */
public class AlertDialogService {

	/****************** 自定义AlertDialog，没继承Dialog ******************/
	// 没有直接继承Dialog，有缺陷，设置background时，当设置圆角时有尖角
	@SuppressLint("InflateParams")
	public static void showUpdateDialog(final Activity Activity) {
		View view = Activity.getLayoutInflater().inflate(R.layout.new_update_dialog, null);
		// 创建builder，传参Context，theme(style)的id
		// 创建自定义样式的Dialog，背景设置了透明，不然有黑色框框。设置了windowIsTranslucent，不然某些机型上UI会别截掉一部分
		// AlertDialog.Builder builder = new AlertDialog.Builder(this,
		// R.style.updateDialogStyle);
		AlertDialog.Builder builder = new AlertDialog.Builder(Activity, 0);
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		/**
		 * 在安卓4.4到5.0之间系统的手机上，AlertDialog的顶部可能被剪切掉，所以需要设置Flags
		 */
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.show();
		dialog.setCancelable(false);
		// 竖屏适配
		if (!ScreenUtil.isLandscape(Activity)) {
			// 竖屏时限制高度为屏幕高度的一半，需要判断
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.height = (int) Math.ceil((Activity.getResources().getDisplayMetrics().heightPixels) / 2);
			dialog.getWindow().setAttributes(lp);
			// 该表UI字体适配布局
			((Button) (view.findViewById(R.id.update_dialog_exit))).setTextSize(12);
			;
			((Button) (view.findViewById(R.id.update_dialog_nextUpdate))).setTextSize(12);
			;
			((Button) (view.findViewById(R.id.update_dialog_updateNow))).setTextSize(12);
			;
			((TextView) (view.findViewById(R.id.update_dialog_explain1))).setTextSize(9);
			((TextView) (view.findViewById(R.id.update_dialog_explain2))).setTextSize(9);
			((TextView) (view.findViewById(R.id.update_dialog_explain3))).setTextSize(9);
		}
		// 监听按钮
		view.findViewById(R.id.update_dialog_exit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showToast(Activity, "退出游戏");
				dialog.dismiss();
				Activity.onBackPressed();
			}
		});
		view.findViewById(R.id.update_dialog_nextUpdate).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showToast(Activity, "下次更新");
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.update_dialog_updateNow).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showToast(Activity, "立即更新");
				dialog.dismiss();
			}
		});
		// 设置内容
		// TextView textView = (TextView)
		// view.findViewById(R.id.update_dialog_textView);
		// textView.setText("");
		/** 如和让ScrollView的ScrollBar恒显示？ fadeScrollbars设置为false，见xml */
		/**
		 * AlertDialog的自定义view设置background后，AlertDialog会有黑色边框。
		 * 需要设置AlertDialog的theme才能去掉。
		 */
	}

	/**
	 * 显示系统退出弹窗
	 */
	public static void showSystemExitDialog(Activity Activity) {
		AlertDialog dialog = new AlertDialog.Builder(Activity).setTitle("确认退出吗")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						for (Activity activity : MyApplication.activitys) {
							activity.finish();
						}
						System.exit(0);
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).setMultiChoiceItems(new String[] { "清除记录", "其他" }, new boolean[] { true, false }, null).create();
		dialog.show();
	}

	/**
	 * 显示自定义退出弹窗
	 */
	@SuppressLint("InflateParams")
	public static void showSimpleCustomExitDialog(Activity Activity) {
		View view = Activity.getLayoutInflater().inflate(R.layout.new_exit_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(Activity, RUtil.getStyle(Activity, "exitDialogStyle"));
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		/**
		 * 在安卓4.4到5.0之间系统的手机上，AlertDialog的顶部可能被剪切掉，所以需要设置Flags
		 */
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog.show();
		// 监听按钮
		view.findViewById(R.id.exit_cancelButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.exit_sureButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				for (Activity activity : MyApplication.activitys) {
					activity.finish();
				}
				System.exit(0);
			}
		});
	}
	
	/****************** 自定义AlertDialog，继承了Dialog ******************/
	public static void showCustemDialogForExit(Activity activity) {
		MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(activity);
		customBuilder.setTitle("退出").setContentWebView("run.gif").setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				for (Activity activity : MyApplication.activitys) {
					activity.finish();
				}
				System.exit(0);
			}
		});
		MyCustomDialog dialog = customBuilder.create();
		dialog.show();
	}

}
