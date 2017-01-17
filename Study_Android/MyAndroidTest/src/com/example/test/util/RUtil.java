package com.example.test.util;

import android.app.Activity;
import android.content.Context;

public class RUtil {

	public static int getResIdentifier(Context context, String name, String type) {
		return context.getResources().getIdentifier(name, type, context.getPackageName());
	}

	/**
	 * 通过id的资源名找id
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getId(Context context, String name) {
		return getResIdentifier(context, name, "id");
	}

	/**
	 * 通过xml中定义的drawable的名找id
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getDrawable(Context context, String name) {
		return getResIdentifier(context, name, "drawable");
	}

	/**
	 * 通过xml中定义的anim的名找id
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getAnim(Context context, String name) {
		return getResIdentifier(context, name, "anim");
	}

	/**
	 * 通过xml中定义的string的名找id
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getString(Context context, String name) {
		return getResIdentifier(context, name, "string");
	}

	/**
	 * 通过xml中定义的layout的名找id
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getLayout(Context context, String name) {
		return getResIdentifier(context, name, "layout");
	}

	/**
	 * 通过xml中定义的array的名找id
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getStringArray(Context context, String name) {
		return getResIdentifier(context, name, "array");
	}

	/**
	 * 通过xml中定义的style的名找id
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getStyle(Context context, String name) {
		return getResIdentifier(context, name, "style");
	}

	/**
	 * 通过xml中定义的string的名，找到string的value
	 * 
	 * @param activity
	 * @param name
	 * @return
	 */
	public static String getValuesString(Activity activity, String name) {
		return activity.getResources().getString(getString(activity, name));
	}

	/**
	 * 通过xml中定义的array的名，找到对应的String[]
	 * 
	 * @param activity
	 * @param name
	 * @return
	 */
	public static String[] getValuesStringArray(Activity activity, String name) {
		return activity.getResources().getStringArray(getStringArray(activity, name));
	}

	/**
	 * 通过资源id获取name
	 * 
	 * @param context
	 * @param id
	 *            资源id
	 * @return
	 */
	public static String getIdName(Context context, int id) {
		return context.getResources().getResourceEntryName(id);
	}

	/**
	 * 获取做为app显示图片的id
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppIcon(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.icon;
		} catch (Exception e) {
			MyLog.e("Error occurred when get app icon", e);
		}
		return -1;
	}

	/**
	 * 通过资源类型和名字，用反射方法来找到id
	 * 
	 * @param context
	 * @param className
	 *            比如id,layout,string等
	 * @param name
	 *            定义的名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int getIdByName(Context context, String className, String name) {
		String packageName = context.getPackageName();
		Class r = null;
		int id = 0;
		try {
			r = Class.forName(packageName + ".R");
			Class[] classes = r.getClasses();
			Class desireClass = null;
			for (int i = 0; i < classes.length; ++i) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}
			if (desireClass != null)
				id = desireClass.getField(name).getInt(desireClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return id;
	}

	/**
	 * 通过资源类型和名字，用反射方法来找到id的数组
	 * 
	 * @param context
	 * @param className
	 *            比如styleable
	 * @param name
	 *            比如styleable定义的名字
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int[] getIdsByName(Context context, String className, String name) {
		String packageName = context.getPackageName();
		Class r = null;
		int[] ids = null;
		try {
			r = Class.forName(packageName + ".R");
			Class[] classes = r.getClasses();
			Class desireClass = null;
			for (int i = 0; i < classes.length; ++i) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}
			if ((desireClass != null) && (desireClass.getField(name).get(desireClass) != null) && (desireClass.getField(name).get(desireClass).getClass().isArray()))
				ids = (int[]) desireClass.getField(name).get(desireClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return ids;
	}
}
