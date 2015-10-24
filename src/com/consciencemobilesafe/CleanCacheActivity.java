package com.consciencemobilesafe;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.ProgressBar;
import android.widget.TextView;
import  android.content.pm.IPackageDataObserver;
import com.consciencemobilesafe.app.R;
import  android.content.pm.IPackageStatsObserver;

public class CleanCacheActivity extends Activity {

	private TextView tv;
	private ProgressBar pb;
	private PackageManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.clean_cache_layout);
		pb = (ProgressBar) findViewById(R.id.clean_cache_pb);
		tv = (TextView) findViewById(R.id.clean_cache_tv);

		pb.setMax(100);
		new Thread() {
			public void run() {
				for (int i = 0; i < 100; i++) {
					pb.setProgress(i);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}.start();

		scanCache();

	}

	private void scanCache() {
		pm = getPackageManager();

		// ���������з���
		final Method[] methods = PackageManager.class.getMethods();
		new Thread() {
			public void run() {
				Method packagesizeMethod;
				//�������еķ������ҵ�������Ҫ���Ǹ�
				for (Method m : methods) {
					System.out.println(m.getName());
					if (m.getName().equals("getPackageSizeInfo")) {
						packagesizeMethod = m;
					}
				}
				
				List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
				for(PackageInfo p : packageInfos){
					//����Ҫ�ҵķ����Ķ��� public abstract void getPackageSizeInfo(String packageName,
//		            IPackageStatsObserver observer);
					//invoke������������
					//����I��ͷService��βһ��ΪԶ�̷�������aidl�ļ�һ��Ϳ�����
//					packagesizeMethod.invoke(pm, p.packageName,)
				}
				
			}
		}.start();
	}
										
	private class MyDataObserver extends IPackageStatsObserver.Stub{

	
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
}
