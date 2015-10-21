package com.consciencemobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * 进程信息
 */
public class TaskInfo {
	private String packageName;
	private String name;
	private boolean isUserTask;
	private long useRam;
	private Drawable icon;
	private boolean isCheck;

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUserTask() {
		return isUserTask;
	}

	public void setUserTask(boolean isUserTask) {
		this.isUserTask = isUserTask;
	}

	public long getUseRam() {
		return useRam;
	}

	public void setUseRam(long useRam) {
		this.useRam = useRam;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "TaskInfo [packName=" + packageName + ", name=" + name
				+ ", isUserTask=" + isUserTask + ", useRam=" + useRam + "]";
	}

}
