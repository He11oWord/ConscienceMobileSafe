package com.consciencemobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.ui.SettingItemView;

public class SetupActivity2 extends SetupBaseActivity {

	private SettingItemView sim_setup;
	// 读取手机卡的管家
	private TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup2_layout);

		// 读取手机卡的实例
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		sim_setup = (SettingItemView) findViewById(R.id.setup2);

		if (TextUtils.isEmpty(sp.getString("simSerial", null))) {
			sim_setup.setCheck(false);
		} else {
			sim_setup.setCheck(true);
		}

		sim_setup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if (sim_setup.isChecked()) {
					editor.putString("simSerial", null);
					sim_setup.setCheck(false);
				} else {
					// 获得手机序列号
					String simSerial = tm.getSimSerialNumber();
					// 保存手机序列号
					editor.putString("simSerial", simSerial);
					sim_setup.setCheck(true);
				
				}
				editor.commit();

			}
		});

	}

	/**
	 * onFlingNext 响应滑动下一步事件
	 */
	public void onFlingNext() {
		String simSerial = sp.getString("simSerial", null);
		if(TextUtils.isEmpty(simSerial)){
			Toast.makeText(this, "您的SIM卡尚未绑定", 1).show();
			return;
		}
		
		Intent intent = new Intent(SetupActivity2.this, SetupActivity3.class);
		startActivity(intent);
		finish();
		// 只能在finish();startActivity(intent);后面使用
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
		System.out.println("前进");

	}

	/**
	 * onFlingBack 响应滑动上一步事件
	 */
	public void onFlingBack() {
		Intent intent = new Intent(SetupActivity2.this, SetupActivity1.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_back_in, R.anim.tran_back_out);
		System.out.println("返回");
	}

}
