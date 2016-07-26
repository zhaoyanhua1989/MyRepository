package com.example.test.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.util.ScreenUtil;
import com.example.test.util.ToastUtil;

public class MyAlertDialog {
	
	private Activity mActivity;

	public MyAlertDialog(Activity mActivity) {
		this.mActivity = mActivity;
	}

	/***************************自定义AlertDialog*******************************/
	@SuppressLint("InflateParams")
	public void showMyAlertDialog() {
		View view = mActivity.getLayoutInflater().inflate(R.layout.new_update_dialog, null);
		// 创建builder，传参Context，theme(style)的id
		// 创建自定义样式的Dialog，背景设置了透明，不然又黑色框框。设置了windowIsTranslucent，不然某些机型上UI会别截掉一部分
		// AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.updateDialogStyle);
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, 0);
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		/**
		 * 在安卓4.4到5.0之间系统的手机上，AlertDialog的顶部可能被剪切掉，所以需要设置Flags
		 */
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.show();
		dialog.setCancelable(false);
		//竖屏适配
		if (!ScreenUtil.isLandscape(mActivity)) {
			// 竖屏时限制高度为屏幕高度的一半，需要判断
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.height = (int) Math.ceil((mActivity.getResources().getDisplayMetrics().heightPixels) / 2);
			dialog.getWindow().setAttributes(lp);
			//该表UI字体适配布局
			((Button)(view.findViewById(R.id.update_dialog_exit))).setTextSize(12);;
			((Button)(view.findViewById(R.id.update_dialog_nextUpdate))).setTextSize(12);;
			((Button)(view.findViewById(R.id.update_dialog_updateNow))).setTextSize(12);;
			((TextView)(view.findViewById(R.id.update_dialog_explain1))).setTextSize(9);
			((TextView)(view.findViewById(R.id.update_dialog_explain2))).setTextSize(9);
			((TextView)(view.findViewById(R.id.update_dialog_explain3))).setTextSize(9);
		}
		// 监听按钮
		view.findViewById(R.id.update_dialog_exit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showToast(mActivity, "退出游戏");
				mActivity.onBackPressed();
			}
		});
		view.findViewById(R.id.update_dialog_nextUpdate).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showToast(mActivity, "下次更新");
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.update_dialog_updateNow).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showToast(mActivity, "立即更新");
				dialog.dismiss();
			}
		});
		// 设置内容
//		TextView textView = (TextView) view.findViewById(R.id.update_dialog_textView);
		// textView.setText("");
		/** 如和让ScrollView的ScrollBar恒显示？ fadeScrollbars设置为false，见xml */
		/** AlertDialog的自定义view设置background后，AlertDialog会有黑色边框。需要设置AlertDialog的theme才能去掉。 */
	}
	
}
