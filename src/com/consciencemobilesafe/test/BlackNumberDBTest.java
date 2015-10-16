package com.consciencemobilesafe.test;

import java.util.List;

import com.consciencemobilesafe.bean.BlcakNumber;
import com.consciencemobilesafe.db.NumberSmsSafeDBopenDatabase;
import com.consciencemobilesafe.utils.BlackNumberDBUtil;

import android.test.AndroidTestCase;

public class BlackNumberDBTest extends AndroidTestCase {
	public void testCreateBlackNumberTest() {
		NumberSmsSafeDBopenDatabase numberSmsSafeDBopenDatabase = new NumberSmsSafeDBopenDatabase(
				getContext());
		numberSmsSafeDBopenDatabase.getWritableDatabase();
	}

	public void testinsert() {
		BlackNumberDBUtil b = new BlackNumberDBUtil(getContext());
		b.insert("110", "1");
		b.insert("120", "2");
		int number1 = 135000000;
		for(int i=1;i<=100;i++){
			b.insert(""+number1+i, ""+Math.random()+1);
		}
	}

	public void testFindAll()throws Exception{
		BlackNumberDBUtil b = new BlackNumberDBUtil(getContext());
		List<BlcakNumber> list = b.queryAll();
		for(BlcakNumber info :list){
			System.out.println(info);
		}
	}
	
	public void testDelete() {
		BlackNumberDBUtil b = new BlackNumberDBUtil(getContext());
		b.delete("110");
	}

	public void testuUpdata() {
		BlackNumberDBUtil b = new BlackNumberDBUtil(getContext());
		b.update("120","1");
	}

	public void testquery() throws Exception{
		BlackNumberDBUtil b = new BlackNumberDBUtil(getContext());
		b.query("110");
	}
}
