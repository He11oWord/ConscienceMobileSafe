package com.consciencemobilesafe.receiver;

import com.consciencemobilesafe.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.util.Log;

public class MyWidget extends AppWidgetProvider {

	// 不管怎么操作，都会执行这个方法
	@Override
	public void onReceive(Context context, Intent intent) {
//		
		super.onReceive(context, intent);
	}

	// 更新的时候，执行这个方法，根据指定的时间进行刷新，最小半小时
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Intent intent = new Intent(context, UpdateWidgetService.class);
		context.startService(intent);
		Log.d("", "打开了");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	// 第一次创建的时候（屏幕上面没有这个widget）
	public void onEnabled(Context context) {
		Intent intent = new Intent(context, UpdateWidgetService.class);
		context.startService(intent);
		Log.d("", "第一次");

		super.onEnabled(context);
	}

	// 当全部的widget消失的时候调用
	public void onDisabled(Context context) {
		Intent intent = new Intent(context, UpdateWidgetService.class);
		context.stopService(intent);
		super.onDisabled(context);
	
	}

}
