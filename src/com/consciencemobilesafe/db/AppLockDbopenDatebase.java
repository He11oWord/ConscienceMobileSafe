package com.consciencemobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AppLockDbopenDatebase extends SQLiteOpenHelper{
	
	private String creat_applock = "create table applock (" 
			+ "id integer primary key autoincrement, " 
			+ "app_packagename text)";

	/**
	 *  ���췽��������һ����Ϊapplock.db�����ݿ�
	 * @param context ������
	 * @param name ���ݿ�����
	 * @param factory 
	 * @param version �汾��
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
