package com.example.randomcall.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "selector.db";  	// ���ݿ����ƣ�Ҳ���ļ���
	private static final int DATABASE_VERSION = 1;  
	public static final String callListTableName = "call_list";		// �����б��ÿ�ε���һ����¼
	public static final String callDetailTableName = "call_detail";	// ������ϸ��¼��ÿ���㵽��ѧ��һ����¼
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// ����һ�δ������ݿ��ʱ�򣬵��ñ����� 
		db.execSQL("CREATE TABLE IF NOT EXISTS " + callListTableName + 
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, timestamp varchar(20))");		// ���ַ�����ʽ���ʱ�䣬���磺2016-03-28 14:00:00
		db.execSQL("CREATE TABLE IF NOT EXISTS " + callDetailTableName + 
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, student_id char(11), name varchar(8), sex char(2), state char(4), callId INTEGER)");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}

