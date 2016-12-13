package com.example.test.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

import com.example.test.util.OverallVariable;

public class CustomContentProvider extends ContentProvider {

	private DBOpenHelper dbOpenHelper = null;

	// UriMatcher类用来匹配Uri，使用match()方法匹配路径时返回匹配码

	/**
	 * 是一个回调函数，在ContentProvider创建的时候，就会运行,第二个参数为指定数据库名称，如果不指定，就会找不到数据库；
	 * 如果数据库存在的情况下是不会再创建一个数据库的。（当然首次调用 在这里也不会生成数据库必须调用SQLiteDatabase的
	 * getWritableDatabase,getReadableDatabase两个方法中的一个才会创建数据库）
	 */
	@Override
	public boolean onCreate() {
		// 这里会调用 DBOpenHelper的构造函数创建一个数据库；
		dbOpenHelper = new DBOpenHelper(this.getContext(), OverallVariable.DATABASE_NAME, OverallVariable.DATABASE_VERSION);
		return true;
	}

	/**
	 * 当执行这个方法的时候，如果没有数据库，他会创建，同时也会创建表，但是如果没有表，下面在执行insert的时候就会出错
	 * 这里的插入数据也完全可以用sql语句书写，然后调用 db.execSQL(sql)执行。
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// 获得一个可写的数据库引用，如果数据库不存在，则根据onCreate的方法里创建；
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		long id = 0;
		switch (OverallVariable.ContentProviderTestTableData.uriMatcher.match(uri)) {
		case OverallVariable.ContentProviderTestTableData.TEACHERS:
			id = db.insert(OverallVariable.USERS_TABLE_NAME, null, values); // 返回的是记录的行号，主键为int，实际上就是主键值
			return ContentUris.withAppendedId(uri, id);
		case OverallVariable.ContentProviderTestTableData.TEACHER:
			id = db.insert(OverallVariable.USERS_TABLE_NAME, null, values);
			String path = uri.toString();
			return Uri.parse(path.substring(0, path.lastIndexOf("/")) + id); // 替换掉id
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int count = 0;
		switch (OverallVariable.ContentProviderTestTableData.uriMatcher.match(uri)) {
		case OverallVariable.ContentProviderTestTableData.TEACHERS:
			count = db.delete(OverallVariable.USERS_TABLE_NAME, selection, selectionArgs);
			break;
		case OverallVariable.ContentProviderTestTableData.TEACHER:
			// 下面的方法用于从URI中解析出id，对这样的路径content://hb.android.OverallVariable.UserTableData.TEACHERProvider/OverallVariable.UserTableData.TEACHER/10
			// 进行解析，返回值为10
			long personid = ContentUris.parseId(uri);
			String where = "_ID=" + personid; // 删除指定id的记录
			where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : ""; // 把其它条件附加上
			count = db.delete(OverallVariable.USERS_TABLE_NAME, where, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		db.close();
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int count = 0;
		switch (OverallVariable.ContentProviderTestTableData.uriMatcher.match(uri)) {
		case OverallVariable.ContentProviderTestTableData.TEACHERS:
			count = db.update(OverallVariable.USERS_TABLE_NAME, values, selection, selectionArgs);
			break;
		case OverallVariable.ContentProviderTestTableData.TEACHER:
			// 下面的方法用于从URI中解析出id，对这样的路径content://com.ljq.provider.personprovider/person/10
			// 进行解析，返回值为10
			long personid = ContentUris.parseId(uri);
			String where = "_ID=" + personid;// 获取指定id的记录
			where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";// 把其它条件附加上
			count = db.update(OverallVariable.USERS_TABLE_NAME, values, where, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		db.close();
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (OverallVariable.ContentProviderTestTableData.uriMatcher.match(uri)) {
		case OverallVariable.ContentProviderTestTableData.TEACHERS:
			return OverallVariable.ContentProviderTestTableData.CONTENT_TYPE;
		case OverallVariable.ContentProviderTestTableData.TEACHER:
			return OverallVariable.ContentProviderTestTableData.CONTENT_TYPE_ITME;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		switch (OverallVariable.ContentProviderTestTableData.uriMatcher.match(uri)) {
		case OverallVariable.ContentProviderTestTableData.TEACHERS:
			return db.query(OverallVariable.USERS_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
		case OverallVariable.ContentProviderTestTableData.TEACHER:
			// 进行解析，返回值为10
			long personid = ContentUris.parseId(uri);
			String where = "_ID=" + personid;// 获取指定id的记录
			where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";// 把其它条件附加上
			return db.query(OverallVariable.USERS_TABLE_NAME, projection, where, selectionArgs, null, null, sortOrder);
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	class DBOpenHelper extends SQLiteOpenHelper {

		// 在SQLiteOepnHelper的子类当中，必须有该构造函数，用来创建一个数据库；
		public DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
			// 必须通过super调用父类当中的构造函数
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		// public DBOpenHelper(Context context, String name) {
		// this(context, name, VERSION);
		// }

		public DBOpenHelper(Context context, String name, int version) {
			this(context, name, null, version);
		}

		/**
		 * 只有当数据库执行创建 的时候，才会执行这个方法。如果更改表名，也不会创建，只有当创建数据库的时候，才会创建改表名之后 的数据表
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			System.out.println("create table");
			db.execSQL("create table " + OverallVariable.ContentProviderTestTableData.TABLE_NAME + "(" + OverallVariable.ContentProviderTestTableData._ID + " INTEGER PRIMARY KEY autoincrement," + OverallVariable.ContentProviderTestTableData.NAME + " varchar(20)," + OverallVariable.ContentProviderTestTableData.TITLE + " varchar(20),"
					+ OverallVariable.ContentProviderTestTableData.DATE_ADDED + " long," + OverallVariable.ContentProviderTestTableData.SEX + " boolean)" + ";");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}
}