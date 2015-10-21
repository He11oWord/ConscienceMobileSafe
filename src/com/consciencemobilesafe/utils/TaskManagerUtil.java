package com.consciencemobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

/**
 * 进程管理的工具类
 * 
 */
public class TaskManagerUtil {

	/**
	 * 获取正在运行的进程
	 * 
	 * @param context
	 * @return 正在运行的列表
	 */
	public static List<RunningAppProcessInfo> getRunningAppProcesses(
			Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		return runningAppProcesses;
	}

	/**
	 * 查看剩余内存
	 * 
	 * @param context
	 *            上下文
	 * @return 剩余内存量
	 */
	public static long getAVilMem(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;

	}

	/**
	 * 查看手机的总内存
	 * 
	 * @param context
	 *            上下文
	 * @return 总内存量
	 */
	public static long getTotalMem(Context context) {
		// 获取总内存，4.0以下用不了
//		 ActivityManager am = (ActivityManager)
//		 context.getSystemService(context.ACTIVITY_SERVICE);
//		 MemoryInfo outInfo = new MemoryInfo();
//		 am.getMemoryInfo(outInfo);
//		 return outInfo.totalMem;;
//
//		// 获得查看大小的实例
//		StatFs s = new StatFs(Environment.getDataDirectory().getAbsolutePath());
//		s.getBlockCount();// 获得分区个数
//		long size = s.getBlockSize();// 获得分区的大小
//		long count = s.getAvailableBlocks();// 获得可用区块的个数
//		return size * count;
		
		//不管在什么版本中都能使用这个方法获取系统内存
		try {
			File file = new File("/proc/meminfo");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line =br.readLine();
			StringBuilder sb = new StringBuilder();
			for(char c :line.toCharArray()){
				if(c >= '0' && c <= '9'){
					sb.append(c);
				}
			}
			
			return Long.parseLong(sb.toString())*1024;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	
	
	}
}
