package com.example.test.adapter;

import java.lang.ref.WeakReference;

import com.example.test.activity.ViewPagerActivity;
import com.example.test.util.MyLog;

import android.os.Handler;
import android.os.Message;

public class ViewPagerHandler extends Handler{

	/**
	 * 不推荐、不完善的padding方法的翻页消息
	 */
	public static final int MSG_OLD_PADDING = 0;
	/**
     * 请求更新显示的View
     */
    public static final int MSG_UPDATE_IMAGE = 1;
    /**
     * 请求暂停轮播
     */
    public static final int MSG_KEEP_SILENT = 2;
    /**
     * 请求恢复轮播
     */
    public static final int MSG_BREAK_SILENT = 3;
    /**
     * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
     * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
     * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
     */
    public static final int MSG_PAGE_CHANGED = 4;
    /**
     * 翻页时间间隔
     */
    public static final long MSG_DELAY = 3000;
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
    	 if (mActivity==null){
             //Activity已经回收，无需再处理UI了
             return ;
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
			break;
		case MSG_KEEP_SILENT:
			MyLog.d("请求暂停轮播");
			break;
		case MSG_BREAK_SILENT:
			MyLog.d("请求恢复轮播");
			break;
		case MSG_PAGE_CHANGED:
			MyLog.d("记录当前的页号");
			break;
		}
    }
}
