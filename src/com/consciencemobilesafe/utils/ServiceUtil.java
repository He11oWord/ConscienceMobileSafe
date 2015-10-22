package com.consciencemobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * У��ĳ�������Ƿ񻹻���
 * serviceName��Ҫ���ķ�������
 */
public class ServiceUtil {
	
	/**
	 * У��ĳ�������Ƿ񻹻���
	 * serviceName��Ҫ���ķ�������
	 */
	
	public boolean isSeriver(Context context,String serviceName){
		//У������Ƿ����
		
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		for(RunningServiceInfo rsi : runningServices){
			String name = rsi.service.getClassName();
			if(serviceName.equals(name)){
				return true;
			}
			
		}
		
		return false;
	}
}
