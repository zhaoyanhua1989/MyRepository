package com.example.test.util;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 全局变量定义类
 * 
 * @author HKW2962
 *
 */
public class OverallVariable {

	// assets目录下面的应用信息文件名
	public static String INFOMSGFILE = "myInfo.properties";
	// 私有数据库
	public static String CUSTOMDATABASE = "customPrivateDatabase";

	/************************* ContentProvider 相关 ************************/
	public static final String AUTHORITY = "hb.android.contentProvider";
	public static final String DATABASE_NAME = "teacher.db";
	// 创建 数据库的时候，都必须加上版本信息；并且必须大于4
	public static final int DATABASE_VERSION = 4;
	public static final String USERS_TABLE_NAME = "teacher";

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
	/************************* ContentProvider 相关 ************************/

}