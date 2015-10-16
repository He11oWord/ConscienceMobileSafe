package com.consciencemobilesafe.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.consciencemobilesafe.bean.BlcakNumber;
import com.consciencemobilesafe.db.NumberSmsSafeDBopenDatabase;

public class BlackNumberDBUtil {

	private NumberSmsSafeDBopenDatabase nsdb;

	public void find(String number) {

	}

	/**
	 * ���췽��
	 * 
	 * @param context
	 * @return
	 */
	public BlackNumberDBUtil(Context context) {
		nsdb = new NumberSmsSafeDBopenDatabase(context);

	}

	/**
	 * ��ѯ����
	 * 
	 * @param number
	 *            ��ѯ�ú����Ƿ����
	 * @return ���ڵ�ʱ�򷵻�true
	 */
	public boolean query(String number) {
		SQLiteDatabase db = nsdb.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from blacknumber where number = ?",
				new String[] { number });

		if (cursor.moveToNext()) {
			return true;
		}
		cursor.close();
		db.close();
		return false;
	}

	/**
	 * ��������
	 * 
	 * @param number
	 *            ���ں���
	 * @param mode
	 *            ����ģʽ,1.���ص绰2.���ض���3.ȫ������
	 */
	public void insert(String number, String mode) {
		SQLiteDatabase db = nsdb.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		db.insert("blacknumber", null, values);
		db.close();
	}

	/**
	 * ɾ������
	 * 
	 * @param numberҪɾ���ĺ���
	 */
	public void delete(String number) {
		SQLiteDatabase db = nsdb.getWritableDatabase();

		db.delete("blacknumber", "number = ?", new String[] { number });
		db.close();
	}

	/**
	 * ��������
	 * 
	 * @param number
	 *            Ҫ�޸ĵ����ں���
	 * @param mode
	 *            Ҫ�޸ĵ�����ģʽ
	 */
	public void update(String number, String newmode) {
		SQLiteDatabase db = nsdb.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newmode);
		db.update("blacknumber", values, "number = ?", new String[] { number });

		db.close();
	}

	/**
	 * ��ѯ��������
	 * @return һ���������ݵļ���
	 */
	public List<BlcakNumber> queryAll() {
		SQLiteDatabase db = nsdb.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacknumber", null);
		List<BlcakNumber> list = new ArrayList<BlcakNumber>();
		
		if (cursor.moveToNext()) {
			BlcakNumber info = new BlcakNumber();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			info.setNumber(number);
			info.setMode(mode);
			list.add(info);	
		
		}
		cursor.close();
		db.close();
		
		return list;
	}

}
