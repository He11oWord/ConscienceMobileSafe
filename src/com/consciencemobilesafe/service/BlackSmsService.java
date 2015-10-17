package com.consciencemobilesafe.service;


import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.consciencemobilesafe.utils.BlackNumberDBUtil;
import com.consciencemobilesafe.utils.NumberQueryUtil;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BlackSmsService extends Service {

	private BlackSmsReceiver bsr;
	private TelephonyManager tm;
	private PListener pListener;

	public void onDestroy() {
	
	
		unregisterReceiver(bsr);
		bsr = null;
		tm.listen(pListener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	/**
	 * �绰����,���غ������绰
	 * @author ��
	 *
	 */
	private class PListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// state ״̬��incomingNumber�������
			super.onCallStateChanged(state, incomingNumber);
			BlackNumberDBUtil b =new BlackNumberDBUtil(getApplicationContext());
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// ������������
				String result = b.queryMode(incomingNumber);
				if("1".equals(result)||"3".equals(result)){
					endCall();					
					Log.d("�绰����", "�ҵ��绰");
				}
				break;
			
			default:
				break;
			}
		}
	}

	@Override
	public void onCreate() {
		
		// ע�����������ʾ�¼�
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		//filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		bsr = new BlackSmsReceiver();
		filter.setPriority(1000);
		registerReceiver(bsr, filter);
		System.out.println("������������");
		pListener = new PListener();
		
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		
		tm.listen(pListener, PhoneStateListener.LISTEN_CALL_STATE);

		super.onCreate();
	}
	/**
	 * ���ض��ŵĹ㲥
	 * @author ��
	 *
	 */
	private class BlackSmsReceiver extends BroadcastReceiver {

		// �ù㲥��������Ҫ�ڸ��ı��н���ע��,��������registerReceiver����
		public void onReceive(Context context, Intent intent) {
//			DevicePolicyManager dpm = (DevicePolicyManager) context
//					.getSystemService(context.DEVICE_POLICY_SERVICE);
			
			//���������ݿ�Ĺ��ߣ�������Ҫ����������ģʽ
			BlackNumberDBUtil b = new BlackNumberDBUtil(getApplicationContext());
			
			// д���ն��ŵĴ���
			Object[] object = (Object[]) intent.getExtras().get("pdus");
			for (Object o : object) {
				// ��þ����ĳһ������
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) o);
				// ������
				String sender = sms.getOriginatingAddress();
				String mode = b.queryMode(sender);
				if("3".equals(mode)||"2".equals(mode)){
					Log.d("���Ǻ�����", "���Ǻ�����");
					//�����������ض��ŵ�
					abortBroadcast();
				}
				    
				Log.d("�յ�������","�յ�������");
			}
		
		}
	}

	/**
	 * �Ҷϵ绰
	 */
	public void endCall() {
		//���÷���Ļ��ƣ��õ�ServiceManager
		//Ѱ������ķ���
		//�õ�IBinder����
		//�������صİ��������ҵ�endCall()����
		//IBinder iBinder = ServiceManager.getService(TELEPHONY_SERVICE);
		try {
			Log.d("endf", "�ҵ��绰");
////		//����servicemanager���ֽ���
//			Class clazz = BlackSmsService.class.getClassLoader().loadClass("android.os.ServiceManager");
//			Method method = clazz.getDeclaredMethod("getService", String.class);
//			IBinder ibinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
//			ITelephony.Stub.asInterface(ibinder).endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
