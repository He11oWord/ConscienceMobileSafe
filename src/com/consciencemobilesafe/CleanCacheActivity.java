package com.consciencemobilesafe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.consciencemobilesafe.app.R;

public class CleanCacheActivity extends Activity {

	private TextView tv;
	private ProgressBar pb;
	private PackageManager pm;
	private int max;
	private int count;

	private LinearLayout ll;
	private Method deleteApplicationCacheFilesMethod = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.clean_cache_layout);

		tv = (TextView) findViewById(R.id.tv_scan_status);
		pb = (ProgressBar) findViewById(R.id.pb);
		ll = (LinearLayout) findViewById(R.id.ll_container);
		scanCache();

	}

	private void scanCache() {
		pm = getPackageManager();

		// 获得类的所有方法
		final Method[] methods = PackageManager.class.getMethods();
		new Thread() {

			public void run() {
				Method packagesizeMethod = null;
				// 遍历所有的方法，找到我们想要的那个
				for (Method m : methods) {
					System.out.println(m.getName());
					if (m.getName().equals("getPackageSizeInfo")) {
						packagesizeMethod = m;
					}

					if (m.getName().equals("deleteApplicationCacheFiles")) {
						deleteApplicationCacheFilesMethod = m;
					}
				}

				List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
				max = packageInfos.size();
				pb.setMax(max);
				count = 1;
				for (PackageInfo p : packageInfos) {
					// 这是要找的方法的定义 public abstract void getPackageSizeInfo(String
					// packageName,
					// IPackageStatsObserver observer);
					// invoke后面跟多个参数
					// 后面I开头Service结尾一般为远程服务，搜索aidl文件一般就可以了
					try {
						packagesizeMethod.invoke(pm, p.packageName,
								new MyDataObserver());
						pb.setProgress(count);
						count++;
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						tv.setText("扫描完成");
					}
				});

			}
		}.start();
	}

	private class MyDataObserver extends IPackageStatsObserver.Stub {

		private ApplicationInfo applicationInfo;

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			final long cache = pStats.cacheSize;
			long code = pStats.codeSize;
			long data = pStats.dataSize;
			final String packname = pStats.packageName;
			// System.out.print("cache" + cache);
			// System.out.print("code" + code);
			// System.out.print("data" + data);
			// System.out.print("packname" + packname);
			try {
				applicationInfo = pm.getApplicationInfo(packname, 0);
				runOnUiThread(new Runnable() {
					public void run() {
						tv.setText("正在扫描：" + applicationInfo.loadLabel(pm));

						if (cache > 0) {
							View view = View.inflate(getApplicationContext(),
									R.layout.clean_cache_item_layout, null);
							TextView tv1 = (TextView) view
									.findViewById(R.id.clean_cache_lv_tv1);
							TextView tv2 = (TextView) view
									.findViewById(R.id.clean_cache_lv_tv2);
							ImageButton b = (ImageButton) view
									.findViewById(R.id.clean_cache_lv_button);
							tv1.setText(applicationInfo.loadLabel(pm));
							tv2.setText(Formatter.formatFileSize(
									getApplicationContext(), cache));
							b.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									// intent.setClassName(getApplicationContext(),"com.android.settings.InstalledAppDetails");
									// intent.
									intent.setClassName("com.android.settings",
											"com.android.settings.InstalledAppDetails");
									startActivity(intent);
									// I/ActivityManager(58): Starting activity:
									// Intent
									// { act=android.intent.action.VIEW
									// cmp=com.android.settings/
									// .InstalledAppDetails (has extras) }

									// 10-24 15:07:20.047:
									// I/ActivityManager(58): Starting activity:
									// Intent
									// {
									// cmp=com.consciencemobilesafe.app/com.consciencemobilesafe.HomeActivity
									// }

								}

							});
							// 清除单条的方式
							// deleteApplicationCacheFiles
							// // public abstract void
							// deleteApplicationCacheFiles(String packageName,
							// // IPackageDataObserver observer);
							// @Override
							// public void onClick(View v) {
							// try {
							// deleteApplicationCacheFilesMethod.invoke(pm,
							// packname,new MyPackageDataObserver() );
							// } catch (IllegalArgumentException e) {
							// // TODO Auto-generated catch block
							// e.printStackTrace();
							// } catch (IllegalAccessException e) {
							// // TODO Auto-generated catch block
							// e.printStackTrace();
							// } catch (InvocationTargetException e) {
							// // TODO Auto-generated catch block
							// e.printStackTrace();
							// }
							// }
							// });

							ll.addView(view, 0);
						}
					}
				});

			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private class MyPackageDataObserver extends IPackageDataObserver.Stub {

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			Log.d("我来了", "sdasdasd");

		}

	}

	/**
	 * 清理手机的全部缓存.
	 * 
	 * @param view
	 */
	public void cleanAll(View view) {
		Method[] methods = PackageManager.class.getMethods();
		for (Method method : methods) {
			if ("freeStorageAndNotify".equals(method.getName())) {
				try {
					method.invoke(pm, Integer.MAX_VALUE,
							new MyPackageDataObserver());
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							ll.removeAllViews();
							ll.notifyAll();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}

}
