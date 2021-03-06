package com.consciencemobilesafe.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpConnection;
import org.json.JSONException;
import org.json.JSONObject;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.utils.StreamTools;

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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		TextView splash_text = (TextView) findViewById(R.id.splash_text);
		splash_text.setText("版本号:"+getVersionCode());
		
		//检查更新
		CheckUpdate();
		
		
		/**
		 * 渐变消失效果
		 * 设置透明度，从0.2到1.0
		 * 设置变化的时间
		 * 将这个页面设置这个效果
		 */
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(500);
		findViewById(R.id.rl_root_splash).startAnimation(aa);
	
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
				Toast.makeText(SplashActivity.this, "欢迎进入", 0).show();
				EnterHome();
				break;
			//弹出升级对话框
			case SHOW_UPDATE_DIALOG:
				ShowUpdateDialog();
				break;
			//URL错误，进入主页面
			case URL_ERROR:
				EnterHome();
				Toast.makeText(SplashActivity.this, "URL错误", 0).show();
				break;
			//网络连接错误，进入主页面
			case NETWORK_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", 0).show();
				EnterHome();
				break;	
			case JSON_ERROR:
				Toast.makeText(SplashActivity.this, "JSON解析错误", 0).show();
				ShowUpdateDialog();
				//EnterHome();
			default:
				break;
			}
		}

	
		
	};
	
	/**
	 * 检查是否有新版本
	 */
	private void CheckUpdate(){
		
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
						
						//获得JESO实例
						JSONObject obj = new JSONObject(result);
						version = (String) obj.get("verson");
						description = (String) obj.get("description");
						apkurl = (String) obj.get("apkurl");
						
						//判断版本是否是最新版
						if(getVersionCode().equals(version)){
							//版本一致，没有最新版
							mes.what = ENTER_HOME;
						}else{
							//版本不一致，弹出升级对话框
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
	 * 弹出升级对话框
	 */
	protected void ShowUpdateDialog(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提示升级");
		
		//设置不能取消
		//builder.setCancelable(false);
		
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				//进入主页面
				EnterHome();
			}
		});
		
		builder.setMessage("发现新版本，是否升级");
		builder.setPositiveButton("立刻升级", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//立刻升级该完成的操作 
				
			}
		}); 
		
		builder.setNegativeButton("暂时不用", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 不升级，进入主页面
				EnterHome();
				dialog.dismiss();
			}
		});
		builder.show();
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

	/**
	 * 进入主页面
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
