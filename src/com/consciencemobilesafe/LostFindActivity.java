package com.consciencemobilesafe;

import com.consciencemobilesafe.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class LostFindActivity extends Activity{
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lostfind_layout);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean setuped = sp.getBoolean("Enter_Setuped", false);
		//boolean setuped =false;
		if(!setuped){
			Intent intent = new Intent(LostFindActivity.this,SetupActivity1.class);
			startActivity(intent);
			finish();
		}
		
		
	}
	
	public void reEnterSetupActivity(View view){
		Intent intent = new Intent(this,SetupActivity1.class);
		startActivity(intent);
	}
	
}
