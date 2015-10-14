package com.consciencemobilesafe;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.service.NumberQueryService;
import com.consciencemobilesafe.ui.SettingItemView;
import com.consciencemobilesafe.utils.ServiceUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

	private SharedPreferences sp;
	// 设置是否开启自动更新
	private SettingItemView siv_update;
	// 设置是否开启来电归属地查询
	private SettingItemView siv_number_query;
	private Intent intent = null;

	protected void onResume(){
		super.onResume();
		ServiceUtil serviceUtil = new ServiceUtil();
		boolean isOpenService = serviceUtil.isSeriver(this,
				"com.consciencemobilesafe.service.NumberQueryService");
	

		if (isOpenService) {
			siv_number_query.setCheck(true);
		} else {
			siv_number_query.setCheck(false);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);

		// 设置是否自动更新
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		sp = getSharedPreferences("config", MODE_PRIVATE);

		if (sp.getBoolean("update", false)) {
			siv_update.setCheck(true);
			// siv_update.setText("自动更新已经打开");
		} else {
			siv_update.setCheck(false);
			// siv_update.setText("自动更新已经关闭");
		}
		siv_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor edit = sp.edit();

				if (siv_update.isChecked()) {
					// 关闭自动更新功能
					siv_update.setCheck(false);
					// siv_update.setText("自动更新已经关闭");
					edit.putBoolean("update", false);
				} else {
					// 打开自动更新功能
					siv_update.setCheck(true);
					// siv_update.setText("自动更新已经打开");
					edit.putBoolean("update", true);
				}
				edit.commit();
			}
		});

		siv_number_query = (SettingItemView) findViewById(R.id.siv_number_query);
		ServiceUtil serviceUtil = new ServiceUtil();
		boolean isOpenService = serviceUtil.isSeriver(this,
				"com.consciencemobilesafe.service.NumberQueryService");
	

		if (isOpenService) {
			siv_number_query.setCheck(true);
		} else {
			siv_number_query.setCheck(false);
		}

		siv_number_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (siv_number_query.isChecked()) {
					// 变成非选中状态
					siv_number_query.setCheck(false);
					intent = new Intent(SettingActivity.this,
							NumberQueryService.class);
					stopService(intent);

				} else {
					// 变为选中状态
					siv_number_query.setCheck(true);
					intent = new Intent(SettingActivity.this,
							NumberQueryService.class);
					startService(intent);
				}
			}
		});

	}

}
