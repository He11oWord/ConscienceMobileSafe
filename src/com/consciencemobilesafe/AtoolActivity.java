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
	// ���ݶ���ʱ��ʾ�ĶԻ���
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
		case R.id.number_quqry_button:// ��������ز�ѯ
			Log.d("sadsad", "query");
			intent = new Intent(this, NumberQueryActivity.class);
			startActivity(intent);
			break;
		case R.id.sms_copy:// ���ű���
			Log.d("sadsad", "dasda");
			// ���ű��ݵķ���
			backupSms();
			break;
		case R.id.sms_restore:// ���Ż�ԭ

			restoreSms();
			break;
		}
	}

	/**
	 * ���ݶ��ŵķ���
	 */
	private void backupSms() {
		pd = new ProgressDialog(this);
		pd.setMessage("���ڱ��ݶ���");
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
					// �����߳��и���UI
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolActivity.this, "���ݳɹ�", 0)
									.show();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolActivity.this, "����ʧ��", 0)
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
		pd.setMessage("���ڻ�ԭ����");
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
							Toast.makeText(AtoolActivity.this, "��ԭ�ɹ�", 0)
									.show();
						}
					});
				} catch (Exception e) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(AtoolActivity.this, "��ԭʧ��", 0)
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
