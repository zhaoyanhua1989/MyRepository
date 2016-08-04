package com.example.test.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.example.test.util.FileUtil;
import com.example.test.util.MyLog;

/**
 * 储存和拿去对象业务处理类
 * @author HKW2962
 *
 */
public class AccessAndTakeService {

	/**
	 * 将Bitmap对象储存到sdcard应用程序私有目录中的PICTURES目录下
	 * @param activity
	 * @param fileName 文件名
	 * @param bm bitMap对象
	 * @param quality 质量，0-100，储存时按质量储存
	 * @return 储存的结果boolean
	 */
	public static boolean SaveBitmapToSDCARDPic(Activity activity, String fileName, Bitmap bm, int quality) {

		File file = FileUtil.getPrivatePictureFile(activity, fileName);
		OutputStream out = null;
		boolean result;
		try {
			out = new FileOutputStream(file);
			result = bm.compress(CompressFormat.PNG, quality, out);
		} catch (Exception e) {
			MyLog.e("SaveBitmapToSDCARDPic exception, 写入失败：" + e.getMessage());
			throw new RuntimeException("写入SDCARD失败!");
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (Exception e) {
					MyLog.e("SaveBitmapToSDCARDPic exception, 写入完成关闭流失败：" + e.getMessage());
				}
		}
		return result;
	}

}
