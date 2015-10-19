package com.consciencemobilesafe;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.utils.SmsUtil;
import com.consciencemobilesafe.utils.SmsUtil.SmsCopyProcess;
import com.consciencemobilesafe.utils.SmsUtil.SmsRestoreProcess;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AtoolActivity extends Activity implements OnClickListener {

	private Button number_quqry_button;
	private Button sms_copy;
	private Button sms_restore;
	// 备份短信时显示的对话框
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atool_layout);

		number_quqry_button = (Button) findViewById(R.id.number_quqry_button);
		sms_copy = (Button) findViewById(R.id.sms_copy);
		sms_restore = (Button) findViewById(R.id.sms_restore);

		number_quqry_button.setOnClickListener(this);
		sms_copy.setOnClickListener(this);
		sms_restore.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;

		switch (v.getId()) {
		case R.id.number_quqry_button:// 来电归属地查询
			Log.d("sadsad", "query");
			intent = new Intent(this, NumberQueryActivity.class);
			startActivity(intent);
			break;
		case R.id.sms_copy:// 短信备份
			Log.d("sadsad", "dasda");
			// 短信备份的方法
			backupSms();
			break;
		case R.id.sms_restore:// 短信还原

			restoreSms();
			break;
		}
	}

	/**
	 * 备份短信的方法
	 */
	private void backupSms() {
		pd = new ProgressDialog(this);
		pd.setMessage("正在备份短信");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();

		new Thread() {
			public void run() {
				try {
					SmsUtil.smsCopy(AtoolActivity.this, new SmsCopyProcess() {

						@Override
						public void setProgress(int Process) {
							pd.setProgress(Process);
						}

						@Override
						public void setMax(int max) {
							pd.setMax(max);
						}
					});
					// 在子线程中更新UI
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolActivity.this, "备份成功", 0)
									.show();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolActivity.this, "备份失败", 0)
									.show();
						}
					});
				} finally {
					pd.dismiss();
				}

			}
		}.start();
	}

	private void restoreSms() {
		pd = new ProgressDialog(this);
		pd.setMessage("正在还原短信");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();

		new Thread() {
			public void run() {
				try {
					SmsUtil.smsRestore(AtoolActivity.this,
							new SmsRestoreProcess() {

								@Override
								public void setProgress(int process) {
									pd.setProgress(process);
								}

								@Override
								public void setMax(int max) {
									pd.setMax(max);
								}
							});

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(AtoolActivity.this, "还原成功", 0)
									.show();
						}
					});
				} catch (Exception e) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(AtoolActivity.this, "还原失败", 0)
									.show();
						}
					});
					e.printStackTrace();
				} finally {
					pd.dismiss();
				}
			}

		}.start();
	}
}
