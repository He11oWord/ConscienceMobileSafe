package com.consciencemobilesafe.receiver;

import com.consciencemobilesafe.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.util.Log;

public class MyWidget extends AppWidgetProvider {

	// ������ô����������ִ���������
	@Override
	public void onReceive(Context context, Intent intent) {
//		
		super.onReceive(context, intent);
	}

	// ���µ�ʱ��ִ���������������ָ����ʱ�����ˢ�£���С��Сʱ
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Intent intent = new Intent(context, UpdateWidgetService.class);
		context.startService(intent);
		Log.d("", "����");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	// ��һ�δ�����ʱ����Ļ����û�����widget��
	public void onEnabled(Context context) {
		Intent intent = new Intent(context, UpdateWidgetService.class);
		context.startService(intent);
		Log.d("", "��һ��");

		super.onEnabled(context);
	}

	// ��ȫ����widget��ʧ��ʱ�����
	public void onDisabled(Context context) {
		Intent intent = new Intent(context, UpdateWidgetService.class);
		context.stopService(intent);
		super.onDisabled(context);
	
	}

}
