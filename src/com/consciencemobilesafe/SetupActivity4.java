package com.consciencemobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.consciencemobilesafe.app.R;

public class SetupActivity4 extends SetupBaseActivity {

	private CheckBox protecting_cb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup4_layout);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		protecting_cb = (CheckBox) findViewById(R.id.protect_checkbox);
		if(sp.getBoolean("protecting",false)){
			protecting_cb.setChecked(true);
			protecting_cb.setText("您的手机已经开启防盗保护");
		}else{
			protecting_cb.setChecked(false);
			protecting_cb.setText("您的手机尚未开启防盗保护");
		}
		protecting_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					protecting_cb.setChecked(true);
					protecting_cb.setText("您的手机已经开启防盗保护");
				}else{
					protecting_cb.setChecked(false);
					protecting_cb.setText("您的手机尚未开启防盗保护");
				}
			}
		});
		
	}

	@Override
	public void onFlingBack() {
		Editor editor = sp.edit();
		editor.putBoolean("protecting", protecting_cb.isChecked());
		editor.commit();
		Intent intent = new Intent(SetupActivity4.this, SetupActivity3.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_back_in, R.anim.tran_back_out);
	}

	@Override
	public void onFlingNext() {
		Editor editor = sp.edit();
		editor.putBoolean("Enter_Setuped", true);
		editor.putBoolean("protecting", protecting_cb.isChecked());
		editor.commit();
		Intent intent = new Intent(SetupActivity4.this, LostFindActivity.class);
		startActivity(intent);
		finish();
	}

}
