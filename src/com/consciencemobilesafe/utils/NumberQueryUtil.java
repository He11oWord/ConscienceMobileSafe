package com.consciencemobilesafe.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 传入一个号码，返回归属地 numberQueryDB(String)
 * 
 * @author 钊
 * 
 */
public class NumberQueryUtil {

	private static String path = "data/data/com.consciencemobilesafe.app/files/address.db";

	public static String numberQueryDB(String number) {

		Cursor cursor = null;

		// 待返回的地址
		String address = number;

		// path是数据库的地址，数据库放在assets文件夹中，无法进行读取。
		// 解决办法是将数据库文件拷贝到data/data/<包名>/filesadress.db，该操作在初始化页面中实现
		SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);

		// 判断输入的数字是不是手机号码
		// 一般是13.14.15.16.18开头，使用 正则表达式就应该写成^1[34568]\d{9}
		// if(number.length()<7){
		// return number;
		// }

		if (number.matches("^1[34568]\\d{9}")) {
			cursor = openDatabase
					.rawQuery(
							"select location from data2 where id = (select outkey from data1 where id = ?)",
							// "select location from data2 where id = (select outkey from data1 where id = ?)",
							new String[] { number.substring(0, 7) });

			if (cursor.moveToNext()) {
				address = cursor.getString(0);
			}
			cursor.close();
		} else {
			// 非手机号码，一些常用号码查询
			switch (number.length()) {
			case 3:
				// 报警号码
				address = "报警号码";
				break;
			case 4:
				// 模拟器号码
				address = "模拟器号码";
				break;
			case 5:
				// 客服号码
				address = "客服号码";
				break;
			case 7:
				// 本地号码
				address = "本地号码";
				break;
			case 8:
				// 本地号码
				address = "本地号码";
				break;

			default:
				// 像010-1115161这种号码，主要查出前缀代表的城市
				if (number.length() >= 10 && number.startsWith("0")) {
					cursor = openDatabase.rawQuery(
							"select location from data2 where area = ?",
							new String[] { number.substring(1, 3) });
					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					}

					// 像0591-1515153这种电话号码
					cursor = openDatabase.rawQuery(
							"select location from data2 where area = ?",
							new String[] { number.substring(1, 4) });
					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					}

					cursor.close();
				}

				break;
			}

		}

		return address;
	}
}
