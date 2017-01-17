package com.example.test.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

public class MyLog {

	public static final String MY_LOG_TAG = "MyLog";
	/**
	 * 是否打印日志，发布时建议设置成false
	 */
	private static boolean isPrint = false;

	public static void i(String info) {
		if (isPrint)
			Log.i(MY_LOG_TAG, info);
	}

	public static void e(String errorInfo, Throwable t) {
		if (isPrint)
			Log.e(MY_LOG_TAG, errorInfo, t);
	}

	public static void e(String errorInfo) {
		if (isPrint)
			Log.e(MY_LOG_TAG, errorInfo);
	}

	public static void w(String info) {
		if (isPrint)
			Log.w(MY_LOG_TAG, info);
	}

	public static void d(String info) {
		if (isPrint)
			Log.d(MY_LOG_TAG, info);
	}

	public static void openLog(boolean flag) {
		isPrint = flag;
	}

	/**
	 * 把异常信息变成字符串，发给开发人员
	 * 
	 * @param e
	 *            Exception
	 */
	public static void handleException(Exception e) {
		String str = "";
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		str = stringWriter.toString();
		if (!isPrint) {
			// TODO 联网发送
		} else {
			// 开发中
			e("异常信息:" + str);
		}
	}

	/******************** 用className~methodName~msg方式打印日志 *********************/
	private static MLogLevel _logLevel = MLogLevel.DEBUG;
	// 该标记在需要调用Android系统Log接口在控制台打印日志信息时使用
	private final static String TAG = "MyLog";

	public static void setLoglevel(MLogLevel logLevel) {
		_logLevel = logLevel;
	}

	// --------- 调试日志接口 -------------
	public static void debug(String className, String methodName, String msg) {
		debug(className, methodName, msg, null);
	}

	public static void debug(String className, String methodName, String msg, Exception e) {
		if (_logLevel.ordinal() < MLogLevel.DEBUG.ordinal())
			return;
		log(LogType.DEBUG, className, methodName, msg, e);
	}

	// --------- INFO日志接口 -------------
	public static void info(String className, String methodName, String msg) {
		log(LogType.INFO, className, methodName, msg, null);
	}

	// --------- 警告日志 ---------
	public static void warn(String className, String methodName, String msg) {
		warn(className, methodName, msg, null);
	}

	public static void warn(String className, String methodName, String msg, Exception e) {
		if (_logLevel.ordinal() < MLogLevel.WARN.ordinal())
			return;

		log(LogType.WARN, className, methodName, msg, e);
	}

	public static void error(String className, String methodName, String msg, Exception e) {
		if (_logLevel.ordinal() < MLogLevel.ERROR.ordinal())
			return;

		logError(className, methodName, msg, e);

	}

	/**
	 * 记录需要上传的出错日志到错误日志文件
	 * 
	 * @param className
	 * @param methodName
	 * @param category
	 *            错误类别
	 * @param msg
	 * @param errLevel
	 *            错误级别： 1 - FATAL， 2 - ERR
	 */
	private static void logError(String className, String methodName, String msg, Exception e) {
		if (e != null) {
			if (!StringUtil.isEmpty(msg))
				msg = msg + "\r\n" + getStackTrace(e);
			else
				msg = getStackTrace(e);
		}

		if (className == null)
			className = "";
		if (methodName == null)
			methodName = "";
		if (msg == null)
			msg = "";

		// String content = String.format("%s`%s`%s", className, methodName,
		// msg.replace("\n", "<br>").replace("\r", ""));
		// TODO 上传错误日志到服务端
		sendSystemLog(LogType.ERROR, msg);
	}

	/**
	 * 记录日志消息到日志文件
	 * 
	 * @param logType
	 *            日志种类
	 * @param className
	 *            产生日志的类名
	 * @param methodName
	 *            产生日志的方法名
	 * @param msg
	 *            日志消息
	 * @param e
	 *            异常对象，无异常为null。
	 */
	private static void log(LogType logType, String className, String methodName, String msg, Exception e) {
		if (e != null) {
			if (!StringUtil.isEmpty(msg))
				msg = msg + "\r\n" + getStackTrace(e);
			else
				msg = getStackTrace(e);
		}

		if (className == null)
			className = "";
		if (methodName == null)
			methodName = "";
		msg = String.format("%s`%s`%s", className, methodName, msg);
		sendSystemLog(logType, msg);
	}

	/**
	 * 往 Android 系统日志中写入日志信息
	 * 
	 * @param logType
	 * @param msg
	 */
	private static void sendSystemLog(LogType logType, String msg) {
		switch (logType) {
		case DEBUG:
			Log.d(TAG, msg);
			break;
		case WARN:
			Log.w(TAG, msg);
			break;
		case ERROR:
			Log.e(TAG, msg);
			break;
		case INFO:
			Log.i(TAG, msg);
			break;
		}
	}

	private static String getStackTrace(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		return baos.toString();
	}

	public enum MLogLevel {
		/**
		 * 错误信息级别，记录错误日志
		 */
		ERROR,
		/**
		 * 警告信息级别，记录错误和警告日志
		 */
		WARN,
		/**
		 * 调试信息级别，记录错误、警告和调试信息，为最详尽的日志级别
		 */
		DEBUG;

		public static MLogLevel fromValue(int value) {
			MLogLevel level;
			switch (value) {
			case 1:
				level = WARN;
				break;
			case 2:
				level = DEBUG;
				break;
			default:
				level = ERROR;
				break;
			}

			return level;
		}
	}

	public enum LogType {
		ERROR, WARN, DEBUG,
		/**
		 * 提示日志
		 */
		INFO
	}
}
