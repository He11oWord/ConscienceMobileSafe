package com.consciencemobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;
import android.util.Log;

import com.consciencemobilesafe.domain.TaskInfo;

/**
 * 进程信息的提供类
 * 
 */
public class TaskInfoProvider {

	/**
	 * 获得进程信息
	 * 
	 * @param context
	 * @return 进程信息类的List
	 * @throws NameNotFoundException
	 */
	public static List<TaskInfo> getTaskInfo(Context context)
			throws NameNotFoundException {
		// 进程管理器
		ActivityManager am = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		List<TaskInfo> infos = new ArrayList<TaskInfo>();
		// 包管理器
		PackageManager packageManager = context.getPackageManager();
		for (RunningAppProcessInfo r : runningAppProcesses) {
			TaskInfo info = new TaskInfo();
			// 包名称
			String packageName = r.processName;
			ApplicationInfo ai;
			
			MemoryInfo[] processMemoryInfo = am
					.getProcessMemoryInfo(new int[]{r.pid});
			long useRam = processMemoryInfo[0].getTotalPrivateDirty() * 1024;
			
			try {
				ai = packageManager.getApplicationInfo(
						packageName, 0);
				// 图标和名字
				
				Drawable icon = ai.loadIcon(packageManager);
				String name = (String) ai.loadLabel(packageManager);
				info.setIcon(icon);
				info.setName(name);
				// 是否是系统进程
				int flages = packageManager.getApplicationInfo(packageName, 0).flags;
				if ((flages & ApplicationInfo.FLAG_SYSTEM) == 0) {
					// 用户程序
					info.setUserTask(true);
				} else {
					// 系统进程
					info.setUserTask(false);
				}
				// 进程使用内存情况
				
				

			
				info.setPackageName(packageName);
				info.setUseRam(useRam);
				infos.add(info);
			} catch (Exception q) {
				q.printStackTrace();
			}
		
			

		}

		return infos;
	}
	
	
}
