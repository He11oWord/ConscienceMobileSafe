package com.consciencemobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.consciencemobilesafe.app.R;

public class SetupActivity1 extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup1_layout);
	}
	
	public void next(View view){
		 Intent intent = new Intent(SetupActivity1.this,SetupActivity2.class);
		 startActivity(intent);
		 finish();
	}
}
