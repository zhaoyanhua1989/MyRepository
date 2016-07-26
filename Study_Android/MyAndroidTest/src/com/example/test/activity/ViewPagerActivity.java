package com.example.test.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

import com.example.test.MyApplication;
import com.example.test.adapter.ViewPagerAdapter;
import com.example.test.R;
import com.example.test.util.MyLog;
import com.example.test.view.MyCircle;

public class ViewPagerActivity extends Activity implements OnPageChangeListener {

	private MyCircle myCircle;
	private int[] mImages = new int[] { R.drawable.viewpager_1, R.drawable.viewpager_2, R.drawable.viewpager_3,
			R.drawable.viewpager_4, -1 };
	private ViewPager viewPager;
	private int currentPage; // viewPager的当前页码
	private Thread pagingThread; // 用于发送viewPager翻页的msg的线程
	private boolean flag = true; // 用于判断viewPager翻页的线程是否继续工作
	@SuppressLint("HandlerLeak")
	Handler h = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			viewPager.setCurrentItem(msg.arg1);
		};
	};

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
		// 实现viewPager的自动跳转
		paging();
	}

	private void showViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewPager_viewPager);
		viewPager.setAdapter(new ViewPagerAdapter(this, mImages));
		viewPager.setOnPageChangeListener(this);
		// 添加触摸监听，在点击时，线程暂停3秒
		viewPager.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				viewPagerClicked();
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
		myCircle.setViePagerChangeByBollListener(viewPager);
	}

	// 手动滑动页面和Thread导致页面自动翻滚时，都会执行，无法判断是否手动翻页
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	// 手动滑动页面和Thread导致页面自动翻滚时，都会执行，无法判断是否手动翻页
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		myCircle.reDraw(position, positionOffset);
		// 当手动滑动页面时从新设定当前自动翻译的页面位置
		currentPage = position;
	}

	// 手动滑动页面和Thread导致页面自动翻滚时，都会执行，无法判断是否手动翻页
	@Override
	public void onPageSelected(int position) {
		MyLog.d("onPageSelected");
	}

	// viewPager的自动翻页
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
					MyLog.d("currentPage=" + currentPage);
					if (currentPage > mImages.length - 1)
						currentPage = 0;
					Message msg = Message.obtain();
					msg.arg1 = currentPage;
					h.sendMessage(msg);
				}
			}
		}, "pagingThread");
		pagingThread.start();
	}

	public void setCurrentPage(int changePage) {
		currentPage = changePage;
	}

	public void viewPagerClicked() {
		flag = true;
	}

}
