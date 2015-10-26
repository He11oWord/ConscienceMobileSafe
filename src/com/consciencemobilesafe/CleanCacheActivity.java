package com.consciencemobilesafe;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.consciencemobilesafe.app.R;

public class CleanCacheActivity extends Activity {

	private TextView tv_scan_status;
	private ProgressBar pb;
	private PackageManager pm;
	private int max;
	private int count;
	private LinearLayout ll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.clean_cache_layout);

		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		pb = (ProgressBar) findViewById(R.id.pb);
		ll = (LinearLayout) findViewById(R.id.ll_container);
		scanCache();

	}
	/**
	 * 扫描手机里面所有应用程序的缓存信息
	 */
	private void scanCache() {
		pm = getPackageManager();
		new Thread() {
			public void run() {
				Method getPackageSizeInfoMethod = null;
				Method[] methods = PackageManager.class.getMethods();
				for (Method method : methods) {
					if ("getPackageSizeInfo".equals(method.getName())) {
						getPackageSizeInfoMethod = method;
					}
				}
				List<PackageInfo> packInfos = pm.getInstalledPackages(0);
				pb.setMax(packInfos.size());
				int progress = 0;
				for (PackageInfo packInfo : packInfos) {
					try {
						String s = packInfo.packageName;
						System.out.println(packInfo.packageName);
						getPackageSizeInfoMethod.invoke(pm,s,new MyDataObserver());
						Thread.sleep(50);
					} catch (Exception e) {
						e.printStackTrace();
					}
					progress++;
					pb.setProgress(progress);
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						tv_scan_status.setText("扫描完毕...");
					}
				});
			};
		}.start();
	}

	private class MyDataObserver extends IPackageStatsObserver.Stub {
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			final long cache = pStats.cacheSize;
			long code = pStats.codeSize;
			long data = pStats.dataSize;
			final String packname = pStats.packageName;
			final ApplicationInfo appInfo;
			try {
				appInfo = pm.getApplicationInfo(packname, 0);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv_scan_status.setText("正在扫描：" + appInfo.loadLabel(pm));
						if (cache > 0) {
							View view = View.inflate(getApplicationContext(), R.layout.clean_cache_item_layout, null);
							TextView tv_cache = (TextView) view.findViewById(R.id.clean_cache_lv_tv1);
							tv_cache.setText("缓存大小:"+Formatter.formatFileSize(getApplicationContext(), cache));
							TextView tv_name = (TextView) view.findViewById(R.id.clean_cache_lv_tv2);
							tv_name.setText(appInfo.loadLabel(pm));
							ImageView iv_delete = (ImageView) view.findViewById(R.id.clean_cache_lv_button);
							iv_delete.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									//deleteApplicationCacheFiles
									try {
										Method method = PackageManager.class.getMethod("deleteApplicationCacheFiles", String.class,
												IPackageDataObserver.class
												);
										//method.invoke(pm, packname,new MypackDataObserver());
									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}
							});
							ll.addView(view,0);
						}
					}
				});
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	

//	private class MyPackageDataObserver extends IPackageDataObserver.Stub {
//
//		@Override
//		public void onRemoveCompleted(String packageName, boolean succeeded)
//				throws RemoteException {
//			Log.d("我来了", "sdasdasd");
//
//		}
//	}

	/**
	 * 清理手机的全部缓存.
	 * 
	 * @param view
	 */
//	public void cleanAll(View view) {
//		Method[] methods = PackageManager.class.getMethods();
//		for (Method method : methods) {
//			if ("freeStorageAndNotify".equals(method.getName())) {
//				try {
//					method.invoke(pm, Integer.MAX_VALUE,
//							new MyPackageDataObserver());
//					runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							ll.removeAllViews();
//							ll.notifyAll();
//						}
//					});
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return;
//			}
//		}
//	}

}
