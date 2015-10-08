package com.consciencemobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.consciencemobilesafe.app.R;

public class SetupActivity4 extends SetupBaseActivity {

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup4_layout);
		sp = getSharedPreferences("config", MODE_PRIVATE);
	}
	
	@Override
	public void onFlingBack() {
		Intent intent = new Intent(SetupActivity4.this, SetupActivity3.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_back_in, R.anim.tran_back_out);		
	}

	@Override
	public void onFlingNext() {
		Editor editor = sp.edit();
		editor.putBoolean("Enter_Setuped", true);
		editor.commit();
		Intent intent = new Intent(SetupActivity4.this, LostFindActivity.class);
		startActivity(intent);
		finish();
	}

}
