package com.consciencemobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 校验某个服务是否还活着
 * serviceName需要检测的服务名称
 */
public class ServiceUtil {
	
	/**
	 * 校验某个服务是否还活着
	 * serviceName需要检测的服务名称
	 */
	
	public boolean isSeriver(Context context,String serviceName){
		//校验服务是否活着
		
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
