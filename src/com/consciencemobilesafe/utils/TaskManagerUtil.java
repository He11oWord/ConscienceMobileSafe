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
 * ���̹���Ĺ�����
 * 
 */
public class TaskManagerUtil {

	/**
	 * ��ȡ�������еĽ���
	 * 
	 * @param context
	 * @return �������е��б�
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
	 * �鿴ʣ���ڴ�
	 * 
	 * @param context
	 *            ������
	 * @return ʣ���ڴ���
	 */
	public static long getAVilMem(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;

	}

	/**
	 * �鿴�ֻ������ڴ�
	 * 
	 * @param context
	 *            ������
	 * @return ���ڴ���
	 */
	public static long getTotalMem(Context context) {
		// ��ȡ���ڴ棬4.0�����ò���
//		 ActivityManager am = (ActivityManager)
//		 context.getSystemService(context.ACTIVITY_SERVICE);
//		 MemoryInfo outInfo = new MemoryInfo();
//		 am.getMemoryInfo(outInfo);
//		 return outInfo.totalMem;;
//
//		// ��ò鿴��С��ʵ��
//		StatFs s = new StatFs(Environment.getDataDirectory().getAbsolutePath());
//		s.getBlockCount();// ��÷�������
//		long size = s.getBlockSize();// ��÷����Ĵ�С
//		long count = s.getAvailableBlocks();// ��ÿ�������ĸ���
//		return size * count;
		
		//������ʲô�汾�ж���ʹ�����������ȡϵͳ�ڴ�
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
