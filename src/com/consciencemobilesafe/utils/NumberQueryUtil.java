package com.consciencemobilesafe.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ����һ�����룬���ع�����
 * numberQueryDB(String)
 * @author ��
 *
 */
public class NumberQueryUtil {

	private static String path = "data/data/com.consciencemobilesafe.app/files/address.db";

	public static String numberQueryDB(String number) {

		//�����صĵ�ַ
		String adress = number;
		
		//path�����ݿ�ĵ�ַ�����ݿ����assets�ļ����У��޷����ж�ȡ��
		//����취�ǽ����ݿ��ļ�������data/data/<����>/filesadress.db���ò����ڳ�ʼ��ҳ����ʵ��
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
