package com.consciencemobilesafe.utils;

import java.util.ArrayList;
import java.util.List;

import com.consciencemobilesafe.domain.AppInfo;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * 应用信息的提供类
 */
public class AppInfoProvider {
	
	/**
	 * 获得App信息
	 * @param context上下文
	 * @return 每个App的资料的集合
	 */
	public static List<AppInfo> getAppInfo(Context context){
		
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		//包管理器
		PackageManager packageManager = context.getPackageManager();
		//已经安装了的包
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
		for(PackageInfo p : installedPackages){
			//获得包名，图片，名字，内存占用点
			String packName = p.packageName;
			Drawable icon = p.applicationInfo.loadIcon(packageManager);
			String name = p.applicationInfo.loadLabel(packageManager).toString();
			int uid = p.applicationInfo.uid;//安装程序的编号
			AppInfo appInfo = new AppInfo();
			appInfo.setPackName(packName);
			appInfo.setIcon(icon);
			appInfo.setName(name);
			//获得手机在什么位置
			int flags = p.applicationInfo.flags;
			//进行 与运算，如果能重叠，说明了是系统内部的
			if((flags&ApplicationInfo.FLAG_SYSTEM) == 0){
				//用户程序	
				appInfo.setUserApp(true);
			}else{
			//系统程序
				appInfo.setUserApp(false);
			}
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0){
				//手机存储
				appInfo.setRam(true);
			}else{
				//外部存储
				appInfo.setRam(false);
			}
			
			appInfos.add(appInfo);
			
		}
		
		
		
		return appInfos;
	}
	
}
