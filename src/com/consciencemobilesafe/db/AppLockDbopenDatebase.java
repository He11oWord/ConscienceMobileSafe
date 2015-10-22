package com.consciencemobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AppLockDbopenDatebase extends SQLiteOpenHelper{
	
	private String creat_applock = "create table applock (" 
			+ "id integer primary key autoincrement, " 
			+ "app_packagename text)";

	/**
	 *  构造方法，创建一个名为applock.db的数据库
	 * @param context 上下文
	 * @param name 数据库名字
	 * @param factory 
	 * @param version 版本号
	 */
	public AppLockDbopenDatebase(Context context) {
		super(context, "applock.db", null, 1);
		
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(creat_applock);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
