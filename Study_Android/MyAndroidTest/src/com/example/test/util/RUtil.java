package com.example.test.util;

import android.app.Activity;
import android.content.Context;

public class RUtil {

	public static int getResIdentifier(Context context, String name, String type) {
        return context.getResources().getIdentifier(name, type,
                context.getPackageName());
    }

    public static int getId(Context context, String name) {
        return getResIdentifier(context, name, "id");
    }

    public static int getDrawable(Context context, String name) {
        return getResIdentifier(context, name, "drawable");
    }

    public static int getString(Context context, String name) {
        return getResIdentifier(context, name, "string");
    }

    public static int getLayout(Context context, String name) {
        return getResIdentifier(context, name, "layout");
    }

    public static int getStringArray(Context context, String name) {
    	return getResIdentifier(context, name, "array");
    }
    
    public static int getStyle(Context context, String name) {
    	return getResIdentifier(context, name, "style");
    }
    
    public static String getValuesString(Activity activity, String name){
		return activity.getResources().getString(getString(activity, name));
	}

    public static String[] getValuesStringArray(Activity activity, String name){
    	return activity.getResources().getStringArray(getStringArray(activity, name));
    }
	
}
