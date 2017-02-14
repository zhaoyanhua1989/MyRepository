package com.example.textjni;

/**
 * 作为jni测试用，工程里面没有生成对应的so文件
 * @author HKW2962
 *
 */
public class TextJNI {

	static {
		System.loadLibrary("jnisoname");
	}
	
	public native static int getJNIInt();
	public native static String getJNIString();
}