package com.example.applist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private PackageManager packageManager=null;
	private MyAdpater adapter=null;
	private ListView lstAppList;
	private EditText txtSearch;	
	private List<PackageInfo> packageList=null;	
	public static Activity fa=null;
	public static final String NO_RESPONSE_TEXT = "Can't Open System App!";
	private static final String NO_MATCH_TEXT = "No Application found!";
	private static boolean isToast = true;
	private ImageView imgNotFound;

	private ArrayList<AppItemModel> mAppList=null;
	private ArrayList<AppItemModel> originalList=null;
	private MyTask mTask=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		//initializing
		fa = this;
		imgNotFound = (ImageView) findViewById(R.id.imgNotFound);
		mAppList = new ArrayList<AppItemModel>();
		originalList = new ArrayList<AppItemModel>();
		packageManager = getPackageManager();
		
		packageList = packageManager
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		for (PackageInfo packageInfo : packageList) {
			// boolean b = isSystemPackage(packageInfo);
			// if(!b){
			addDataToMap(packageInfo);
			// }
		}		
		
		lstAppList = (ListView) findViewById(R.id.lstAppList);
		adapter = new MyAdpater(this, mAppList);
		lstAppList.setAdapter(adapter);
		originalList.addAll(mAppList);
		
		searchFunctionality();
	}

	
	private boolean isSystemPackage(PackageInfo packageInfo) {
		return ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
				: false;
	}	
	
	private void addDataToMap(PackageInfo packageInfo) {
		AppItemModel item = new AppItemModel();
		item.setIcon(packageManager.getApplicationIcon
				(packageInfo.applicationInfo));
		item.setName(packageManager.getApplicationLabel(packageInfo.applicationInfo)
				.toString());
		item.setPackageName(packageInfo.packageName);
		item.setTarget(String.valueOf(packageInfo.applicationInfo.targetSdkVersion));
		item.setVersion(packageInfo.versionName);
		item.setPath(packageInfo.applicationInfo.sourceDir);
		item.setInstallTime(setDateFormat(packageInfo.firstInstallTime));
		item.setLastUpdated(setDateFormat(packageInfo.firstInstallTime));
		
		if(packageInfo.reqFeatures != null){
			item.setFeatures(getFeatures(packageInfo.reqFeatures));
		}else{
			item.setFeatures(" - ");
		}
		
		// uses-permission
        if (packageInfo.requestedPermissions != null)
        	item.setPermissions(getPermissions(packageInfo.requestedPermissions));
        else{
        	item.setPermissions(" - ");
        }
		
		mAppList.add(item);
	}
	
	// Convert string array to comma separated string
    private String getFeatures(FeatureInfo[] reqFeatures) {
        String features = "";
        for (int i = 0; i < reqFeatures.length; i++) {
            features = features + reqFeatures[i] + "\n";
        }
        return features;
    }
	
	 // Convert string array to comma separated string
    private String getPermissions(String[] requestedPermissions) {
        String permission = "";
        for (int i = 0; i < requestedPermissions.length; i++) {
            permission = permission + requestedPermissions[i] + "\n";
        }
        return permission;
    }

	@SuppressLint("SimpleDateFormat")
	private String setDateFormat(long firstInstallTime) {
		// TODO Auto-generated method stub
		Date date = new Date(firstInstallTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String strDate = sdf.format(date);
		return strDate;
	}
	
	private void searchFunctionality() {
		txtSearch = (EditText) findViewById(R.id.txtSearch);
		txtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				String searchString = txtSearch.getText().toString();
				
				if (mTask != null){
					if(mTask.getStatus() == AsyncTask.Status.RUNNING){						
						mTask.cancel(true);
						Log.i("mTask","Cancelled");
					}
					if(mTask.m_isFinished){
						mTask.updateUI(mAppList);
					}
		        }				
				mTask = new MyTask();
				mTask.mActivity = MainActivity.this;
				mTask.execute(searchString);		        				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}
	
	@Override
    public void onDestroy()
    {
        super.onDestroy();        
        mTask.mActivity = null;
        if (this.isFinishing()){
        	mTask.cancel(false);
        }
    }	

	class MyTask extends AsyncTask<String, Void, ArrayList<AppItemModel>> {				
				
		ArrayList<AppItemModel> tempList;
		MainActivity mActivity = null;
		boolean m_isFinished  = false;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tempList = new ArrayList<AppItemModel>();			
		}	
		
		@Override
		protected ArrayList<AppItemModel> doInBackground(String... params) {
			String searchString = params[0];			
			int textLength = searchString.length();
						
			if(textLength>0){				
				for (int i = 0; i < originalList.size(); i++) {
					
					if(isCancelled()) break;
					
					String appName = originalList.get(i).getName();
					Log.i("mTask",String.valueOf(isCancelled()) + "-- " + appName);
					if (textLength <= appName.length()) {
						if (searchString.equalsIgnoreCase(appName.substring(0,
								textLength))) {
//							boolean b = isSystemPackage(packageList.get(i));
//							if(!b){
							tempList.add(originalList.get(i));
							//publishProgress(packageList.get(i));
//							}
	
						}
					}
				}
			}else{				
				tempList.addAll(originalList);				
			}
			return tempList;
		}	    

		@Override
		protected void onPostExecute(ArrayList<AppItemModel> result) {
			super.onPostExecute(result);	
			Log.i("mTask","onPostExecute is called");
			if (mActivity != null)
            {
				mAppList.clear();
				if (result.size() > 0) {					
					mAppList.addAll(result);
				}
				updateUI(mAppList);
				m_isFinished = true;
            }
		}
		
		
		public void updateUI(ArrayList<AppItemModel> appList)
		{	
				//do nothing
			if(appList.size()>0){
				lstAppList.setVisibility(View.VISIBLE);
				imgNotFound.setVisibility(View.GONE);
				isToast = true;
			} else {
				lstAppList.setVisibility(View.GONE);
				imgNotFound.setVisibility(View.VISIBLE);				
				if(isToast){
					Toast.makeText(mActivity, NO_MATCH_TEXT,
							Toast.LENGTH_SHORT).show();
					isToast = false;
				}
			}			
			MainActivity.this.adapter.notifyDataSetChanged();
		}
		
	}


}
