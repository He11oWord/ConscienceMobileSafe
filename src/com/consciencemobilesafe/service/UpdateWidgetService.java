package com.consciencemobilesafe.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.consciencemobilesafe.TaskManagerActivity;
import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.receiver.MyWidget;
import com.consciencemobilesafe.utils.TaskManagerUtil;

import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	private Timer timer;
	private TimerTask timerTask;
	private AppWidgetManager awm;
	private LockOnReceiver lor;
	private LockOffReceiver lof;

	@Override
	public void onCreate() {
		awm = AppWidgetManager.getInstance(getApplicationContext());
		lor = new LockOnReceiver();
		lof = new LockOffReceiver();
		registerReceiver(lor, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		registerReceiver(lof, new IntentFilter(Intent.ACTION_SCREEN_ON));
		startTimer();

		super.onCreate();
	}

	private void startTimer() {
		if (timer == null && timerTask == null) {
			timer = new Timer();
			timerTask = new TimerTask() {
				@Override
				public void run() {
					Log.d("3s", "3s");
					ComponentName provider = new ComponentName(
							UpdateWidgetService.this, MyWidget.class);
					RemoteViews views = new RemoteViews(getPackageName(),
							R.layout.process_widget);
					views.setTextViewText(
							R.id.process_count,
							"�������еĳ���"
									+ TaskManagerUtil
											.getRunningAppProcesses(getApplicationContext()));
					views.setTextViewText(
							R.id.process_memory,
							"ʣ������ڴ�"
									+ Formatter
											.formatFileSize(
													getApplicationContext(),
													TaskManagerUtil
															.getAVilMem(getApplicationContext())));
					Intent intent = new Intent();
					intent.setAction("com.consciencemobilesafe.clean");
					PendingIntent pendingIntent = PendingIntent.getBroadcast(
							getApplicationContext(), 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);

					views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
					awm.updateAppWidget(provider, views);
				}
			};
			timer.schedule(timerTask, 0, 2000);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("3s", "����ر�");
		stopTimer();
		unregisterReceiver(lor);
		unregisterReceiver(lof);
		lor = null;
		lof = null;
		
	}

	/**
	 * �رռ�ʱ��
	 */
	private void stopTimer() {
		if (timer != null && timerTask != null) {
			timer.cancel();
			timerTask.cancel();
			timer = null;
			timerTask = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * ����ʱ��
	 *
	 */
	class LockOnReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// �����ֻ�����
			stopTimer();
		}
	}

	/**
	 * ������ʱ�����
	 */
	class LockOffReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// �����ֻ�����
			startTimer();
		}
	}

}
