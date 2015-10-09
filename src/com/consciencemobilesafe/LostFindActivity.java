package com.consciencemobilesafe;

import com.consciencemobilesafe.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {

	private SharedPreferences sp;
	private TextView safe_phone_tv;
	private ImageView protecting_lost_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lostfind_layout);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean setuped = sp.getBoolean("Enter_Setuped", false);
		if (!setuped) {
			Intent intent = new Intent(LostFindActivity.this,
					SetupActivity1.class);
			startActivity(intent);
			finish();
		}

		// 是否设置安全号码
		safe_phone_tv = (TextView) findViewById(R.id.safe_phone_tv);
		if (TextUtils.isEmpty(sp.getString("safe_phone", ""))) {
			safe_phone_tv.setText("尚未设置安全号码");
		} else {
			safe_phone_tv.setText(sp.getString("safe_phone", ""));
		}

		// 是否开启防盗保护
		protecting_lost_iv = (ImageView) findViewById(R.id.protecting_lost_iv);
		if (sp.getBoolean("protecting", false)) {
			// 手机已经开启保护
			protecting_lost_iv.setImageResource(R.drawable.lock);
		} else {
			// 手机尚未开启保护
			protecting_lost_iv.setImageResource(R.drawable.unlock);

		}

	}

	public void reEnterSetupActivity(View view) {
		Intent intent = new Intent(this, SetupActivity1.class);
		startActivity(intent);
	}

}
