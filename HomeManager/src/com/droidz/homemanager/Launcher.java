package com.droidz.homemanager;

import android.graphics.drawable.Drawable;

public class Launcher {
	protected String packageName;
	protected boolean defaultLauncher;
	protected String name;
	protected Drawable icon;
	protected boolean systemApp;
	
	public Launcher(String packageName, boolean defaultLauncher, Drawable icon, String name, boolean systemApp) {
		this.packageName = packageName;
		this.defaultLauncher = defaultLauncher;
		this.icon = icon;
		this.name = name;
		this.systemApp = systemApp;
	}
}
