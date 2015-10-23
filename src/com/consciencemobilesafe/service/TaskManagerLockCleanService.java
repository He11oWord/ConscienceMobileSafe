package com.consciencemobilesafe.service;

import java.util.List;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 提供锁屏清理进程的服务
 */
public class TaskManagerLockCleanService extends Service{

	private ActivityManager am;
	private LockCleanReceiver lc;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		lc = new LockCleanReceiver();
		IntentFilter filter = new IntentFilter(
				Intent.ACTION_SCREEN_OFF);
		registerReceiver(lc, filter);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(lc);
		lc=null;
		super.onDestroy();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	class LockCleanReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			
			//清理手机进程
			
			List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
			for(RunningAppProcessInfo r: runningAppProcesses	){
				am.killBackgroundProcesses(r.processName);
			}
			
		}
	}
	
}
