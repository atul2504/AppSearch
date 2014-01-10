package com.example.applist;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdpater extends BaseAdapter implements OnClickListener{

	Context context;	
	LayoutInflater inflater;
	ArrayList<AppItemModel> mAppList;
	AppItemModel itemModel;
	
	public MyAdpater(Context context, ArrayList<AppItemModel> mAppList) {
		//Constructor to initialize context and inflater
		this.context = context;
		this.mAppList = mAppList;
		inflater = LayoutInflater.from(context);		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAppList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.single_row, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}		
		
		itemModel = (AppItemModel) getItem(position);		
		Drawable appIcon = itemModel.getIcon();
		appIcon.setBounds(0, 0, 40, 40);
		holder.lblList.setCompoundDrawables(appIcon, null, null, null);
		holder.lblList.setCompoundDrawablePadding(15);
		holder.lblList.setText(itemModel.getName());	
		
		//Sets Tag to remember the clicked item
		holder.imgAppInfo.setTag(position);
		holder.linearParent.setTag(position);
		
		holder.imgAppInfo.setOnClickListener(this);		
		holder.linearParent.setOnClickListener(this);		
		return convertView;
	}
	
	private class ViewHolder{
		TextView lblList;
		ImageView imgAppInfo;
		LinearLayout linearParent;
		
		public ViewHolder(View convertView) {
			super();
			lblList = (TextView) convertView.findViewById(R.id.lblListItem);
			imgAppInfo = (ImageView) convertView.findViewById(R.id.imgAppInfo);
			linearParent = (LinearLayout) convertView.findViewById(R.id.linearParent);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		int position = (Integer)v.getTag();
		AppItemModel packInfo = (AppItemModel) getItem(position);
		//Log.d("onClick",packInfo.getPackageName());
		
		switch (v.getId()) {
		case R.id.imgAppInfo:			
			AppData appData = (AppData) context.getApplicationContext();
			appData.setPackageInfo(packInfo);
			
			Intent intent = new Intent(context,AppInfo.class);
			//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MainActivity.fa.startActivity(intent);
			break;
			
		case R.id.linearParent:
			
			
			PackageManager packageManager = MainActivity.fa.getPackageManager();
			Intent launchIntent = packageManager .getLaunchIntentForPackage(packInfo.getPackageName());
			if (launchIntent==null){
				Toast.makeText(context, MainActivity.NO_RESPONSE_TEXT, Toast.LENGTH_SHORT).show();
			}else{
				context.startActivity(launchIntent);
			}
			if(packInfo.getPackageName().equalsIgnoreCase(MainActivity.fa.getPackageName())){
				
				MainActivity.fa.finish();
			}
			//	
			break;
			
		default:
			break;
		}
	}
	
	

}
