package com.example.randomcall.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "selector.db";  	// 数据库名称，也是文件名
	private static final int DATABASE_VERSION = 1;  
	public static final String callListTableName = "call_list";		// 点名列表表，每次点名一条记录
	public static final String callDetailTableName = "call_detail";	// 点名详细记录表，每个点到的学生一条记录
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 当第一次创建数据库的时候，调用本方法 
		db.execSQL("CREATE TABLE IF NOT EXISTS " + callListTableName + 
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, timestamp varchar(20))");		// 以字符串格式存放时间，例如：2016-03-28 14:00:00
		db.execSQL("CREATE TABLE IF NOT EXISTS " + callDetailTableName + 
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, student_id char(11), name varchar(8), sex char(2), state char(4), callId INTEGER)");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}

