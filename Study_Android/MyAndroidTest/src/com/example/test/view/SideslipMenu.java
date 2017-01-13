package com.example.test.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.test.R;
import com.example.test.util.RUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SideslipMenu {

	private Button[] btnArray;
	private SlidingMenu slidingMenu;
	private int customBtAmount;

	/**
	 * 显示侧滑菜单
	 * 
	 * @param activity
	 * @param amount
	 *            需要几个按钮显示，最多设置10个
	 * @param sideslipMenuListener
	 *            侧滑菜单的按钮按键监听
	 * @throws SideslipMenuBottonOutOfLimits
	 *             侧滑菜单显示的按钮超过了最大个数
	 */
	public SideslipMenu(Activity activity, int amount, SideslipMenuListener sideslipMenuListener) throws SideslipMenuBottonOutOfLimits {
		if (amount > 20) {
			throw new SideslipMenuBottonOutOfLimits();
		}
		customBtAmount = amount;
		btnArray = new Button[amount];
		slidingMenu = new SlidingMenu(activity);
		slidingMenu.setMenu(R.layout.sideslip_leftmenu);
		slidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
		// 测试侧滑菜单的宽高
		int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
		slidingMenu.setBehindOffset(screenWidth * 3 / 8);
		// 设置渐入渐出效果的值
		slidingMenu.setFadeDegree(0.35f);
		// 设置侧滑菜单按钮的监听
		for (int i = 0; i < amount; i++) {
			btnArray[i] = (Button) activity.findViewById(RUtil.getId(activity, "sideslip_leftmenu_bt" + i));
		}
		SideslipMenuBtListener myListener = new SideslipMenuBtListener(activity, sideslipMenuListener);
		for (Button btn : btnArray) {
			btn.setOnClickListener(myListener);
		}
	}

	public void setSideslipMenuBtName(String[] strs) throws SetSideslipMenuBtNameQuantityError {
		if (strs.length != customBtAmount) {
			throw new SetSideslipMenuBtNameQuantityError();
		}
		Button chacheBt;
		for (int i = 0; i < customBtAmount; i++) {
			chacheBt = btnArray[i];
			chacheBt.setText(strs[i]);
		}
		chacheBt = null;
	}

	public void showMenu() {
		slidingMenu.showMenu();
	}

	class SideslipMenuBtListener implements OnClickListener {

		private SideslipMenuListener mlistener;
		private Context mContext;

		public SideslipMenuBtListener(Context context, SideslipMenuListener sideslipMenuListener) {
			mContext = context;
			mlistener = sideslipMenuListener;
		}

		@Override
		public void onClick(View v) {
			String idName = RUtil.getIdName(mContext, v.getId());
			Integer.parseInt(idName.substring(idName.lastIndexOf("t")));
			mlistener.onSideslipMenuClicked(Integer.parseInt(idName.substring(idName.lastIndexOf("t"))));
		}
	}

	/**
	 * 侧滑菜单按钮的点击回调
	 * 
	 * @author HKW2962
	 */
	public abstract static class SideslipMenuListener {
		protected abstract void onSideslipMenuClicked(int index);
	}

	/**
	 * 侧滑菜单显示的按钮超过了最大个数
	 * 
	 * @author HKW2962
	 */
	public static class SideslipMenuBottonOutOfLimits extends Exception {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * 设置侧滑菜单button的name，传入的name[]{}数量和定义的button数量不一致
	 * 
	 * @author HKW2962
	 *
	 */
	public static class SetSideslipMenuBtNameQuantityError extends Exception {
		private static final long serialVersionUID = 1L;
	}
}