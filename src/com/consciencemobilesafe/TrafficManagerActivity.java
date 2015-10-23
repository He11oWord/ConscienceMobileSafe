package com.consciencemobilesafe;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;

/**
 * ������ѯ�Ľ���
 * @Description TODO
 * @author lizhao
 * @date 2015-10-23 ����12:45:14
 */
public class TrafficManagerActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//1���һ����������
		PackageManager packageManager = getPackageManager();
		//2.������еİ�װ����
		List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
		//3.����list���uid
		for(ApplicationInfo ai : installedApplications){
			int uid = ai.uid;
			//��ó���������������
			long tx = TrafficStats.getUidTxBytes(uid);//���͵�
			long rx = TrafficStats.getUidRxBytes(uid);//�ϴ���
			//����ֵ�����-1�����ʾû�в�������
		}
		//2.3g���õ�����
		TrafficStats.getMobileRxBytes();
		TrafficStats.getMobileTxBytes();
		//��������˿ڵ�����
		TrafficStats.getTotalRxBytes();
		TrafficStats.getTotalTxBytes();
		
	
		
		
	}
}
