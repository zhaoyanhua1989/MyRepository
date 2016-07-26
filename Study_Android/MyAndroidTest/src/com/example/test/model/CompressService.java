package com.example.test.model;

import com.example.test.util.BitmapBiz;
import com.example.test.util.ScreenUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

/**
 * 压缩业务类
 * 
 * @author HKW2962
 *
 */
public class CompressService {

	// 按屏幕尺寸压缩图片，慎用！
	public static Bitmap compressNormalScreenSize(Activity activity, int resourceId) {
		return BitmapBiz.compress(activity, resourceId, ScreenUtil.getMetrics(activity).widthPixels, ScreenUtil.getMetrics(activity).heightPixels);
	}

	// 按屏幕尺寸的一半压缩图片
	public static Bitmap compressHalfScreenSize(Activity activity, int resourceId) {
		return BitmapBiz.compress(activity, resourceId, (ScreenUtil.getMetrics(activity).widthPixels) / 4, (ScreenUtil.getMetrics(activity).heightPixels) / 4);
	}

	// 按指定大小一半压缩图片
	public static Bitmap compressHalfSize(Activity activity, int resourceId, int width, int height) {
		return BitmapBiz.compress(activity, resourceId, width/2, height/2);
	}

	// 按指定大小压缩图片
	public static Bitmap compressAssign(Activity activity, int resourceId, int width, int height) {
		return BitmapBiz.compress(activity, resourceId, width, height);
	}

	// 释放背景图片BitMap资源
	public static void releaseBackgroundBitMap(Activity activity, int resourceId) {
		View view = activity.findViewById(resourceId);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) view.getBackground();
		view.setBackgroundResource(0);
		bitmapDrawable.setCallback(null);
		Bitmap bitmap = bitmapDrawable.getBitmap();
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
	}

	// 释放BitMap资源
	public static void releaseBitMap(Bitmap bitMap) {
		if (bitMap != null && !bitMap.isRecycled()) {
			bitMap.recycle();
			bitMap = null;
		}
		System.gc();
	}

}
