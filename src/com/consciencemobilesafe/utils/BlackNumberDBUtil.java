package com.consciencemobilesafe.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


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
		boolean result = false;
		cursor = db.rawQuery("select * from blacknumber where number = ?",
				new String[] { number });

		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}

	/**
	 * ��ѯĳ�������ģʽ
	 * @param number
	 *            ��ѯ�ú����Ƿ����
	 * @return �ú����ģʽ
	 */
	public String queryMode(String number) {
		SQLiteDatabase db = nsdb.getReadableDatabase();
		Cursor cursor = null;
		String result = null;
		cursor = db.rawQuery("select mode from blacknumber where number = ?",
				new String[] { number });

		if (cursor.moveToNext()) {
			result = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return result;
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
	 * @param number Ҫ�޸ĵ����ں���
	 * @param mode Ҫ�޸ĵ�����ģʽ
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
		Cursor cursor = db.rawQuery("select number,mode from blacknumber order by id desc", null);
		List<BlcakNumber> result = new ArrayList<BlcakNumber>();
		BlcakNumber info;
		while(cursor.moveToNext()) {
			info = new BlcakNumber();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			info.setNumber(number);
			info.setMode(mode);
			result.add(info);	
		
		}
		cursor.close();
		db.close();
		
		return result;
	}

}
