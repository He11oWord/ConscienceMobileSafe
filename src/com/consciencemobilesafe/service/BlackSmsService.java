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
	 * 电话监听,拦截黑名单电话
	 * @author 钊
	 *
	 */
	private class PListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// state 状态，incomingNumber来电号码
			super.onCallStateChanged(state, incomingNumber);
			BlackNumberDBUtil b =new BlackNumberDBUtil(getApplicationContext());
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 来电铃声响起
				String result = b.queryMode(incomingNumber);
				if("1".equals(result)||"3".equals(result)){
					endCall();					
					Log.d("电话响起", "挂掉电话");
				}
				break;
			
			default:
				break;
			}
		}
	}

	@Override
	public void onCreate() {
		
		// 注册监听短信显示事件
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		//filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		bsr = new BlackSmsReceiver();
		filter.setPriority(1000);
		registerReceiver(bsr, filter);
		System.out.println("开启监听服务");
		pListener = new PListener();
		
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		
		tm.listen(pListener, PhoneStateListener.LISTEN_CALL_STATE);

		super.onCreate();
	}
	/**
	 * 拦截短信的广播
	 * @author 钊
	 *
	 */
	private class BlackSmsReceiver extends BroadcastReceiver {

		// 该广播接收者需要在该文本中进行注册,在下文中registerReceiver方法
		public void onReceive(Context context, Intent intent) {
//			DevicePolicyManager dpm = (DevicePolicyManager) context
//					.getSystemService(context.DEVICE_POLICY_SERVICE);
			
			//用来查数据库的工具，这里主要用来查拦截模式
			BlackNumberDBUtil b = new BlackNumberDBUtil(getApplicationContext());
			
			// 写接收短信的代码
			Object[] object = (Object[]) intent.getExtras().get("pdus");
			for (Object o : object) {
				// 获得具体的某一条短信
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) o);
				// 发送者
				String sender = sms.getOriginatingAddress();
				String mode = b.queryMode(sender);
				if("3".equals(mode)||"2".equals(mode)){
					Log.d("这是黑名单", "这是黑名单");
					//这是用来拦截短信的
					abortBroadcast();
				}
				    
				Log.d("收到短信了","收到短信了");
			}
		
		}
	}

	/**
	 * 挂断电话
	 */
	public void endCall() {
		//利用反射的机制，拿到ServiceManager
		//寻找里面的方法
		//拿到IBinder对象
		//导入隐藏的包，即可找到endCall()方法
		//IBinder iBinder = ServiceManager.getService(TELEPHONY_SERVICE);
		try {
			Log.d("endf", "挂掉电话");
////		//加载servicemanager的字节码
//			Class clazz = BlackSmsService.class.getClassLoader().loadClass("android.os.ServiceManager");
//			Method method = clazz.getDeclaredMethod("getService", String.class);
//			IBinder ibinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
//			ITelephony.Stub.asInterface(ibinder).endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
