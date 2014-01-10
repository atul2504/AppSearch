package com.example.applist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AppInfo extends Activity implements OnClickListener {

	TextView appLabel, packageName, version, features;
	TextView permissions, andVersion, installed, lastModify, path;
	PackageInfo packageInfo;
	ImageView imgBack;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_info);
		findViewsById();
		Log.d("Eror","Line before appData");
		AppData appData = (AppData) MainActivity.fa.getApplicationContext();
		AppItemModel itemModel = appData.getPackageInfo(); 
		
		Log.d("Eror","Line after appData");
		setValues(itemModel);
	}	
	
	@SuppressLint("NewApi")
	private void setValues(AppItemModel itemModel) {
		// Setting Applications data

		// APP name
		appLabel.setText(itemModel.getName());

		// package name
		packageName.setText(itemModel.getPackageName());
		
		// version name
		version.setText(itemModel.getVersion());

		// target Version
		andVersion.setText(itemModel.getTarget());

		// path
		path.setText(itemModel.getPath());

		// first installation
		installed.setText(itemModel.getInstallTime());
		
		//last Modified
		lastModify.setText(itemModel.getLastUpdated());
		
		//features
		
            features.setText(itemModel.getFeatures());
		
		
		// uses-permission
        
            permissions.setText(itemModel.getPermissions());
        

	}

	
	private void findViewsById() {
		appLabel = (TextView) findViewById(R.id.applabel);
		packageName = (TextView) findViewById(R.id.package_name);
		version = (TextView) findViewById(R.id.version_name);
		features = (TextView) findViewById(R.id.req_feature);
		permissions = (TextView) findViewById(R.id.req_permission);
		andVersion = (TextView) findViewById(R.id.andversion);
		path = (TextView) findViewById(R.id.path);
		installed = (TextView) findViewById(R.id.insdate);
		lastModify = (TextView) findViewById(R.id.last_modify);
		imgBack = (ImageView) findViewById(R.id.imgBack);
		imgBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}

}
