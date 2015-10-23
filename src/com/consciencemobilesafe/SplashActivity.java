package com.consciencemobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpConnection;
import org.json.JSONException;
import org.json.JSONObject;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.service.WatchDogService;
import com.consciencemobilesafe.utils.StreamTools;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	protected static final String TAG = "SplashActivity";
	protected static final int ENTER_HOME = 0;
	protected static final int SHOW_UPDATE_DIALOG = 1;
	protected static final int URL_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	protected static final int JSON_ERROR = 4;

	private String description;
	private String version;
	private String apkurl;

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		TextView splash_text = (TextView) findViewById(R.id.splash_text);
		splash_text.setText("�汾��:" + getVersionCode());
		sp = getSharedPreferences("config", MODE_PRIVATE);
		installShortCut();
		// path�����ݿ�ĵ�ַ�����ݿ����assets�ļ����У��޷����ж�ȡ��
		// ����취�ǽ����ݿ��ļ�������/data/data/<����>/files/adress.db�£��ò����ڳ�ʼ��ҳ����ʵ��
		// ��NumberQueryUtil.JAVA
		copyDB();
	
		
		boolean updated = sp.getBoolean("update", false);
		if (updated) {
			// �������
			CheckUpdate();
		} else {
			// �ӳ�1s������ҳ��
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					enterName();
				}
			}, 1000);

		}

		/**
		 * ������ʧЧ�� ����͸���ȣ���0.2��1.0 ���ñ仯��ʱ�� �����ҳ���������Ч��
		 */
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(500);
		findViewById(R.id.rl_root_splash).startAnimation(aa);

	}

	/**
	 * �����绰���ݿ⵽����Ŀ¼֮��
	 */
	@SuppressLint("ShowToast")
	private void copyDB() {

		try {
			File file = new File(getFilesDir(), "address.db");

			// �ж��ļ��Ƿ����
			if (file.exists() && file.length() > 0) {
				Toast.makeText(this, "�ļ��Ѵ���", 0).show();
			} else {
				Toast.makeText(this, "�ļ�������", 0).show();
				InputStream is = getAssets().open("address.db");
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);

				}

				is.close();
				fos.close();
			}
		} catch (IOException e) {
			Toast.makeText(this, "��������", 0).show();
			;
			e.printStackTrace();

		}

	}

	/**
	 * �����͹�������Ϣ
	 * 
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			// ������ҳ��
			case ENTER_HOME:
				Toast.makeText(SplashActivity.this, "��ӭ����", 0).show();
				enterName();
				break;
			// ���������Ի���
			case SHOW_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			// URL���󣬽�����ҳ��
			case URL_ERROR:
				enterName();
				Toast.makeText(SplashActivity.this, "URL����", 0).show();
				break;
			// �������Ӵ��󣬽�����ҳ��
			case NETWORK_ERROR:
				Toast.makeText(SplashActivity.this, "�������", 0).show();
				enterName();
				break;
			case JSON_ERROR:
				Toast.makeText(SplashActivity.this, "JSON��������", 0).show();
				showUpdateDialog();
				// EnterHome();
			default:
				break;
			}
		}

	};

	/**
	 * ����Ƿ����°汾
	 */
	private void CheckUpdate() {

		new Thread() {
			Message mes = new Message();

			// �õ�ϵͳ��ʱ�䣬��ʼʱ
			long startTime = System.currentTimeMillis();

			public void run() {
				try {
					// ��ȡUrl
					URL url = new URL(getString(R.string.serverurl));
					// ����
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					int code = conn.getResponseCode();

					// �����ɹ�����в���
					if (code == 200) {
						InputStream is = conn.getInputStream();
						// ����ת����String
						String result = StreamTools.readFromStream(is);
						Log.i(TAG, "�����ɹ�" + result);

						// ���JESOʵ��
						JSONObject obj = new JSONObject(result);
						version = (String) obj.get("verson");
						description = (String) obj.get("description");
						apkurl = (String) obj.get("apkurl");

						// �жϰ汾�Ƿ������°�
						if (getVersionCode().equals(version)) {
							// �汾һ�£�û�����°�
							mes.what = ENTER_HOME;
						} else {
							// �汾��һ�£����������Ի���
							mes.what = SHOW_UPDATE_DIALOG;
						}

					}
				} catch (MalformedURLException e) {
					mes.what = URL_ERROR;
					e.printStackTrace();

				} catch (IOException e) {
					mes.what = NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					mes.what = JSON_ERROR;
					e.printStackTrace();
				} finally {

					// ��һ�λ��ϵͳʱ��
					long endTime = System.currentTimeMillis();
					// �м�ʱ���
					long dTime = endTime - startTime;

					if (dTime < 2000) {
						try {
							Thread.sleep(2000 - dTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					handler.sendMessage(mes);
				}
			}
		}.start();
	}

	/**
	 * ���������Ի���
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("��ʾ����");

		// ���ò���ȡ��
		// builder.setCancelable(false);

		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// ������ҳ��
				enterName();
			}
		});

		builder.setMessage("�����°汾���Ƿ�����");
		builder.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ������������ɵĲ���

			}
		});

		builder.setNegativeButton("��ʱ����", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ��������������ҳ��
				enterName();
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * ��ȡ��ǰ�İ汾��
	 * 
	 * @return �汾��
	 */
	private String getVersionCode() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "nothing";
		}

	}

	/**
	 * ������ҳ��
	 */
	protected void enterName() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * ��װ�����ݷ�ʽ
	 */
	private void installShortCut() {
		// ���͹㲥��ͼ
		Intent intent = new Intent();
		intent.setAction("com.android.luncher.action.INSTALL_SHORTCUT");
		// ��ݷ�ʽ����������������Ϣ1.����2.ͼ��3.��ʲô����
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "�����ֻ���ʿ");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory
				.decodeResource(getResources(), R.drawable.ic_launcher));

		// ��ݷ�ʽ����ͼ
		Intent shortcutIntent = new Intent();
		shortcutIntent.setAction("android.intent.action.MAIN");
		shortcutIntent.addCategory("android.intent.category.LAUNCHER");
		shortcutIntent.setClassName(getPackageName(),
				"com.consciencemobilesafe.SplashActivity");
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

		sendBroadcast(intent);
		Log.d("sadsad", "dsadasdhj");
	}

}
