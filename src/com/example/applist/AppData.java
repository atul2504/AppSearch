package com.example.applist;

import android.app.Application;
import android.content.pm.PackageInfo;

public class AppData extends Application{
	private AppItemModel packageInfo;

	public AppItemModel getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(AppItemModel packageInfo) {
		this.packageInfo = packageInfo;
	}
	
}
