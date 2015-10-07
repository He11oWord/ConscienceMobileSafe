package com.consciencemobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

import com.consciencemobilesafe.app.R;

public class SetupActivity4 extends Activity {

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup4_layout);
		sp = getSharedPreferences("config", MODE_PRIVATE);
	}

	public void next(View view) {
		Editor editor = sp.edit();
		editor.putBoolean("Enter_Setuped", true);
		editor.commit();
		Intent intent = new Intent(SetupActivity4.this, LostFindActivity.class);
		startActivity(intent);
		finish();
	}

	public void back(View view) {
		Intent intent = new Intent(SetupActivity4.this, SetupActivity3.class);
		startActivity(intent);
		finish();
	}

}
