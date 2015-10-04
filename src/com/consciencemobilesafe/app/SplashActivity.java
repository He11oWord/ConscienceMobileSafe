package com.consciencemobilesafe.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpConnection;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.utils.StreamTools;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	protected static final String TAG = "SplashActivity";
	protected static final int ENTER_HOME = 0;
	protected static final int URL_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		TextView splash_text = (TextView) findViewById(R.id.splash_text);
		splash_text.setText("版本号:"+getVersionCode());
		
		//检查更新
		checkUpdate();
	}

	/**
	 * 处理发送过来的信息
	 * 
	 */
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			//进入主页面
			case ENTER_HOME:
				enterHome();
				break;
			//URL错误，进入主页面
			case URL_ERROR:
				enterHome();
				Toast.makeText(getApplicationContext(), "URL错误", 0);
				break;
			//网络连接错误，进入主页面
			case NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "网络错误", 0);
				enterHome();
				break;	
			default:
				break;
			}
		}

	
		
	};
	
	
	/**
	 * 检查是否有新版本
	 */
	private void checkUpdate(){
		
		new Thread(){
			Message mes = new Message();
			
			//得到系统的时间，开始时
			long startTime = System.currentTimeMillis();
			
			public void run(){
				try{
					//获取Url
					URL url = new URL(getString(R.string.serverurl));
					//联网
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					int code = conn.getResponseCode();
					
					//联网成功后进行操作
					if(code == 200){
						InputStream is = conn.getInputStream();
						//将流转换成String
						String result = StreamTools.readFromStream(is);
						Log.i(TAG, "联网成功"+result);
						
						mes.what = ENTER_HOME;
					}
				}catch(MalformedURLException e){
					mes.what = URL_ERROR;
					e.printStackTrace();
				
				}catch(IOException e){
					mes.what = NETWORK_ERROR;
					e.printStackTrace();
				}finally{
					
					//再一次获得系统时间
					long endTime = System.currentTimeMillis();
					//中间时间差
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
	 * 获取当前的版本号
	 * @return 版本号
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

	protected void enterHome() {
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
