package com.consciencemobilesafe.service;

import java.util.List;

import com.consciencemobilesafe.WatchDogItemActivity;
import com.consciencemobilesafe.service.TaskManagerLockCleanService.LockCleanReceiver;
import com.consciencemobilesafe.utils.AppLockDBUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

/**
 * ���Ź�
 * 
 * @Description ����������������еĳ���
 * @author lizhao
 * @date 2015-10-22 ����9:43:01
 */
public class WatchDogService extends Service {

	private ActivityManager am;
	boolean flag;
	private AppLockDBUtil al;
	private ComponentName cn;
	private String app_packagename;
	private tempStopReceiver tsr;
	private String tempPackage;
	private TempCleanReceiver tc;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * ������ʱֹͣ�������Ĺ㲥������
	 * 
	 * @Description TODO
	 * @author lizhao
	 * @date 2015-10-22 ����11:11:02
	 */
	class tempStopReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("", "�����յ���");
			tempPackage = intent.getStringExtra("tempPackageName");
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		tempPackage = null;
		// ���չ㲥
		tsr = new tempStopReceiver();
		registerReceiver(tsr, new IntentFilter(
				"com.consciencemobilesafe.tempstop"));

		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		flag = true;
		al = new AppLockDBUtil(getApplicationContext());

		// ��һ���µ��߳�ȥ��ȡ����
		new Thread() {

			public void run() {
				while (flag) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// ������е�ջ
					List<RunningTaskInfo> runningTasks = am
							.getRunningTasks(2);
					cn = runningTasks.get(0).topActivity;
					app_packagename = cn.getPackageName();
					if (!TextUtils.isEmpty(app_packagename)) {
						if (al.query(app_packagename)) {
							if (!TextUtils.isEmpty(tempPackage )&&tempPackage.equals(app_packagename)) {
								
							} else {

								Intent i = new Intent(
										getApplicationContext(),
										WatchDogItemActivity.class);
								// ������û��ջ��Ϣ�ģ��ڷ�����activityʱ��Ҫָ�����activityu���е�����ջ
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								i.putExtra("packname", app_packagename);
								
								startActivity(i);
							}
						}
					}
				}
			};

		}.start();

		tc = new TempCleanReceiver();
		IntentFilter filter = new IntentFilter(
				Intent.ACTION_SCREEN_OFF);
		registerReceiver(tc, filter);
		super.onCreate();
		
		Log.d("", "�򿪿��Ź�");

		
	}

	public void onDestroy() {
		flag = false;
		Log.d("", "�رտ��Ź�");
		unregisterReceiver(tsr);
		
		unregisterReceiver(tc);
		tc=null;
		tsr = null;
		super.onDestroy();
	}
	
	class TempCleanReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			Log.d("sdsada", "������");
			//�����ֻ�����
			
			tempPackage = null;
			
		}
	}

}
