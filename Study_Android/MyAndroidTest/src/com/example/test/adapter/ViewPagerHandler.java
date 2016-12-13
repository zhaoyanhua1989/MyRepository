package com.example.test.adapter;

import java.lang.ref.WeakReference;

import com.example.test.activity.ViewPagerActivity;
import com.example.test.util.MyLog;

import android.os.Handler;
import android.os.Message;

public class ViewPagerHandler extends Handler {

	/**
	 * 不推荐、不完善的padding方法的翻页消息
	 */
	public static final int MSG_OLD_PADDING = 0;
	/**
	 * 请求更新显示的View
	 */
	public static final int MSG_UPDATE_IMAGE = 1;
	/**
	 * 翻页时间间隔
	 */
	public static final long MSG_DELAY = 3000;
	/**
	 * 当手动点击时的翻页时间间隔，会延长
	 */
	public static final long MSG_DELAY_LONG = 5000;
	/**
	 * 使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
	 */
	private WeakReference<ViewPagerActivity> weakReference;

	public ViewPagerHandler(WeakReference<ViewPagerActivity> wk) {
		weakReference = wk;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		ViewPagerActivity mActivity = weakReference.get();
		if (mActivity == null) {
			// Activity已经回收，无需再处理UI了
			return;
		}
		switch (msg.what) {
		case MSG_OLD_PADDING:
			MyLog.i("padding请求翻页");
			mActivity.viewPager.setCurrentItem(msg.arg1);
			// 翻页时通知改变Fragment
			mActivity.replaceFrg(!mActivity.fragmentFlag);
			break;
		case MSG_UPDATE_IMAGE:
			MyLog.d("请求更新显示的View");
			mActivity.currentPage++;
			// 如果当前页面的页数大于页面总数，则置零
			if (mActivity.currentPage > mActivity.mImages.length - 1) {
				mActivity.h.removeMessages(MSG_UPDATE_IMAGE);
				mActivity.currentPage = 0;
			}
			mActivity.viewPager.setCurrentItem(mActivity.currentPage);
			// 每次翻页完了就清空消息一次，不然可能会出现连着翻两页的情况
			mActivity.h.removeMessages(MSG_UPDATE_IMAGE);
			// 准备下一次翻页
			mActivity.h.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
			break;
		}
	}
}
