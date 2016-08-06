package com.example.test.activity;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

import com.example.test.MyApplication;
import com.example.test.adapter.Fragment1;
import com.example.test.adapter.Fragment2;
import com.example.test.adapter.ViewPagerAdapter;
import com.example.test.adapter.ViewPagerHandler;
import com.example.test.R;
import com.example.test.util.MyLog;
import com.example.test.view.MyCircle;

public class ViewPagerActivity extends FragmentActivity implements OnPageChangeListener {

	private MyCircle myCircle;
	public int[] mImages = new int[] { R.drawable.viewpager_1, R.drawable.viewpager_2, R.drawable.viewpager_3,
			R.drawable.viewpager_4, -1 };
	public ViewPager viewPager;
	public int currentPage; // viewPager的当前页码
	private Thread pagingThread; // 用于发送viewPager翻页的msg的线程
	private boolean flag = true; // 用于判断viewPager翻页的线程是否继续工作
	public ViewPagerHandler h = new ViewPagerHandler(new WeakReference<ViewPagerActivity>(this));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view_pager);
		MyApplication.activitys.add(this);
		// 初始化viewPager
		showViewPager();
		// 画圆
		showBolls();

		// 实现viewPager的自动跳转，以下两种方式
		// 1.通过线程，不推荐，不好控制
		// paging();
		// 2.完全通过Handler实现
		doPaging();

		// 设置左边和下边的Fragment
		addFragment();
	}

	private void showViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewPager_viewPager);

		// 设置Adapter，3种方式：
		// 1.Adapter继承自PagerAdapter
		viewPager.setAdapter(new ViewPagerAdapter(this, mImages));
		// 2.Adapter继承自FragmentPagerAdapter，需要Activity继承FragmengActivity
		// viewPager.setAdapter(new ViewPagerAdapter2(this, mImages,
		// getSupportFragmentManager()));
		// 3.Adapter继承自FragmentStatePagerAdapter，需要Activity继承FragmengActivity
		// viewPager.setAdapter(new ViewPagerAdapter3(this, mImages,
		// getSupportFragmentManager()));

		viewPager.setOnPageChangeListener(this);
		// 添加触摸监听，在点击时，线程暂停3秒
		viewPager.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				viewPagerClicked(); // 这里适配paging()方法，如果是doPaging()，这可以不设置触摸监听
				// 这里不能返回true，不然会导致无法触发手指触摸翻页
				return false;
			}
		});
	}

	private void showBolls() {
		// 画圆
		myCircle = (MyCircle) findViewById(R.id.viewPager_myCircle);
		myCircle.setContext(this);
		myCircle.setCount(mImages.length);
	}

	// 手动滑动页面、页面自动翻页、小球点击变化后页面翻页，都会执行2次(模拟按下时，松开时)，无法判断是否手动翻页
	@Override
	public void onPageScrollStateChanged(int state) {
		// 下面的逻辑匹配doPaging的翻页方法，不匹配paging
		switch (state) {
		case ViewPager.SCROLL_STATE_DRAGGING: // 当手指按下时，暂停翻页
			// 将现有的翻页消息清除
			h.removeMessages(ViewPagerHandler.MSG_UPDATE_IMAGE);
			break;
		case ViewPager.SCROLL_STATE_IDLE: // 当手指松开时，恢复翻页，但是页面停留时间增加
			h.sendEmptyMessageDelayed(ViewPagerHandler.MSG_UPDATE_IMAGE, ViewPagerHandler.MSG_DELAY_LONG);
			break;
		default:
			break;
		}
	}

	// 手动滑动页面、页面自动翻页、小球点击变化后页面翻页，都会执行多次，无法判断是否手动翻页，可以计算偏移量。
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		myCircle.reDraw(position, positionOffset);
		// 当手动滑动页面时从新设定当前自动翻译的页面位置
	}

	// 手动滑动页面、页面自动翻页、小球点击变化后页面翻页，都会执行一次，无法判断是否手动翻页
	@Override
	public void onPageSelected(int position) {
		MyLog.d("ViewPagerActivity, onPageSelected...");
		MyLog.i("onPageSelected：position="+position+",currentPage="+currentPage+",viewPagerPage="+viewPager.getCurrentItem());
		synchronized (ViewPagerActivity.class) {
			currentPage = position;
		}
		// 翻页时(不管是手动导致还是小球点击后变化导致，都走这里)，通知改变Fragment
		replaceFrg(!fragmentFlag);
	}

	// viewPager的自动翻页
	@SuppressWarnings("unused")
	private void paging() {
		pagingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					/*
					 * 注意，这里不能直接把postDelayed放在while循环里，postDelayed不会让线程休眠，
					 * 循环会一直执行postDelayed，2秒一到，会一直有handler陆续发送消息
					 */
					// h.postDelayed(new Runnable() {
					// @Override
					// public void run() {
					// currentPage++;
					// if (currentPage > mImages.length)
					// currentPage = 0;
					// Message msg = Message.obtain();
					// msg.arg1 = currentPage;
					// h.sendMessage(msg);
					// }
					// }, 2000);

					try {
						Thread.sleep(2000);
					} catch (Exception e) {
					}
					// 如果手动滑动页面，或者选中页面，会暂停一下
					if (flag) {
						try {
							Thread.sleep(4000);
						} catch (Exception e) {
						}
						flag = false;
					}
					currentPage++;
					MyLog.d("VIewPagerActivity, currentPage=" + currentPage);
					if (currentPage > mImages.length - 1)
						currentPage = 0;
					Message msg = Message.obtain();
					msg.what = ViewPagerHandler.MSG_OLD_PADDING;
					msg.arg1 = currentPage;
					h.sendMessage(msg);
				}
			}
		}, "pagingThread");
		pagingThread.start();
	}
	
	private void doPaging() {
		h.sendEmptyMessageDelayed(ViewPagerHandler.MSG_UPDATE_IMAGE, ViewPagerHandler.MSG_DELAY);
	}

	public void viewPagerClicked() {
		flag = true;
	}

	// 当下标小球被点击选中时，通知viewPager翻页
	public void notifyPaging(int position) {
		MyLog.d("ViewPagerActivity, notifyPaging...");
		// 当选中小球改变viewPager页码时，从新设定当前自动翻译的页面位置
		currentPage = position;
		// 选中小球，改变viewPager页码
		viewPager.setCurrentItem(position);
		
		// 选中小球改变了页面时，页面停留时间增加
		// 当选中小球改变viewPager页码时，先终止翻页线程休眠一段时间后重新开始，该方法匹配paging
		viewPagerClicked();
		// 当选中小球改变viewPager页码时，先清除正在执行的翻页消息，然后发送延迟时间增加的翻页消息,该方法匹配doPaging
		h.removeMessages(ViewPagerHandler.MSG_UPDATE_IMAGE);
		h.sendEmptyMessageDelayed(ViewPagerHandler.MSG_UPDATE_IMAGE, ViewPagerHandler.MSG_DELAY_LONG);
	}

	/*************************以下是不完善的Fragment案例*************************/
	
	private Fragment1 frg1;
	private Fragment2 frg2;
	public boolean fragmentFlag;

	private void addFragment() {
		frg1 = new Fragment1();
		frg2 = new Fragment2();
		replaceFrg(!fragmentFlag);
	}

	// 动态更新Fragment
	public void replaceFrg(boolean flag) {
		MyLog.e("before fragmentFlag ="+fragmentFlag);
		fragmentFlag = flag;
		MyLog.e("after fragmentFlag ="+fragmentFlag);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if (!fragmentFlag) {
			ft.replace(R.id.viewPager_FragmentLL_left, frg1);
			// ft.replace(R.id.viewPager_FragmentLL_bottom, frg);
		} else {
			ft.replace(R.id.viewPager_FragmentLL_left, frg2);
		}
		ft.commit();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		h.removeMessages(ViewPagerHandler.MSG_UPDATE_IMAGE);
		h = null;
	}

}
