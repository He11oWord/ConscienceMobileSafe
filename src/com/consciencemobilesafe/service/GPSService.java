package com.consciencemobilesafe.service;

import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.renderscript.Double2;

public class GPSService extends Service{

	private LocationManager lm;
	private MyLocationListener listener;
	SharedPreferences sp = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new MyLocationListener();
		sp = getSharedPreferences("config",MODE_PRIVATE);
		
		
		//获取最佳的定位方式,设置该方式为最佳的
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE	);
		
		
		
		// 设置参数细化：
		// criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置为最大精度
		// criteria.setAltitudeRequired(false);//不要求海拔信息
		// criteria.setBearingRequired(false);//不要求方位信息
		// criteria.setCostAllowed(true);//是否允许付费
		// criteria.setPowerRequirement(Criteria.POWER_LOW);//对电量的要求

		String bestProvider = lm.getBestProvider(criteria, true);
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//取消监听
		lm.removeUpdates(listener);
		listener = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	class MyLocationListener implements LocationListener {

		// 当位置发生改变的时候调用
		@Override
		public void onLocationChanged(Location location) {
			//
			String longitude = "纬度：" + location.getLongitude()			;
			String latitude  = "经度：" + location.getLatitude();
			String accuracy = "精度" + location.getAccuracy();
		
			//将GPS坐标改成加偏（火星）坐标
			try {
				//"axisoffset.dat"为数据库
				InputStream is = getAssets().open("axisoffset.dat");
				ModifyOffset instance = ModifyOffset.getInstance(is);
				instance.s2c(new PointDouble(location.getLatitude(), location.getLongitude()));
				//更改
				longitude =  "纬度：" + instance.Y;
				latitude  = "经度："+instance.X;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//保存位置，然后发送短信
			Editor editor = sp.edit();
			editor.putString("lastLocation", longitude+latitude+accuracy);
			editor.commit();
			
			
		}

		// 当状态发生改变的时候调用（checkBox）
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		// 当某项服务可用时调用
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		// 当某项服务不可用时调用
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}
	
	
}
