package com.consciencemobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class NumberSmsSafeDBopenDatabase extends SQLiteOpenHelper{

	
	private String creat_blacknumber = "create table blacknumber (" 
			+ "id integer primary key autoincrement, " 
			+ "number text, "
			+ "mode text)";

	/**
	 *  构造方法，创建一个名为blacknumber.db的数据库
	 * @param context 上下文
	 * @param name 数据库名字
	 * @param factory 
	 * @param version 版本号
	 */
	public NumberSmsSafeDBopenDatabase(Context context) {
		super(context, "blacknumber.db", null, 1);
		
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(creat_blacknumber);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
