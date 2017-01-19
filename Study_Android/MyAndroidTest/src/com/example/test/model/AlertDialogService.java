package com.example.test.model;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.test.MyApplication;
import com.example.test.R;
import com.example.test.util.AppUtil;
import com.example.test.util.MyLog;
import com.example.test.util.OverallVariable;
import com.example.test.util.PropertiesUtil;
import com.example.test.util.ToastUtil;
import com.example.test.view.MyCustomDialog;

/**
 * 处理AlertDialog的业务类
 * 
 * @author HKW2962
 *
 */
public class AlertDialogService {

	private final static String TAG = AlertDialogService.class.getSimpleName();
	private static Dialog loadingBar;
	private static UpdateService mUpdateService;

	/**
	 * 展示更新弹窗（直接使用AlertDialog，有缺陷，设置background时，当设置圆角时有尖角）
	 * 
	 * @param activity
	 * @param needUpdate
	 *            是否需要展示更新信息，传入值应为OverallVariable.Update.UNDO_UPDATE 或
	 *            OverallVariable.Update.DO_UPDATE
	 * @param bundle
	 *            如果需要展示更新信息，则需要传递更新包的信息过来，Bundle对象；如果不需要展示更新信息，则可以传null
	 */
	@SuppressLint({ "InflateParams", "NewApi" })
	public static void showUpdateVersionDialog(final Activity activity, int needUpdate, Bundle bundle) {
		View view = activity.getLayoutInflater().inflate(R.layout.my_dialog_update_layout, null);
		// 创建builder，传参Context，theme(style)的id
		// 创建自定义样式的Dialog，背景设置了透明，不然有黑色框框。设置了windowIsTranslucent，不然某些机型上UI会别截掉一部分
		// AlertDialog.Builder builder = new AlertDialog.Builder(this,
		// R.style.updateDialogStyle);
		AlertDialog.Builder builder = new AlertDialog.Builder(activity, 0);
		builder.setView(view);
		final AlertDialog dialog = builder.create();

		// 初始化更新窗UI和兼容性
		initUpdateUI(activity, view, dialog);

		// 显示更新窗的数据，并根据实际情况对UI微调
		showUpdateInformation(needUpdate, bundle, view);

		// 监听按钮
		view.findViewById(R.id.update_dialog_nextUpdate).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showCustomToast(activity, "下次更新");
				dialog.dismiss();
			}
		});
		if (needUpdate == OverallVariable.Update.DO_UPDATE) {
			final String updateUrl = bundle.getString("updateUrl");
			view.findViewById(R.id.update_dialog_exit).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ToastUtil.showCustomToast(activity, "退出游戏");
					dialog.dismiss();
					activity.onBackPressed();
				}
			});
			view.findViewById(R.id.update_dialog_updateNow).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mUpdateService.downloadUpdateApk(updateUrl);
					dialog.dismiss();
				}
			});
		}
		/** 如和让ScrollView的ScrollBar恒显示？ fadeScrollbars设置为false，见xml */
		/**
		 * AlertDialog的自定义view设置background后，AlertDialog会有黑色边框。
		 * 需要设置AlertDialog的theme才能去掉。
		 */
	}

	/**
	 * 设置更新弹窗的UI和兼容性
	 * 
	 * @param activity
	 * @param view
	 *            Layout对象
	 * @param dialog
	 *            创建的dialog
	 */
	private static void initUpdateUI(final Activity activity, View view, final AlertDialog dialog) {
		/**
		 * 在安卓4.4到5.0之间系统的手机上，AlertDialog的顶部可能被剪切掉，所以需要设置Flags
		 */
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.show();
		dialog.setCancelable(false);
		// 竖屏适配
		if (!AppUtil.isLandscape(activity)) {
			// 竖屏时限制高度为屏幕高度的一半，需要判断
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.height = (int) Math.ceil((activity.getResources().getDisplayMetrics().heightPixels) / 2);
			dialog.getWindow().setAttributes(lp);
			// 该表UI字体适配布局
			((Button) view.findViewById(R.id.update_dialog_exit)).setTextSize(12);
			((Button) view.findViewById(R.id.update_dialog_nextUpdate)).setTextSize(12);
			((Button) view.findViewById(R.id.update_dialog_updateNow)).setTextSize(12);
			((TextView) view.findViewById(R.id.update_dialog_explain1)).setTextSize(9);
			((TextView) view.findViewById(R.id.update_dialog_explain2)).setTextSize(9);
			((TextView) view.findViewById(R.id.update_dialog_explain3)).setTextSize(9);
		}
	}

	/**
	 * 显示更新窗的数据，并根据实际情况对UI微调
	 * 
	 * @param needUpdate
	 * @param bundle
	 * @param view
	 */
	private static void showUpdateInformation(int needUpdate, Bundle bundle, View view) {
		// 版本展示textView
		((TextView) view.findViewById(R.id.update_dialog_currentVersion)).setText("当前版本：" + PropertiesUtil.getVersion());
		// 内容展示textView
		TextView contentTv = (TextView) view.findViewById(R.id.update_dialog_textView);
		if (needUpdate == OverallVariable.Update.DO_UPDATE) {
			if (bundle == null) {
				return;
			}
			String version = bundle.getString("version");
			// 传过来的换行符转义了，需要还原
			String content = bundle.getString("content").replace("\\n", "\n");
			String apkSize = bundle.getString("apkSize");
			String updateDate = bundle.getString("updateDate");
			// 设置显示的UI内容
			contentTv.setText(content);
			((TextView) view.findViewById(R.id.update_dialog_explain1)).setText("更新日期：" + updateDate);
			((TextView) view.findViewById(R.id.update_dialog_explain2)).setText("更新大小：" + apkSize);
			((TextView) view.findViewById(R.id.update_dialog_explain3)).setText("版本号：" + version);
		} else {
			// 隐藏更新按钮和新版本信息栏
			view.findViewById(R.id.update_dialog_relativelayout1).setVisibility(View.GONE);
			view.findViewById(R.id.update_dialog_exit).setVisibility(View.GONE);
			((Button) view.findViewById(R.id.update_dialog_nextUpdate)).setText("确定");
			view.findViewById(R.id.update_dialog_updateNow).setVisibility(View.GONE);
			// 设置显示的UI内容
			((TextView) view.findViewById(R.id.update_dialog_title)).setText("已是最新版本");
			contentTv.setText(PropertiesUtil.getValue("updateContent"));
		}
	}

	/**
	 * 显示系统退出弹窗，这里用AlertDialog
	 */
	public static void showSystemExitDialog(final Activity activity) {
		AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("确认退出吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				for (Activity activity : MyApplication.activitys) {
					activity.finish();
				}
				MyApplication.destroy(activity);
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
	 * 显示修改的系统退出弹窗(这里是Dialog，而非AlertDialog)
	 */
	@SuppressLint("NewApi")
	public static void showSystemDialog(Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Dialog_MyTheme);
		builder.setMessage("Hello World").setTitle("Alert Dialog").setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 显示自定义退出弹窗，这里使用Dialog而非AlertDialog，因为使用AlertDialog，会造成圆角出现尖角问题(白色或黑色尖角)
	 * 这里用到了showGif.jar显示动态图(GifImageView对象展示)
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	public static void showSimpleCustomExitDialog(final Activity activity) {
		View view = activity.getLayoutInflater().inflate(R.layout.my_dialog_exit_layout, null);
		final Dialog dialog = new Dialog(activity, R.style.exitDialogStyle);
		// 显示动态图片
		GifImageView gifIV = (GifImageView) view.findViewById(R.id.exit_GifImageView);
		gifIV.setVisibility(View.VISIBLE);
		gifIV.setScaleType(ScaleType.FIT_XY);
		try {
			gifIV.setBackgroundDrawable(new GifDrawable(activity.getAssets(), "run.gif"));
		} catch (IOException e) {
			MyLog.e("setGifImageView exception:" + e.getMessage());
		}
		dialog.setContentView(view);
		dialog.setCancelable(true);
		/**
		 * 在安卓4.4到5.0之间系统的手机上，AlertDialog的顶部可能被剪切掉，所以需要设置Flags
		 */
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
				MyApplication.destroy(activity);
				System.exit(0);
			}
		});
	}

	/**
	 * 自定义类MyCustomDialog，继承了Dialog 这里用到了WebView显示动态图
	 * 
	 * @param activity
	 */
	public static void showCustemDialogForExit(final Activity activity) {
		MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(activity);
		customBuilder.setTitle("退出").setContentWebView("run.gif").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				for (Activity activity : MyApplication.activitys) {
					activity.finish();
				}
				MyApplication.destroy(activity);
				System.exit(0);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		MyCustomDialog dialog = customBuilder.create();
		dialog.show();
	}

	/**
	 * 显示PopupMenu，优点：在不需要自定义background时使用方便简洁；缺点：不能自定义background，这里未使用
	 * 
	 * @param activity
	 * @param v
	 *            PopupMenu绑定的view
	 */
	public static void showPopupMenu(final Activity activity, final Handler handler, View v) {
		final PopupMenu pm = new PopupMenu(activity, v);
		pm.getMenuInflater().inflate(R.menu.my_pop, pm.getMenu());
		pm.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.main_pop1:
					// 自定义AlertDialog，更新弹窗
					if (mUpdateService == null) {
						mUpdateService = new UpdateService(activity, handler);
					}
					mUpdateService.checkUpdateInformation();
					pm.dismiss();
					break;
				case R.id.main_pop2:
					// 系统退出弹窗
					AlertDialogService.showSystemExitDialog(activity);
					break;
				case R.id.main_pop3:
					// 测试style的系统退出窗口
					AlertDialogService.showSystemDialog(activity);
					break;
				case R.id.main_pop4:
					// 自定义退出弹窗1
					AlertDialogService.showSimpleCustomExitDialog(activity);
					break;
				case R.id.main_pop5:
					// 自定义退出弹窗2
					AlertDialogService.showCustemDialogForExit(activity);
					break;
				}
				return false;
			}
		});
		pm.show();
	}

	/**
	 * 显示PopupWindow，可以自定义background
	 * 
	 * @param activity
	 * @param v
	 *            承载的父控件 PopupWindow绑定的view
	 */
	@SuppressLint("InflateParams")
	public static void showPopupWindow(final Activity activity, final Handler handler, View v) {
		View view = activity.getLayoutInflater().inflate(R.layout.my_dialog_popwindow_layout, null);
		final PopupWindow pw = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		// 必须在代码中设置一下背景色，点击外面不会隐藏此弹窗
		pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		// Focusable 为False时，不执行则点击外面不会隐藏此弹窗
		pw.setOutsideTouchable(true);
		// 显示 默认显示View的正下方，xoff表示x轴的偏移，正值表示向左，负值表示向右；yoff表示相对y轴的偏移，正值是向下，负值是向上
		// pw.showAsDropDown(v, 0, 0);
		// 相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
		pw.showAtLocation(v, Gravity.CENTER, 0, 0);

		// 设置点击监听
		((Button) view.findViewById(R.id.popwindow_bt1)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 自定义AlertDialog，更新弹窗
				if (mUpdateService == null) {
					mUpdateService = new UpdateService(activity, handler);
				}
				mUpdateService.checkUpdateInformation();
				pw.dismiss();
			}
		});
		((Button) view.findViewById(R.id.popwindow_bt2)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 系统退出弹窗
				AlertDialogService.showSystemExitDialog(activity);
				pw.dismiss();
			}
		});
		((Button) view.findViewById(R.id.popwindow_bt3)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 测试style的系统退出窗口
				AlertDialogService.showSystemDialog(activity);
				pw.dismiss();
			}
		});
		((Button) view.findViewById(R.id.popwindow_bt4)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 自定义退出弹窗1
				AlertDialogService.showSimpleCustomExitDialog(activity);
				pw.dismiss();
			}
		});
		((Button) view.findViewById(R.id.popwindow_bt5)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 自定义退出弹窗2
				AlertDialogService.showCustemDialogForExit(activity);
				pw.dismiss();
			}
		});
	}

	/**
	 * 显示Loading UI
	 * 
	 * @param _activity
	 *            显示于哪个activity
	 * @param title
	 *            显示文字
	 * @param cancelable
	 *            是否能被触摸外边取消
	 * */
	static public void showLoading(final Activity _activity, final String title, final boolean cancelable) {
		_activity.runOnUiThread(new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (null != loadingBar && loadingBar.isShowing()) {
					return;
				}
				loadingBar = new Dialog(_activity);
				loadingBar.requestWindowFeature(Window.FEATURE_NO_TITLE);
				loadingBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

				int height = AppUtil.dp2px(_activity, 120);
				int width = AppUtil.dp2px(_activity, 100);

				LinearLayout mLinearLayout = new LinearLayout(_activity);
				LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(width, height);
				mLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;

				GradientDrawable shape = new GradientDrawable();
				shape.setCornerRadius(AppUtil.dp2px(_activity, 5));
				shape.setColor(Color.parseColor(OverallVariable.Color.DIALOG_TRANSPARENT_BLACK));

				mLinearLayout.setBackgroundDrawable(shape);
				mLinearLayout.setLayoutParams(mLayoutParams);
				mLinearLayout.setOrientation(LinearLayout.VERTICAL);

				ProgressBar progressBar = new ProgressBar(_activity);
				int top = AppUtil.dp2px(_activity, 10);
				int pbWH = AppUtil.dp2px(_activity, 60);
				LinearLayout.LayoutParams mgParams = new LinearLayout.LayoutParams(pbWH, pbWH);
				mgParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
				mgParams.setMargins(0, top, 0, 0);
				progressBar.setLayoutParams(mgParams);

				TextView textView = new TextView(_activity);
				textView.setText(title);
				textView.setTextSize(16);
				textView.setTextColor(Color.WHITE);
				textView.setGravity(Gravity.CENTER);

				mLinearLayout.addView(progressBar);
				mLinearLayout.addView(textView);

				loadingBar.setContentView(mLinearLayout, mLayoutParams);
				loadingBar.setCancelable(cancelable);
				try {
					loadingBar.show();
				} catch (Exception e) {
					MyLog.error(TAG, "showLoading", "showLoading error", e);
				}
			}
		});
	}

	static public void hideLoading(final Activity _activity) {
		_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (null != loadingBar && loadingBar.isShowing()) {
					try {
						loadingBar.dismiss();
						loadingBar = null;
					} catch (IllegalArgumentException e) {
						MyLog.error(TAG, "hideLoading", "hideLoading error", e);
					} catch (Exception e) {
						MyLog.error(TAG, "hideLoading", "hideLoading error", e);
					}
				}
			}
		});
	}
}
