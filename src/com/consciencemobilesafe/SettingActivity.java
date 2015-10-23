package com.consciencemobilesafe;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.service.BlackSmsService;
import com.consciencemobilesafe.service.NumberQueryService;
import com.consciencemobilesafe.service.WatchDogService;
import com.consciencemobilesafe.ui.NumberQuerySettingItemView;
import com.consciencemobilesafe.ui.SettingItemView;
import com.consciencemobilesafe.utils.ServiceUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

	private SharedPreferences sp;
	// 设置是否开启自动更新
	private SettingItemView siv_update;
	// 设置是否开启来电归属地查询
	private SettingItemView siv_number_query;
	private Intent intent = null;

	// 设置归属地背景框风格
	private NumberQuerySettingItemView nqsiv;
	private SettingItemView siv_black_number;
	private SettingItemView siv_watch_dog;

	protected void onResume() {
		super.onResume();
		ServiceUtil serviceUtil = new ServiceUtil();
		boolean isOpenService = serviceUtil.isSeriver(this,
				"com.consciencemobilesafe.service.NumberQueryService");

		if (isOpenService) {
			siv_number_query.setCheck(true);
		} else {
			siv_number_query.setCheck(false);
		}
		
		isOpenService = serviceUtil.isSeriver(this,
				"com.consciencemobilesafe.service.BlackSmsService");
		if (isOpenService) {
			siv_black_number.setCheck(true);
		} else {
			siv_black_number.setCheck(false);
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

		// 设置是否开启归属地查询
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

		//设置是否开启黑名单拦截
		siv_black_number = (SettingItemView) findViewById(R.id.siv_black_number);
		ServiceUtil serviceUtil_black = new ServiceUtil();
		boolean isOpenBlackService = serviceUtil_black.isSeriver(this,
				"com.consciencemobilesafe.service.BlackSmsService");

		if (isOpenBlackService) {
			siv_black_number.setCheck(true);
		} else {
			siv_black_number.setCheck(false);
		}

		siv_black_number.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (siv_black_number.isChecked()) {
					// 变成非选中状态
					siv_black_number.setCheck(false);
					intent = new Intent(SettingActivity.this,
							BlackSmsService.class);
					stopService(intent);

				} else {
					// 变为选中状态
					siv_black_number.setCheck(true);
					intent = new Intent(SettingActivity.this,
							BlackSmsService.class);
					startService(intent);
				}
			}
		});

		// 设置背景框风格
		nqsiv = (NumberQuerySettingItemView) findViewById(R.id.number_setting_style);

		final String[] items = { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
		int number_style = sp.getInt("number_style", 0);
		nqsiv.setText(items[number_style]);

		nqsiv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("归属地提示框风格");

				int number_style = sp.getInt("number_style", 0);
				nqsiv.setText(items[number_style]);

				builder.setTitle("归属地提示框风格");

				// 第一个String数组，第二个是默认选中第几个，三个监听事件
				builder.setSingleChoiceItems(items, number_style,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// 将选中的文本保存
								Editor edit = sp.edit();
								edit.putInt("number_style", which);
								edit.commit();
								nqsiv.setText(items[which]);

								// 关闭窗口
								dialog.dismiss();
							}
						});

				builder.setNegativeButton("cancle", null);
				builder.show();

			}
		});

		//是否开启程序锁
		siv_watch_dog = (SettingItemView) findViewById(R.id.siv_watch_dog);
		ServiceUtil serviceUtil1 = new ServiceUtil();
		boolean isOpenWatchService = serviceUtil1.isSeriver(this,
				"com.consciencemobilesafe.service.WatchDogService");

		if (isOpenWatchService) {
			siv_watch_dog.setCheck(true);
		} else {
			siv_watch_dog.setCheck(false);
		}
		
		siv_watch_dog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (siv_watch_dog.isChecked()) {
					// 变成非选中状态
					siv_watch_dog.setCheck(false);
					intent = new Intent(SettingActivity.this,
							WatchDogService.class);
					stopService(intent);

				} else {
					// 变为选中状态
					siv_watch_dog.setCheck(true);
					intent = new Intent(SettingActivity.this,
							WatchDogService.class);
					startService(intent);
				}
			}
		});
	}
}
