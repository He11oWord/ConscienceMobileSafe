package com.consciencemobilesafe.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * ����һ�����룬���ع����� numberQueryDB(String)
 * 
 * @author ��
 * 
 */
public class NumberQueryUtil {

	private static String path = "data/data/com.consciencemobilesafe.app/files/address.db";

	public static String numberQueryDB(String number) {

		Cursor cursor = null;

		// �����صĵ�ַ
		String address = number;

		// path�����ݿ�ĵ�ַ�����ݿ����assets�ļ����У��޷����ж�ȡ��
		// ����취�ǽ����ݿ��ļ�������data/data/<����>/filesadress.db���ò����ڳ�ʼ��ҳ����ʵ��
		SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);

		// �ж�����������ǲ����ֻ�����
		// һ����13.14.15.16.18��ͷ��ʹ�� ������ʽ��Ӧ��д��^1[34568]\d{9}
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
			// ���ֻ����룬һЩ���ú����ѯ
			switch (number.length()) {
			case 3:
				// ��������
				address = "��������";
				break;
			case 4:
				// ģ��������
				address = "ģ��������";
				break;
			case 5:
				// �ͷ�����
				address = "�ͷ�����";
				break;
			case 7:
				// ���غ���
				address = "���غ���";
				break;
			case 8:
				// ���غ���
				address = "���غ���";
				break;

			default:
				// ��010-1115161���ֺ��룬��Ҫ���ǰ׺����ĳ���
				if (number.length() >= 10 && number.startsWith("0")) {
					cursor = openDatabase.rawQuery(
							"select location from data2 where area = ?",
							new String[] { number.substring(1, 3) });
					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					}

					// ��0591-1515153���ֵ绰����
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
