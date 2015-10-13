package com.consciencemobilesafe.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 传入一个号码，返回归属地
 * numberQueryDB(String)
 * @author 钊
 *
 */
public class NumberQueryUtil {

	private static String path = "data/data/com.consciencemobilesafe.app/files/address.db";

	public static String numberQueryDB(String number) {

		//待返回的地址
		String adress = number;
		
		//path是数据库的地址，数据库放在assets文件夹中，无法进行读取。
		//解决办法是将数据库文件拷贝到data/data/<包名>/filesadress.db，该操作在初始化页面中实现
		SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = openDatabase
				.rawQuery(
						"select location from data2 where id = (select outkey from data1 where id = ?)",
						new String[] { number.substring(0, 7) });
		
		if(cursor.moveToNext()){
			adress = cursor.getString(0);
		}
		
		return adress;
	}
}
