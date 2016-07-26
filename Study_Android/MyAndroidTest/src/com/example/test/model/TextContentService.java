package com.example.test.model;

import java.util.Arrays;

import com.example.test.R;
import com.example.test.view.MyTextView;

import android.app.Activity;

/**
 * 动态加载文字处理业务类
 * @author HKW2962
 *
 */
public class TextContentService {

	private Activity mActivity;
	private MyTextView wcTextView;
	private String[] texts;
	private int currentIndex; // 传入文字当前数量

	public TextContentService(Activity activity, MyTextView textView, String[] strs) {
		mActivity = activity;
		wcTextView = textView;
		texts = strs;
	}

	public void showContent() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
				}
				while (true) {
					if (currentIndex == texts.length)
						break;
					try {
						Thread.sleep(400);
					} catch (Exception e) {
					}
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							wcTextView.reDraw(Arrays.copyOf(texts, currentIndex));
						}
					});
					currentIndex++;
				}
				while (true) {
					if (currentIndex < 0) {
						break;
					}
					try {
						Thread.sleep(300);
					} catch (Exception e) {
					}
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							wcTextView.done(currentIndex, mActivity.getResources().getColor(R.color.text_highlighted), true);
						}
					});
					currentIndex--;
				}
			}
		}).start();
	}

}
