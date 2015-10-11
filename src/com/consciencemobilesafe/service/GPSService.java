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
		
		
		//��ȡ��ѵĶ�λ��ʽ,���ø÷�ʽΪ��ѵ�
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE	);
		
		
		
		// ���ò���ϸ����
		// criteria.setAccuracy(Criteria.ACCURACY_FINE);//����Ϊ��󾫶�
		// criteria.setAltitudeRequired(false);//��Ҫ�󺣰���Ϣ
		// criteria.setBearingRequired(false);//��Ҫ��λ��Ϣ
		// criteria.setCostAllowed(true);//�Ƿ�������
		// criteria.setPowerRequirement(Criteria.POWER_LOW);//�Ե�����Ҫ��

		String bestProvider = lm.getBestProvider(criteria, true);
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//ȡ������
		lm.removeUpdates(listener);
		listener = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	class MyLocationListener implements LocationListener {

		// ��λ�÷����ı��ʱ�����
		@Override
		public void onLocationChanged(Location location) {
			//
			String longitude = "γ�ȣ�" + location.getLongitude()			;
			String latitude  = "���ȣ�" + location.getLatitude();
			String accuracy = "����" + location.getAccuracy();
		
			//��GPS����ĳɼ�ƫ�����ǣ�����
			try {
				//"axisoffset.dat"Ϊ���ݿ�
				InputStream is = getAssets().open("axisoffset.dat");
				ModifyOffset instance = ModifyOffset.getInstance(is);
				instance.s2c(new PointDouble(location.getLatitude(), location.getLongitude()));
				//����
				longitude =  "γ�ȣ�" + instance.Y;
				latitude  = "���ȣ�"+instance.X;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//����λ�ã�Ȼ���Ͷ���
			Editor editor = sp.edit();
			editor.putString("lastLocation", longitude+latitude+accuracy);
			editor.commit();
			
			
		}

		// ��״̬�����ı��ʱ����ã�checkBox��
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		// ��ĳ��������ʱ����
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		// ��ĳ����񲻿���ʱ����
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}
	
	
}
