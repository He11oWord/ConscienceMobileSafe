package com.consciencemobilesafe.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.consciencemobilesafe.db.NumberSmsSafeDBopenDatabase;
import com.consciencemobilesafe.domain.BlcakNumberInfo;

/**
 * ɱ�����ݿ��ҵ����
 * 
 * @Description TODO
 * @author lizhao
 * @date 2015-10-23 ����10:45:55
 */
public class AntiVirusDBUtil {


	/**
	 * ��ѯһ��md5�Ƿ��ڲ������ݿ�����
	 * @param md5
	 * @return
	 */
	public static boolean isVirus(String md5) {

		boolean result = false;
		String path = "/data/data/com.consciencemobilesafe.app/files/antivirus.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select * from datable where md5 = ?", new String[]{md5});

		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
		
	}

}
