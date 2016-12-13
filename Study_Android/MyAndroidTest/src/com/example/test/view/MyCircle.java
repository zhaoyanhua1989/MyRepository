package com.example.test.view;

import com.example.test.activity.ViewPagerActivity;
import com.example.test.util.MyLog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 根据viewPager中view的数量画对应个数的圆
 * 
 * @author HKW2962
 *
 */
public class MyCircle extends View {

	private int count; // 画多少个圆
	private Paint paint = new Paint(); // 画笔
	private int startX; // 当前绘画x位置
	private int startY; // 当前绘画的y位置
	private int radius = 20; // 圆的半径
	private int pos; // 位置
	private float offset; // 偏移量
	private Point[] circleCenters; // 各原点的圆心，设置监听用
	private boolean flag; // 判断circleCenters数组是否已填充满
	private ViewPagerActivity mactivity; // 上下文对象，用于页码翻页的调整

	public MyCircle(Context context) {
		super(context);
	}

	// 必须要重写此构造方法，不然直接crash
	public MyCircle(Context context, AttributeSet attribute) {
		super(context, attribute);
	}

	public void setCount(int n) {
		count = n;
		circleCenters = new Point[n];
	}

	// 当屏幕尺寸发生变化时调用，首次进入会调用，在onDraw方法之前
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		MyLog.d("起始测量, w= " + w);
		// 算绘画的起始位置，比如每个小球半径10，间隔10，各占30
		startX = w - (3 * radius * count);
		startY = h;
	}

	// 绘图
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		// 画球，每个小球半径10
		for (int i = 0; i < count; i++) {
			canvas.drawCircle(startX + 3 * i * radius, startY / 2, radius, paint);
			// 取圆心的点，但是触摸拿到的点比实际圆心X轴少一个半径，这里添加圆心点的时候也减一个半径
			if (!flag)
				circleCenters[i] = new Point(startX - radius + (1 + 3 * i) * radius, startY / 2);
		}
		flag = true;
		paint.setColor(Color.YELLOW);
		paint.setStyle(Style.FILL);
		canvas.drawCircle(startX + 3 * (pos + offset) * radius, startY / 2, radius, paint);
	}

	public void reDraw(int pos, float offset) {
		this.pos = pos;
		this.offset = offset;
		invalidate(); // 重新调用onDraw
	}

	/**
	 * 相当于没有任何控件的情况下，监听触摸事件(View类中的方法)
	 * 
	 * @return True if the event was handled, false otherwise.
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Point point = new Point((int) event.getX(), (int) event.getY());
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setBollsOnClickListener(point);
			break;
		}
		// 这里如果返回的是super.onTouchEvent(event)或者false，将不能正确的监听点中小球切换view
		return true;
	}

	/**
	 * 判断小球的点击时间，如果点击的坐标点在任意一个小球(圆)的范围内，则该小球是选中的小球，并设置viewPager跳转到指定位置
	 * 
	 * @param point
	 *            点击的坐标点
	 */
	private void setBollsOnClickListener(Point point) {
		for (int i = 0; i < circleCenters.length; i++) {
			Point mpoint = circleCenters[i];
			if (Math.sqrt(Math.pow(point.x - mpoint.x, 2) + Math.pow(point.y - mpoint.y, 2)) <= radius) {
				MyLog.d("点击选中小球：" + i);
				// 选中小球通知翻页
				mactivity.notifyPaging(i);
				break;
			}
		}
	}

	public void setContext(ViewPagerActivity activity) {
		mactivity = activity;
	}
}
