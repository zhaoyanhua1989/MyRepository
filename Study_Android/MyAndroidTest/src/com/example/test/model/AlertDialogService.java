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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ImageView.ScaleType;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.example.test.MyApplication;
import com.example.test.R;
import com.example.test.util.MyLog;
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

	/**
	 * 直接使用AlertDialog，有缺陷，设置background时，当设置圆角时有尖角
	 * @param activity
	 */
	@SuppressLint({ "InflateParams", "NewApi" })
	public static void showUpdateDialog(final Activity activity) {
		View view = activity.getLayoutInflater().inflate(R.layout.dialog_update_layout, null);
		// 创建builder，传参Context，theme(style)的id
		// 创建自定义样式的Dialog，背景设置了透明，不然有黑色框框。设置了windowIsTranslucent，不然某些机型上UI会别截掉一部分
		// AlertDialog.Builder builder = new AlertDialog.Builder(this,
		// R.style.updateDialogStyle);
		AlertDialog.Builder builder = new AlertDialog.Builder(activity, 0);
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
		if (!ScreenUtil.isLandscape(activity)) {
			// 竖屏时限制高度为屏幕高度的一半，需要判断
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.height = (int) Math.ceil((activity.getResources().getDisplayMetrics().heightPixels) / 2);
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
				ToastUtil.showToast(activity, "退出游戏");
				dialog.dismiss();
				activity.onBackPressed();
			}
		});
		view.findViewById(R.id.update_dialog_nextUpdate).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showToast(activity, "下次更新");
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.update_dialog_updateNow).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showToast(activity, "立即更新");
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
	 * 显示系统退出弹窗，这里用AlertDialog
	 */
	public static void showSystemExitDialog(final Activity activity) {
		AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("确认退出吗")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
		builder.setMessage("Hello World").setTitle("Alert Dialog").setIcon(android.R.drawable.ic_dialog_alert)
				.setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener() {
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
	@SuppressLint("InflateParams")
	public static void showSimpleCustomExitDialog(final Activity activity) {
		View view = activity.getLayoutInflater().inflate(R.layout.dialog_exit_layout, null);
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
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
	 * 自定义类MyCustomDialog，继承了Dialog
	 * 这里用到了WebView显示动态图
	 * @param activity
	 */
	public static void showCustemDialogForExit(final Activity activity) {
		MyCustomDialog.Builder customBuilder = new MyCustomDialog.Builder(activity);
		customBuilder.setTitle("退出").setContentWebView("run.gif")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						for (Activity activity : MyApplication.activitys) {
							activity.finish();
						}
						MyApplication.destroy(activity);
						System.exit(0);
					}
				});
		MyCustomDialog dialog = customBuilder.create();
		dialog.show();
	}
	
	/**
	 * 显示PopupMenu，优点：在不需要自定义background时使用方便简洁；缺点：不能自定义background
	 * @param activity
	 * @param v PopupMenu绑定的view
	 */
	public static void showPopupMenu(final Activity activity, View v){
		PopupMenu pm = new PopupMenu(activity, v);
		pm.getMenuInflater().inflate(R.menu.pop, pm.getMenu());
		pm.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.main_pop1:
					// 自定义AlertDialog，更新弹窗
					AlertDialogService.showUpdateDialog(activity);
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
	 * @param activity
	 * @param v PopupWindow绑定的view
	 */
	@SuppressLint("InflateParams")
	public static void showPopupWindow(final Activity activity, View v) {
		View view = activity.getLayoutInflater().inflate(R.layout.dialog_popwindow_layout, null);
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
		((Button)view.findViewById(R.id.popwindow_bt1)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 自定义AlertDialog，更新弹窗
				AlertDialogService.showUpdateDialog(activity);
				pw.dismiss();
			}
		});
		((Button)view.findViewById(R.id.popwindow_bt2)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 系统退出弹窗
				AlertDialogService.showSystemExitDialog(activity);
				pw.dismiss();
			}
		});
		((Button)view.findViewById(R.id.popwindow_bt3)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 测试style的系统退出窗口
				AlertDialogService.showSystemDialog(activity);
				pw.dismiss();
			}
		});
		((Button)view.findViewById(R.id.popwindow_bt4)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 自定义退出弹窗1
				AlertDialogService.showSimpleCustomExitDialog(activity);
				pw.dismiss();
			}
		});
		((Button)view.findViewById(R.id.popwindow_bt5)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 自定义退出弹窗2
				AlertDialogService.showCustemDialogForExit(activity);
				pw.dismiss();
			}
		});
	}

}
