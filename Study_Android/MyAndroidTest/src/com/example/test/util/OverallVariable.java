package com.example.test.util;

import android.content.UriMatcher;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;

/**
 * 全局变量定义类
 * 
 * @author HKW2962
 *
 */
public class OverallVariable {

	// assets目录下面的应用信息文件名
	public static final String INFOMSGFILE = "myInfo.properties";
	// 私有数据库
	public static final String CUSTOMDATABASE = "custompublicDatabase";
	// 短信验证码相关
	public static final String SENDMSGTOREGISTER = "【测试】尊敬的玩家，感谢注册测试账号，您的账号通行证验证码为：{0}，该验证码5分钟内有效。";
	// 验证码发送的Intent的action
	public static final String SEND_OK_OR_NOT = "SEND_OK_OR_NOT";
	// 验证码接收的Intent的action
	public static final String RECEIVE_OK_OR_NOT = "RECEIVE_OK_OR_NOT";

	public static final String AUTHORITY = "hb.android.contentProvider";
	public static final String DATABASE_NAME = "teacher.db";
	// 创建 数据库的时候，都必须加上版本信息；并且必须大于4
	public static final int DATABASE_VERSION = 4;
	public static final String USERS_TABLE_NAME = "teacher";

	/**
	 * SideslipActivity 用于测试contentProvider的常量类
	 * 
	 * @author HKW2962
	 *
	 */
	public static final class ContentProviderTestTableData implements BaseColumns {
		public static final String TABLE_NAME = "teacher";
		// Uri，外部程序需要访问就是通过这个Uri访问的，这个Uri必须的唯一的。
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/teacher");
		// 数据集的MIME类型字符串则应该以vnd.android.cursor.dir/开头
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/hb.android.teachers";
		// 单一数据的MIME类型字符串应该以vnd.android.cursor.item/开头
		public static final String CONTENT_TYPE_ITME = "vnd.android.cursor.item/hb.android.teacher";
		/* 自定义匹配码 */
		public static final int TEACHERS = 1;
		/* 自定义匹配码 */
		public static final int TEACHER = 2;

		public static final String TITLE = "title";
		public static final String NAME = "name";
		public static final String DATE_ADDED = "date_added";
		public static final String SEX = "SEX";
		public static final String DEFAULT_SORT_ORDER = "_id desc";
		// 管理Uri的对象
		public static final UriMatcher uriMatcher;
		static {
			// 常量UriMatcher.NO_MATCH表示不匹配任何路径的返回码
			uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
			// 如果match()方法匹配content://hb.android.teacherProvider/teachern路径,返回匹配码为TEACHERS
			uriMatcher.addURI(OverallVariable.AUTHORITY, "teacher", TEACHERS);
			// 如果match()方法匹配content://hb.android.teacherProvider/teacher/数字id,路径，返回匹配码为TEACHER
			uriMatcher.addURI(OverallVariable.AUTHORITY, "teacher/#", TEACHER);
		}
	}

	/**
	 * 颜色相关的常量类
	 * 
	 * @author HKW2962
	 *
	 */
	public static final class Color {
		public static String DIALOG_TRANSPARENT_BLACK = "#99000000";
		public static String UI_TYPE_BASIC_WHITE = "#F3F4F6";
		public static String UI_TYPE_BASIC_BLACK = "#31313c";
		public static String UI_TYPE_BASIC_RED = "#f75b49";
		public static String UI_TYPE_BASIC_BLUE = "#058794";
	}

	/**
	 * WebViewActivity 用于测试h5的常量类
	 * 
	 * @author HKW2962
	 *
	 */
	public static final class WebView {
		public static final String WEB_PAGE_URL_HEADER = "file:///android_asset";
		public static final String WEB_PAGE_URL_ASSETS_HTML = "file:///android_asset/assets/html";
		// 自己定义的urL的header，看用于校验前部分操作是否成功，并以此来判断用不用加载后面的操作
		public static final String WEB_INTERFACE_HEADER = "";
		public static final String WEB_GLOBAL_INTERFACE_RELOAD = "reload";
		public static final String WEB_HEADER_INTERFACE_CLOSES = "closesPage";
		public static final String WEB_HEADER_INTERFACE_BACK = "backPage";
		// UI样式(颜色)
		public static final String UI_TYPE_CSS_WHITE = "white";
		public static final String UI_TYPE_CSS_BLACK = "black";
		public static final String UI_TYPE_CSS_BLUE = "blue";
		public static final String UI_TYPE_CSS_RED = "red";
		public static final int UI_TYPE_CSS_WHITE_CODE = 1;
		public static final int UI_TYPE_CSS_BLACK_CODE = 2;
		public static final int UI_TYPE_CSS_BLUE_CODE = 3;
		public static final int UI_TYPE_CSS_RED_CODE = 4;
		public static final int MAX_RETRY_LOAD_WEB = 5; // 最大重刷次數
		public static final String CALL_JS_HEAD = "javascript:";
	}

	/**
	 * 更新功能用于测试的常量类
	 * 
	 * @author HKW2962
	 *
	 */
	public static final class Update {
		// SD卡根目录
		public static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
		// 更新下载的apk的储存路径
		public static final String APK_PATH = SDCARD_ROOT + "/temp/goodLuck";
		// Handler的处理事件，更新
		public static final int DO_UPDATE = 2;
		// Handler的处理事件，不更新
		public static final int UNDO_UPDATE = 3;
	}
}