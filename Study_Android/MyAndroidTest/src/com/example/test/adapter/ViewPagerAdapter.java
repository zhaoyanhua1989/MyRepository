package com.example.test.adapter;

import com.example.test.model.CompressService;
import com.example.test.R;
import com.example.test.util.MyLog;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

/**
 * ViewPager的Adapter，继承自PagerAdapter。
 * 如果ViewPager中需要自定义布局，则不建议使用这个Adapter，不方便。
 * 
 * @author HKW2962
 *
 */
public class ViewPagerAdapter extends PagerAdapter {

	private Activity mActivity;
	private int[] mImages;

	public ViewPagerAdapter(Activity activity, int[] mImages) {
		this.mActivity = activity;
		this.mImages = mImages;
	}

	@Override
	public int getCount() {
		return mImages.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (View) object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		MyLog.d("页面翻页，滑动position=" + position);
		//当设置第5个位置时(viewPager从0开始数)，显示其他图片
		if (position == 4) {
			//由于container单独添加view会自动填满，所以需要一个父控件来控制自定义图片，防止拉伸。
			LinearLayout ll = new LinearLayout(mActivity);
			ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			ll.setGravity(Gravity.CENTER);
			//自定义图片
			TextView textView = new TextView(mActivity);
			textView.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
			textView.setText("7");
			textView.setTextSize(50F);
			textView.setTextColor(Color.WHITE);
			textView.setGravity(Gravity.CENTER);
			textView.setBackgroundResource(R.drawable.viewpager_textview_shape);
			ll.addView(textView);
			container.addView(ll);
			return ll;
		}
		ImageView imageView = new ImageView(mActivity);
		imageView.setScaleType(ScaleType.FIT_XY);
		// 这里最大化的缩小图片尺寸，以节约内存，尺寸缩小为原来的一半
		imageView.setImageBitmap(CompressService.compressAssign(mActivity, mImages[position], 240, 400));
		container.addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
