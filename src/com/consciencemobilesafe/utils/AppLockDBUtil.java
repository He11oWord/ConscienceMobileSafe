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
	 * 构造方法
	 * 
	 * @param context
	 * @return
	 */
	public AppLockDBUtil(Context context) {
		aldb = new  AppLockDbopenDatebase(context);

	}

	/**
	 * 查询数据
	 * 
	 * @param number
	 *            查询该号码是否存在
	 * @return 存在的时候返回true
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
//	 * 查询某个号码的模式
//	 * 
//	 * @param number
//	 *            查询该号码是否存在
//	 * @return 该号码的模式
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
	 * 插入数据
	 * 
	 * @param app_packagename 包名
	 */
	public void insert(String app_packagename) {
		SQLiteDatabase db = aldb.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("app_packagename", app_packagename);
		db.insert("applock", null, values);
		db.close();
	}

	/**
	 * 删除数据
	 * 
	 * @param number要删除的号码
	 */
	public void delete(String app_packagename) {
		SQLiteDatabase db = aldb.getWritableDatabase();

		db.delete("applock", "app_packagename = ?", new String[] { app_packagename });
		db.close();
	}

//	/**
//	 * 更新数据
//	 * 
//	 * @param number
//	 *            要修改的拉黑号码
//	 * @param mode
//	 *            要修改的拦截模式
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
//	 * 查询所有数据
//	 * 
//	 * @return 一个所有数据的集合
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
//	 * 查询部分数据
//	 * 
//	 * @param partNumber
//	 *            一次获取多少条记录
//	 * @param partLocation
//	 *            从哪个位置开始获取
//	 * @return 一个所有数据的集合
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
