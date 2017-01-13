package com.example.test.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

public class FileUtil {

	// Create an image file name
	/**
	 * 注意此方法返回的路径不一定是外置储存的路径 1.一部分手机将eMMC存储挂载到 /mnt/external_sd
	 * 、/mnt/sdcard2等节点， 而将外置的SD卡挂载到
	 * Environment.getExternalStorageDirectory()这个结点。
	 * 此时，调用Environment.getExternalStorageDirectory()，则返回外置的SD的路径。
	 * 2.而另一部分手机直接将eMMC存储挂载在Environment.getExternalStorageDirectory()这个节点，
	 * 而将真正的外置SD卡挂载到/mnt/external_sd、/mnt/sdcard2 等节点。
	 * 此时，调用Environment.getExternalStorageDirectory()，则返回内置的SD的路径。
	 * 
	 * @return 用华为手机测试，返回的是内置储存的路径
	 */
	@SuppressLint("SimpleDateFormat")
	public static File createImageFile() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "MYJPEG_" + timeStamp + "_";
		try {
			File imageFile = File.createTempFile(imageFileName, /* prefix */
					".jpg", /* suffix */
					Environment.getExternalStorageDirectory() /* 根目录directory */);
			return imageFile;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 获得的SharedPreferences对象，可以被同一应用下的其他组件共享
	 * 在/data/data/<package>/shared_pref/目录下创建一个以name命名的xml文件，mode文件为模式存储偏好
	 * 这里默认mode为私有
	 * 
	 * @param activity
	 * @param name
	 *            文件名
	 * @return
	 */
	public static SharedPreferences getSharedPreferences(Activity activity, String name) {
		return activity.getSharedPreferences("SP", Activity.MODE_PRIVATE);
	}

	/**
	 * 获得的SharedPreferences对象，文件名和Activity名一致，不能被共享，只能在本Activity中使用
	 * 
	 * @param activity
	 * @param name
	 * @return
	 */
	public static SharedPreferences getPreferences(Activity activity) {
		return activity.getPreferences(Activity.MODE_PRIVATE);
	}

	/**
	 * 获得sdcard应用程序私有目录中的PICTURES目录或者文件
	 * 
	 * @param activity
	 * @param name
	 *            文件名，如果为null，则返回PICTURES目录
	 * @return
	 */
	public static File getPrivatePictureFile(Activity activity, String name) {
		File pic = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		if (TextUtils.isEmpty(name)) {
			return pic;
		}
		return new File(pic, name);
	}

	/**
	 * 限制删除删除给定文件下面的文件
	 * 
	 * @param file
	 *            File
	 * @param limit
	 *            限制删除的文件名开头，没有限制则填null
	 * @param deletDir
	 *            是否删除文件夹
	 * @return
	 */
	public static boolean deleteSDFile(File file, String limit, boolean deletDir) {
		// file目标文件夹绝对路径
		if (file.isFile()) { // 该路径名表示的文件是否是一个标准文件
			if (!TextUtils.isEmpty(limit)) {
				if (file.getName().startsWith(limit)) {
					file.delete(); // 删除该文件
					MyLog.d("delete file：" + file.getName());
				}
			} else {
				file.delete(); // 删除该文件
				MyLog.d("delete file：" + file.getName());
			}
		} else if (file.isDirectory()) { // 该路径名表示的文件是否是一个目录（文件夹）
			File[] files = file.listFiles(); // 列出当前文件夹下的所有文件
			for (File f : files) {
				deleteSDFile(f, limit, deletDir); // 递归删除
			}
		}
		// 删除文件夹, 如果这里是内置存储根目录，不建议删除。
		if (deletDir)
			file.delete();
		return true;
	}

	/**
	 * 
	 * @param context
	 */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/**
	 * * 清除本应用所有数据库 (/data/data/com.xxx.xxx/databases)
	 * 
	 * @param context
	 */
	@SuppressLint("SdCardPath")
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
	}

	/**
	 * 清除本应用SharedPreference (/data/data/com.xxx.xxx/shared_prefs)
	 * 
	 * @param context
	 */
	@SuppressLint("SdCardPath")
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
	}

	/**
	 * 按名字清除本应用数据库
	 * 
	 * @param context
	 * @param dbName
	 */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	/**
	 * 清除/data/data/com.xxx.xxx/files下的内容
	 * 
	 * @param context
	 */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	/**
	 * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
	 * 
	 * @param context
	 */
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/**
	 * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
	 * 
	 * @param filePath
	 */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}

	/**
	 * 清除本应用所有的数据
	 * 
	 * @param context
	 * @param filepath
	 */
	public static void cleanApplicationData(Context context, String... filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		for (String filePath : filepath) {
			cleanCustomCache(filePath);
		}
	}

	/**
	 * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
	 * 
	 * @param directory
	 */
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				if (item.isDirectory()) {
					deleteFilesByDirectory(item);
				} else {
					item.delete();
				}
			}
		}
	}
}
