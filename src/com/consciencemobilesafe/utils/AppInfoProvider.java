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
 * Ӧ����Ϣ���ṩ��
 */
public class AppInfoProvider {
	
	/**
	 * ���App��Ϣ
	 * @param context������
	 * @return ÿ��App�����ϵļ���
	 */
	public static List<AppInfo> getAppInfo(Context context){
		
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		//��������
		PackageManager packageManager = context.getPackageManager();
		//�Ѿ���װ�˵İ�
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
		for(PackageInfo p : installedPackages){
			//��ð�����ͼƬ�����֣��ڴ�ռ�õ�
			String packName = p.packageName;
			Drawable icon = p.applicationInfo.loadIcon(packageManager);
			String name = p.applicationInfo.loadLabel(packageManager).toString();
			int uid = p.applicationInfo.uid;//��װ����ı��
			AppInfo appInfo = new AppInfo();
			appInfo.setPackName(packName);
			appInfo.setIcon(icon);
			appInfo.setName(name);
			//����ֻ���ʲôλ��
			int flags = p.applicationInfo.flags;
			//���� �����㣬������ص���˵������ϵͳ�ڲ���
			if((flags&ApplicationInfo.FLAG_SYSTEM) == 0){
				//�û�����	
				appInfo.setUserApp(true);
			}else{
			//ϵͳ����
				appInfo.setUserApp(false);
			}
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0){
				//�ֻ��洢
				appInfo.setRam(true);
			}else{
				//�ⲿ�洢
				appInfo.setRam(false);
			}
			
			appInfos.add(appInfo);
			
		}
		
		
		
		return appInfos;
	}
	
}
