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
 * 看门狗
 * 
 * @Description 用来监测所有在运行的程序
 * @author lizhao
 * @date 2015-10-22 下午9:43:01
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
	 * 用来暂时停止这个服务的广播接收者
	 * 
	 * @Description TODO
	 * @author lizhao
	 * @date 2015-10-22 下午11:11:02
	 */
	class tempStopReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("", "老子收到了");
			tempPackage = intent.getStringExtra("tempPackageName");
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		tempPackage = null;
		// 接收广播
		tsr = new tempStopReceiver();
		registerReceiver(tsr, new IntentFilter(
				"com.consciencemobilesafe.tempstop"));

		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		flag = true;
		al = new AppLockDBUtil(getApplicationContext());

		// 打开一个新的线程去获取包名
		new Thread() {

			public void run() {
				while (flag) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// 获得运行的栈
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
								// 服务是没有栈信息的，在服务开启activity时，要指定这个activityu运行的任务栈
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
		
		Log.d("", "打开看门狗");

		
	}

	public void onDestroy() {
		flag = false;
		Log.d("", "关闭看门狗");
		unregisterReceiver(tsr);
		
		unregisterReceiver(tc);
		tc=null;
		tsr = null;
		super.onDestroy();
	}
	
	class TempCleanReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			Log.d("sdsada", "锁屏了");
			//清理手机进程
			
			tempPackage = null;
			
		}
	}

}
