package com.example.test.view;

import com.example.test.R;
import com.example.test.util.MyLog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {

	private Paint paintA = new Paint(); // 写字
	private String[] texts = new String[] { " " }; // 一调用WcTextView就会onDraw，texts不能为空
	private int textWidth; // 文字的宽度
	private int textMargin = 50; // 文字间的间隙
	private int w; // 屏幕宽度
	private int h; // 屏幕高度
	private int startX; // 第一个字开始的位置
	private int startY; // 绘画文字的高度
	private boolean isDone; // 绘画文字是否完成
	private int mposition; // 需要改变颜色的当前文字位置
	private boolean mClockwise; // 改变颜色是正序还是逆序来改变
	private int mColor; // 需要改变颜色的文字颜色

	public MyTextView(Context context) {
		super(context);
		setPaint(paintA);
	}

	@SuppressLint("ResourceAsColor")
	public MyTextView(Context context, AttributeSet attribute) {
		super(context, attribute);
		setPaint(paintA);
	}

	// 自定义的view，在layout中设置字体大小颜色等属性没有作用，需要设置paint
	private void setPaint(Paint paint) {
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(40);
		// 设置字体加粗
		paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
		// 设置阴影半径， 宽， 高， 颜色
		paint.setShadowLayer(2, -2, -2, R.color.text_shadow);
		// 用color.xml中定义的颜色来setColor
		paint.setColor(getResources().getColor(R.color.light_font));
		// 设置字体宽度
		textWidth = (int) paintA.measureText(texts.toString(), 0, 1);
		MyLog.d("textWidth=" + textWidth);
	}

	// 起始位置是本控件的宽和高
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		MyLog.d("onSizeChanged, w=" + w + ",h=" + h + ",oldw=" + oldw + ",oldh=" + oldh);
		super.onSizeChanged(w, h, oldw, oldh);
		this.w = w;
		this.h = h;
		setStartXY();
	}

	private void setStartXY() {
		// 算绘画的起始位置，从中间开始，计算每个字的宽度和字间的间隙(间隙也是字体宽度)
		if (texts.length > 0) {
			startX = w / 2 - (texts.length * textWidth + (texts.length - 1) * textMargin) / 2;
			startY = h / 2 - textWidth / 2;
		}
		MyLog.d("绘图起始位置：x=" + startX + ",y=" + startY);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < texts.length; i++) {
			canvas.drawText(texts[i], startX + i * (textWidth + textMargin), startY, paintA);
		}
		// 如果isDone，开始执行改变字体颜色
		if (isDone) {
			// 如果是逆序，从反方向开始改变
			if (!mClockwise) {
				mposition = texts.length - 1 - mposition;
			}
			for (int i = 0; i < texts.length; i++) {
				if (mposition == i) {
					paintA.setColor(mColor);
					canvas.drawText(texts[i], startX + i * (textWidth + textMargin), startY, paintA);
					break;
				}
			}
		}
		paintA.setColor(getResources().getColor(R.color.light_font));
	}

	public void reDraw(String[] strs) {
		texts = strs;
		setStartXY();
		invalidate();
	}

	/**
	 * 绘制文字完成，调用此方法改变文字颜色
	 * 
	 * @param position
	 *            需要改变颜色的文字的位置
	 * @param color
	 *            需要改变的颜色
	 * @param clockwise
	 *            是正序还是逆序
	 */
	public void done(int position, int color, boolean clockwise) {
		mposition = position;
		mColor = color;
		mClockwise = clockwise;
		isDone = true;
		invalidate();
	}

}
