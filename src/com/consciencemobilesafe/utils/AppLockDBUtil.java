package com.consciencemobilesafe.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.consciencemobilesafe.db.AppLockDbopenDatebase;
import com.consciencemobilesafe.db.NumberSmsSafeDBopenDatabase;
import com.consciencemobilesafe.domain.BlcakNumberInfo;

public class AppLockDBUtil {

	private  AppLockDbopenDatebase aldb;

	

	/**
	 * ���췽��
	 * 
	 * @param context
	 * @return
	 */
	public AppLockDBUtil(Context context) {
		aldb = new  AppLockDbopenDatebase(context);

	}

	/**
	 * ��ѯ����
	 * 
	 * @param number
	 *            ��ѯ�ú����Ƿ����
	 * @return ���ڵ�ʱ�򷵻�true
	 */
	public boolean query(String app_packagename) {
		SQLiteDatabase db = aldb.getReadableDatabase();
		Cursor cursor = null;
		boolean result = false;
		cursor = db.rawQuery("select * from blacknumber where app_packagename = ?",
				new String[] { app_packagename });

		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}

//	/**
//	 * ��ѯĳ�������ģʽ
//	 * 
//	 * @param number
//	 *            ��ѯ�ú����Ƿ����
//	 * @return �ú����ģʽ
//	 */
//	public String queryMode(String number) {
//		SQLiteDatabase db = aldb.getReadableDatabase();
//		Cursor cursor = null;
//		String result = null;
//		cursor = db.rawQuery("select mode from blacknumber where number = ?",
//				new String[] { number });
//
//		if (cursor.moveToNext()) {
//			result = cursor.getString(0);
//		}
//		cursor.close();
//		db.close();
//		return result;
//	}

	/**
	 * ��������
	 * 
	 * @param app_packagename ����
	 */
	public void insert(String app_packagename) {
		SQLiteDatabase db = aldb.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("app_packagename", app_packagename);
		db.insert("applock", null, values);
		db.close();
	}

	/**
	 * ɾ������
	 * 
	 * @param numberҪɾ���ĺ���
	 */
	public void delete(String app_packagename) {
		SQLiteDatabase db = aldb.getWritableDatabase();

		db.delete("applock", "app_packagename = ?", new String[] { app_packagename });
		db.close();
	}

//	/**
//	 * ��������
//	 * 
//	 * @param number
//	 *            Ҫ�޸ĵ����ں���
//	 * @param mode
//	 *            Ҫ�޸ĵ�����ģʽ
//	 */
//	public void update(String app_packagename) {
//		SQLiteDatabase db = aldb.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put("app_packagename", app_packagename);
//		db.update("blacknumber", values, "number = ?", new String[] { number });
//
//		db.close();
//	}

//	/**
//	 * ��ѯ��������
//	 * 
//	 * @return һ���������ݵļ���
//	 */
//	public List<BlcakNumberInfo> queryAll() {
//		SQLiteDatabase db = aldb.getReadableDatabase();
//		Cursor cursor = db.rawQuery(
//				"select app_packagename from applock", null);
//		List<BlcakNumberInfo> result = new ArrayList<BlcakNumberInfo>();
//		BlcakNumberInfo info;
//		while (cursor.moveToNext()) {
//			info = new BlcakNumberInfo();
//			String number = cursor.getString(0);
//			String mode = cursor.getString(1);
//			info.setNumber(number);
//			info.setMode(mode);
//			result.add(info);
//
//		}
//		cursor.close();
//		db.close();
//
//		return result;
//	}
//
//	
//
//	/**
//	 * ��ѯ��������
//	 * 
//	 * @param partNumber
//	 *            һ�λ�ȡ��������¼
//	 * @param partLocation
//	 *            ���ĸ�λ�ÿ�ʼ��ȡ
//	 * @return һ���������ݵļ���
//	 */
//	public List<BlcakNumberInfo> queryPart(int partNumber,int partLocation) {
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		SQLiteDatabase db = nsdb.getReadableDatabase();
//		Cursor cursor = db
//				.rawQuery(
//						"select number,mode from blacknumber order by id desc limit ? offset ?",
//						new String[] { String.valueOf(partNumber),String.valueOf(partLocation)});
//		List<BlcakNumberInfo> result = new ArrayList<BlcakNumberInfo>();
//		BlcakNumberInfo info;
//		while (cursor.moveToNext()) {
//			info = new BlcakNumberInfo();
//			String number = cursor.getString(0);
//			String mode = cursor.getString(1);
//			info.setNumber(number);
//			info.setMode(mode);
//			result.add(info);
//
//		}
//		cursor.close();
//		db.close();
//
//		return result;
//	}

}
