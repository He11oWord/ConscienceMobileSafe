package com.consciencemobilesafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.consciencemobilesafe.app.R;

public class SetupActivity3 extends SetupBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup3_layout);
	}



	@Override
	public void onFlingBack() {
		Intent intent = new Intent(SetupActivity3.this, SetupActivity2.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_back_in, R.anim.tran_back_out);		
	}

	@Override
	public void onFlingNext() {
		Intent intent = new Intent(SetupActivity3.this, SetupActivity4.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}
	
	public void selectContact(View view){
		Intent intent = new Intent(this,SelectContactActivity.class);
		startActivityForResult(intent, 0);
	}

}
