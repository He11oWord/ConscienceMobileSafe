package com.consciencemobilesafe;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.service.TaskManagerLockCleanService;
import com.consciencemobilesafe.utils.ServiceUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TaskManagerSettingActivity extends Activity {

	private CheckBox cb1;
	private CheckBox cb2;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_manager_setting_layout);
		cb1 = (CheckBox) findViewById(R.id.task_m_setting_cb1);
		cb2 = (CheckBox) findViewById(R.id.task_m_setting_cb2);
		sp = getSharedPreferences("config", MODE_PRIVATE);

		cb1.setChecked(sp.getBoolean("task_manager_see_sys_task", false));
		cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					Editor edit = sp.edit();
					edit.putBoolean("task_manager_see_sys_task", true);
					edit.commit();
				} else {
					Editor edit = sp.edit();
					edit.putBoolean("task_manager_see_sys_task", false);
					edit.commit();
				}
			}
		});

		cb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Intent intent = new Intent(getApplicationContext(),
						TaskManagerLockCleanService.class);
				if (isChecked) {
					startService(intent);
				} else {
					stopService(intent);
				}
			}
		});

		// //秒表
		// //3秒钟之后结束，1秒更新一次
		//
		// CountDownTimer cdt = new CountDownTimer(3000,1000) {
		//
		// @Override
		// public void onTick(long millisUntilFinished) {
		// //更新的时候执行
		// }
		//
		// @Override
		// public void onFinish() {
		// // 结束的时候执行
		//
		// }
		// };

	}

	@Override
	protected void onStart() {
		boolean result = new ServiceUtil().isSeriver(getApplicationContext(),
				"com.consciencemobilesafe.service.TaskManagerLockCleanService");
		cb2.setChecked(result);
		super.onStart();
	}

}
