package com.consciencemobilesafe.receiver;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WidgetCleanReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("", "sdansidnasoidnasio");
		System.out.println("sadsadas");
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		for (RunningAppProcessInfo r : runningAppProcesses) {
			am.killBackgroundProcesses(r.processName);
		}
	}
}
