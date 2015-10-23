package com.consciencemobilesafe.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {

	private String packName;
	private String name;
	private boolean isRam;
	private boolean isUserApp;
	private Drawable icon;
	private int uid;
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public boolean isUserApp() {
		return isUserApp;
	}

	public void setUserApp(boolean isUserApp) {
		this.isUserApp = isUserApp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRam() {
		return isRam;
	}

	public void setRam(boolean isRam) {
		this.isRam = isRam;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "name" + name + "isRam";
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

}
