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
	 * 构造方法
	 * 	
	 * @param context
	 * @return
	 */
	public BlackNumberDBUtil(Context context) {
		nsdb = new NumberSmsSafeDBopenDatabase(context);

	}

	/**
	 * 查询数据
	 * 
	 * @param number
	 *            查询该号码是否存在
	 * @return 存在的时候返回true
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
	 * 查询某个号码的模式
	 * @param number
	 *            查询该号码是否存在
	 * @return 该号码的模式
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
	 * 插入数据
	 * 
	 * @param number
	 *            拉黑号码
	 * @param mode
	 *            拦截模式,1.拦截电话2.拦截短信3.全部拦截
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
	 * 删除数据
	 * 
	 * @param number要删除的号码
	 */
	public void delete(String number) {
		SQLiteDatabase db = nsdb.getWritableDatabase();

		db.delete("blacknumber", "number = ?", new String[] { number });
		db.close();
	}

	/**
	 * 更新数据
	 * 
	 * @param number 要修改的拉黑号码
	 * @param mode 要修改的拦截模式
	 */
	public void update(String number, String newmode) {
		SQLiteDatabase db = nsdb.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newmode);
		db.update("blacknumber", values, "number = ?", new String[] { number });

		db.close();
	}

	/**
	 * 查询所有数据
	 * @return 一个所有数据的集合
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
