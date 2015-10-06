package edu.gxut.consciencemobilesafe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpConnection;
import org.json.JSONException;
import org.json.JSONObject;

import com.consciencemobilesafe.app.R;

import edu.gxut.consciencemobilesafe.utils.StreamTools;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
		splash_text.setText("�汾��:"+getVersionCode());
		sp = getSharedPreferences("config", MODE_PRIVATE);
	
		boolean updated = sp.getBoolean("update", false);
		if(updated){
			//�������
			CheckUpdate();
		}else{
			//�ӳ�2s������ҳ��
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					EnterHome();
				}
			}, 2000);
			
		}
		
		
		
		
		/**
		 * ������ʧЧ��
		 * ����͸���ȣ���0.2��1.0
		 * ���ñ仯��ʱ��
		 * �����ҳ���������Ч��
		 */
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(500);
		findViewById(R.id.rl_root_splash).startAnimation(aa);
	
	}

	/**
	 * �����͹�������Ϣ
	 * 
	 */
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			//������ҳ��
			case ENTER_HOME:
				Toast.makeText(SplashActivity.this, "��ӭ����", 0).show();
				EnterHome();
				break;
			//���������Ի���
			case SHOW_UPDATE_DIALOG:
				ShowUpdateDialog();
				break;
			//URL���󣬽�����ҳ��
			case URL_ERROR:
				EnterHome();
				Toast.makeText(SplashActivity.this, "URL����", 0).show();
				break;
			//�������Ӵ��󣬽�����ҳ��
			case NETWORK_ERROR:
				Toast.makeText(SplashActivity.this, "�������", 0).show();
				EnterHome();
				break;	
			case JSON_ERROR:
				Toast.makeText(SplashActivity.this, "JSON��������", 0).show();
				ShowUpdateDialog();
				//EnterHome();
			default:
				break;
			}
		}

	
		
	};
	
	/**
	 * ����Ƿ����°汾
	 */
	private void CheckUpdate(){
		
		new Thread(){
			Message mes = new Message();
			
			//�õ�ϵͳ��ʱ�䣬��ʼʱ
			long startTime = System.currentTimeMillis();
			
			public void run(){
				try{
					//��ȡUrl
					URL url = new URL(getString(R.string.serverurl));
					//����
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					int code = conn.getResponseCode();
					
					//�����ɹ�����в���
					if(code == 200){
						InputStream is = conn.getInputStream();
						//����ת����String
						String result = StreamTools.readFromStream(is);
						Log.i(TAG, "�����ɹ�"+result);
						
						//���JESOʵ��
						JSONObject obj = new JSONObject(result);
						version = (String) obj.get("verson");
						description = (String) obj.get("description");
						apkurl = (String) obj.get("apkurl");
						
						//�жϰ汾�Ƿ������°�
						if(getVersionCode().equals(version)){
							//�汾һ�£�û�����°�
							mes.what = ENTER_HOME;
						}else{
							//�汾��һ�£����������Ի���
							mes.what = SHOW_UPDATE_DIALOG;
						}
						
					}
				}catch(MalformedURLException e){
					mes.what = URL_ERROR;
					e.printStackTrace();
				
				}catch(IOException e){
					mes.what = NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					mes.what = JSON_ERROR;
					e.printStackTrace();
				}finally{
					
					//��һ�λ��ϵͳʱ��
					long endTime = System.currentTimeMillis();
					//�м�ʱ���
					long dTime = endTime - startTime;
					
					if(dTime<2000){
						try {
							Thread.sleep(2000-dTime);
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
	protected void ShowUpdateDialog(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("��ʾ����");
		
		//���ò���ȡ��
		//builder.setCancelable(false);
		
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				//������ҳ��
				EnterHome();
			}
		});
		
		builder.setMessage("�����°汾���Ƿ�����");
		builder.setPositiveButton("��������", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//������������ɵĲ��� 
				
			}
		}); 
		
		builder.setNegativeButton("��ʱ����", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ��������������ҳ��
				EnterHome();
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	/**
	 * ��ȡ��ǰ�İ汾��
	 * @return �汾��
	 */
	private String getVersionCode(){
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
	protected void EnterHome() {
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
		finish();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
