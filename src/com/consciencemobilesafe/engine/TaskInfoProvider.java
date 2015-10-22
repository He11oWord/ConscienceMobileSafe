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
 * ������Ϣ���ṩ��
 * 
 */
public class TaskInfoProvider {

	/**
	 * ��ý�����Ϣ
	 * 
	 * @param context
	 * @return ������Ϣ���List
	 * @throws NameNotFoundException
	 */
	public static List<TaskInfo> getTaskInfo(Context context)
			throws NameNotFoundException {
		// ���̹�����
		ActivityManager am = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		List<TaskInfo> infos = new ArrayList<TaskInfo>();
		// ��������
		PackageManager packageManager = context.getPackageManager();
		for (RunningAppProcessInfo r : runningAppProcesses) {
			TaskInfo info = new TaskInfo();
			// ������
			String packageName = r.processName;
			ApplicationInfo ai;
			
			MemoryInfo[] processMemoryInfo = am
					.getProcessMemoryInfo(new int[]{r.pid});
			long useRam = processMemoryInfo[0].getTotalPrivateDirty() * 1024;
			
			try {
				ai = packageManager.getApplicationInfo(
						packageName, 0);
				// ͼ�������
				
				Drawable icon = ai.loadIcon(packageManager);
				String name = (String) ai.loadLabel(packageManager);
				info.setIcon(icon);
				info.setName(name);
				// �Ƿ���ϵͳ����
				int flages = packageManager.getApplicationInfo(packageName, 0).flags;
				if ((flages & ApplicationInfo.FLAG_SYSTEM) == 0) {
					// �û�����
					info.setUserTask(true);
				} else {
					// ϵͳ����
					info.setUserTask(false);
				}
				// ����ʹ���ڴ����
				
				

			
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
