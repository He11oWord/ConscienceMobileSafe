package com.consciencemobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.consciencemobilesafe.app.R;

public class SetupActivity2 extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup2_layout);
	}
	
	public void next(View view){
		 Intent intent = new Intent(SetupActivity2.this,SetupActivity3.class);
		 startActivity(intent);
		 finish();
	}
	
	public void back(View view){
		 Intent intent = new Intent(SetupActivity2.this,SetupActivity1.class);
		 startActivity(intent);
		 finish();
	}
	
}
