package com.example.test.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapBiz {

	public static Bitmap compress(byte[] bytes, int width, int height) {
		MyLog.d("compress, decodeByteArray...");
		Options opts = new Options();
		// 设置这个，只得到Bitmap的属性信息放入opts，而不把Bitmap加载到内存中
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		int w = opts.outWidth / width;
		int h = opts.outHeight / height;
		// 取最大的比例，保证整个图片的长或者宽必定在该屏幕中可以显示得下
		opts.inSampleSize = w > h ? w : h;
		// 内存不足时可被回收
		opts.inPurgeable = true;
		// 设置为false,表示不仅Bitmap的属性，也要加载bitmap
		opts.inJustDecodeBounds = false;
		Bitmap map = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
		return map;
	}
	
	/**
	 * @param ctx
	 *            为Context对象
	 * @param imgId
	 *            为图片ID
	 * @param width
	 *            为需要的图片宽度
	 * @param height
	 *            为需要的图片的高度
	 * */
	public static Bitmap compress(Context ctx, int imgId, int width, int height) {
		MyLog.d("compress, decodeResource...");
		// 构建Option对象，借助此对象封装读取的数据信息
		Options opts = new Options();
		// 只读取图片边界信息
		opts.inJustDecodeBounds = true;
		// 读取图片,此时只包含边界信息(不会将整个图片读到内存)
		BitmapFactory.decodeResource(ctx.getResources(), imgId, opts);
		// 计算压缩比例(按屏幕尺寸压缩)
		int w = opts.outWidth / width;
		int h = opts.outHeight / height;
		opts.inSampleSize = w > h ? w : h;
		// 设置inJustDecodeBounds为false,目的是读取图片字节
		opts.inJustDecodeBounds = false;
		// 执行压缩操作
		return BitmapFactory.decodeResource(ctx.getResources(), imgId, opts);
	}

	/**
	 * 大牛写，根据路径获得突破并压缩返回bitmap用于显示
	 *
	 * @param filePath
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
		MyLog.d("getSmallBitmap...");
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 只返回图片的大小信息
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 计算图片的缩放值
	 *
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		MyLog.d("calculateInSampleSize...");
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

}
