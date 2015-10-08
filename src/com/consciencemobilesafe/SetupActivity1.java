package com.consciencemobilesafe;

import android.content.Intent;
import android.os.Bundle;

import com.consciencemobilesafe.app.R;

public class SetupActivity1 extends SetupBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup1_layout);
	}


	@Override
	public void onFlingBack() {
		
	}

	@Override
	public void onFlingNext() {
		Intent intent = new Intent(SetupActivity1.this, SetupActivity2.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);

	}
}
