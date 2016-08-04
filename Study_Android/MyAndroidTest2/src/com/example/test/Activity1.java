package com.example.test;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.example.util.ImageAdapter;
import com.example.util.ImageHandler;

public class Activity1 extends Activity {

	public ImageHandler handler = new ImageHandler(new WeakReference<Activity1>(this));
	public ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity1);
		viewPager = (ViewPager) findViewById(R.id.activity1_viewPager);

		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ImageView view1 = new ImageView(this);
		view1.setLayoutParams(lp);
		ImageView view2 = new ImageView(this);
		view2.setLayoutParams(lp);
		ImageView view3 = new ImageView(this);
		view3.setLayoutParams(lp);
		view1.setImageResource(R.drawable.viewpager_1);
		view2.setImageResource(R.drawable.viewpager_2);
		view3.setImageResource(R.drawable.viewpager_3);
		ArrayList<ImageView> views = new ArrayList<ImageView>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		viewPager.setAdapter(new ImageAdapter(views));
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			// 配合Adapter的currentItem字段进行设置。
			@Override
			public void onPageSelected(int arg0) {
				handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			// 覆写该方法实现轮播效果的暂停和恢复
			@Override
			public void onPageScrollStateChanged(int arg0) {
				switch (arg0) {
				case ViewPager.SCROLL_STATE_DRAGGING:
					handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
					break;
				case ViewPager.SCROLL_STATE_IDLE:
					handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
					break;
				default:
					break;
				}
			}
		});
		viewPager.setCurrentItem(Integer.MAX_VALUE / 2);// 默认在中间，使用户看不到边界
		// 开始轮播效果
		handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.activity1_button1:
			finish();
			break;

		case R.id.activity1_button2:
			startActivity(new Intent(this, Activity2.class));
			break;
		}
	}
}
