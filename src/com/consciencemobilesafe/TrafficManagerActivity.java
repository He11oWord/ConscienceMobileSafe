package com.consciencemobilesafe;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;

/**
 * 流量查询的界面
 * @Description TODO
 * @author lizhao
 * @date 2015-10-23 下午12:45:14
 */
public class TrafficManagerActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//1获得一个包管理器
		PackageManager packageManager = getPackageManager();
		//2.获得所有的安装程序
		List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
		//3.遍历list获得uid
		for(ApplicationInfo ai : installedApplications){
			int uid = ai.uid;
			//获得程序所产生的流量
			long tx = TrafficStats.getUidTxBytes(uid);//发送的
			long rx = TrafficStats.getUidRxBytes(uid);//上传的
			//返回值如果是-1，则表示没有产生流量
		}
		//2.3g所用的流量
		TrafficStats.getMobileRxBytes();
		TrafficStats.getMobileTxBytes();
		//所有网络端口的流量
		TrafficStats.getTotalRxBytes();
		TrafficStats.getTotalTxBytes();
		
	
		
		
	}
}
