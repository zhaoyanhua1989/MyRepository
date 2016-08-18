package com.example.test.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.model.CompressService;
import com.example.test.util.MyLog;

/**
 * ViewPager的Adapter，继承自FragmentPagerAdapter，要求调用的Activity继承FragmentActivity。
 * 如果ViewPager中需要自定义布局
 * ，且ViewPager页面不多，则建议使用这个Adapter。ViewPager在显示时，实际上是显示Fragment布局。
 * 缺点：所有被用户访问过的页面会被缓存进内存，所以页面多不建议使用。
 * 
 * @author HKW2962
 *
 */
public class ViewPagerAdapter2 extends FragmentPagerAdapter {

	private Activity mActivity;
	private int[] mImages;

	public ViewPagerAdapter2(Activity activity, int[] mImages, FragmentManager fm) {
		super(fm);
		this.mActivity = activity;
		this.mImages = mImages;
	}

	public ViewPagerAdapter2(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return mImages.length;
	}

	@Override
	public Fragment getItem(int position) {
		MyLog.d("页面翻页，滑动position=" + position);
		viewPagerFragment fragment = new viewPagerFragment(mImages[position]);
		return fragment;
	}

	/**
	 * A simple {@link android.support.v4.app.Fragment} subclass.
	 * 
	 */
	class viewPagerFragment extends Fragment {

		private int mImage;

		public viewPagerFragment(int mImage) {
			this.mImage = mImage;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_viewpager_activity_item, container,
					false);
			// 当设置第5个位置时(viewPager从0开始数)，显示其他图片
			if (mImage == -1) {
				// 由于container单独添加view会自动填满，所以需要一个父控件来控制自定义图片，防止拉伸。
				LinearLayout ll = new LinearLayout(mActivity);
				ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				ll.setGravity(Gravity.CENTER);
				// 自定义图片
				TextView textView = new TextView(mActivity);
				textView.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
				textView.setText("7");
				textView.setTextSize(50F);
				textView.setTextColor(Color.WHITE);
				textView.setGravity(Gravity.CENTER);
				textView.setBackgroundResource(R.drawable.viewpager_textview_shape);
				ll.addView(textView);
				layout.addView(ll);
				return layout;
			}
			ImageView imageView = (ImageView) layout.findViewById(R.id.fragment_viewPagerActivity_ImageView);
			// 这里最大化的缩小图片尺寸，以节约内存，尺寸缩小为原来的一半
			imageView.setImageBitmap(CompressService.compressAssign(mActivity, mImage, 240, 400));
			return layout;
		}
	}

}
