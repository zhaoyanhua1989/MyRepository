package com.example.test.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**借助此类中的方法访问SQLite数据库*/
public class DBContext {
	
	private DBHelper dbHelper;
	private String tableName;

	public DBContext(Context context, String databaseName, String tableName) {
		this.tableName = tableName;
		dbHelper = new DBHelper(context, databaseName, null, 1);
	}

	/**
	 * 执行查询返回Cursor，可有占位符
	 * @param params中的数据为sql中?的值，假如没有?就为null
	 */
	public Cursor execQuery(String sql, String[] params) {
		//1.打开数据库(不存在则创建)
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		//2.执行查询
		Cursor c = sdb.rawQuery(sql, params);
		sdb.close();
		return c;
	}
	
	public Cursor execQuery(String sql) {
		return execQuery(sql, null);
	}

	/**执行插入操作*/
	public long execInsert(ContentValues values) {
		SQLiteDatabase sdb = dbHelper.getReadableDatabase();
		long id = sdb.insert("note", null, values);
		sdb.close();
		return id;
	}

	/**此类为一个工具类，用于操作SQLite数据库*/
	class DBHelper extends SQLiteOpenHelper {

		/**
		 * @param context 上下文对象
		 * @param name 数据库名
		 * @param factory CursorFactory，默认null
		 * @param version 版本号
		 */
		public DBHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		/**数据库创建时第一时间执行*/
		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "create table " + tableName + "(" +
				"_id integer primary key autoincrement," +
				"content varchar(100) not null," +
				"noteDate datetime not null)";
			db.execSQL(sql);
		}

		/**数据库版本升级时会自动调用*/
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			String sql = "drop table if exists " + tableName;
			db.execSQL(sql);
			onCreate(db);
		}
	}

}
