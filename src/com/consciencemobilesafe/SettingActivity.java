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
	// �����Ƿ����Զ�����
	private SettingItemView siv_update;
	// �����Ƿ�����������ز�ѯ
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

		// �����Ƿ��Զ�����
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		sp = getSharedPreferences("config", MODE_PRIVATE);

		if (sp.getBoolean("update", false)) {
			siv_update.setCheck(true);
			// siv_update.setText("�Զ������Ѿ���");
		} else {
			siv_update.setCheck(false);
			// siv_update.setText("�Զ������Ѿ��ر�");
		}
		siv_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor edit = sp.edit();

				if (siv_update.isChecked()) {
					// �ر��Զ����¹���
					siv_update.setCheck(false);
					// siv_update.setText("�Զ������Ѿ��ر�");
					edit.putBoolean("update", false);
				} else {
					// ���Զ����¹���
					siv_update.setCheck(true);
					// siv_update.setText("�Զ������Ѿ���");
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
					// ��ɷ�ѡ��״̬
					siv_number_query.setCheck(false);
					intent = new Intent(SettingActivity.this,
							NumberQueryService.class);
					stopService(intent);

				} else {
					// ��Ϊѡ��״̬
					siv_number_query.setCheck(true);
					intent = new Intent(SettingActivity.this,
							NumberQueryService.class);
					startService(intent);
				}
			}
		});

	}

}
