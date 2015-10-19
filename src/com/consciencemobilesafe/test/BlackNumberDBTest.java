package com.consciencemobilesafe.test;

import java.util.List;
import java.util.Random;

import com.consciencemobilesafe.db.NumberSmsSafeDBopenDatabase;
import com.consciencemobilesafe.domain.BlcakNumberInfo;
import com.consciencemobilesafe.utils.BlackNumberDBUtil;

import android.test.AndroidTestCase;
import android.util.Log;

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
		Random random = new Random();
		for(int i=1;i<=100;i++){
			b.insert(String.valueOf(number1+i), String.valueOf(random.nextInt(3)+1));
			System.out.println(i);
		}
	} 

	public void testFindAll()throws Exception{
		BlackNumberDBUtil b = new BlackNumberDBUtil(getContext());
		List<BlcakNumberInfo> list = b.queryAll();
		for(BlcakNumberInfo info :list){
			Log.d("haha",  info.getNumber()+"number,"+info.getMode()+"mode");
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
