package com.consciencemobilesafe;

import com.consciencemobilesafe.app.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 程序锁弹出的界面
 * 
 * @Description TODO
 * @author lizhao
 * @date 2015-10-22 下午10:07:55
 */
public class WatchDogItemActivity extends Activity {
	private EditText et;
	private Button ok;
	private Button cancel;
	private TextView tv;
	private String packageName;
	private ActivityManager am;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.watch_dog_layout);

		et = (EditText) findViewById(R.id.watch_dog_et);
		ok = (Button) findViewById(R.id.watch_dog_ok);
		cancel = (Button) findViewById(R.id.watch_dog_cancel);
		tv = (TextView) findViewById(R.id.watch_dog_tv);
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		
		
		
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = et.getText().toString();
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(WatchDogItemActivity.this, "密码不能为空", 0)
							.show();
				}

				if ("1".equals(password)) {
					finish();
					am.killBackgroundProcesses(getPackageName());
					// 关闭页面，并且发出一条暂时停止监听的信息
					Intent intent = new Intent();
					intent.setAction("com.consciencemobilesafe.tempstop");
					
					intent.putExtra("tempPackageName", packageName);
					sendBroadcast(intent);

				} else {
					Toast.makeText(WatchDogItemActivity.this, "密码错误", 0).show();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		
		
		});

		Intent intent = getIntent();
		packageName = intent.getStringExtra("packname");
		PackageManager packageManager = getPackageManager();
		String name;
		try {
			name = (String) packageManager.getApplicationInfo(packageName, 0)
					.loadLabel(packageManager);
			tv.setText("用来打开" + name);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 按返回键时，回到桌面
	 */
	public void onBackPressed(){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		startActivity(intent);
		//所有的activity最小化 不会执行ondestory 只执行 onstop方法。
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
	
}
