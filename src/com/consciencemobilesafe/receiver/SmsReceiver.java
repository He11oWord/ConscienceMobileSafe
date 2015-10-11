package com.consciencemobilesafe.receiver;


import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.service.GPSService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
	
	
	private String TAG = "SmsReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("fuck0");
		//д���ն��ŵĴ���
		Object[] object = (Object[]) intent.getExtras().get("pdus");
			for(Object o : object){
				//��þ����ĳһ������
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) o);
				//������
				String sender = sms.getOriginatingAddress();
				//���ŵ�����
				String sendBody = sms.getMessageBody();
				
				
				if("#*alarm*#".equals(sendBody)){
					Log.d(TAG , "���ű������֣�#*alarm*#");
					MediaPlayer mp = MediaPlayer.create(context,R.raw.ylzs);
					//����ѭ��
					mp.setLooping(false);
					mp.setVolume(1.0f, 1.0f);
					mp.start();
					
					//�رչ㲥
					abortBroadcast();
				}else if("#*location*#".equals(sendBody)){
					Log.d(TAG , "GPS׷�٣�#*location*#");
					Intent i = new Intent(context,GPSService.class);
					context.startService(i);
					SharedPreferences sp = context.getSharedPreferences("config",context.MODE_PRIVATE);
					String lastLocation = sp.getString("lastLocation", null);
					if(TextUtils.isEmpty(lastLocation)){
						SmsManager s = SmsManager.getDefault();
						s.sendTextMessage(sender, null, "get location...", null, null);
					}else{
						SmsManager s = SmsManager.getDefault();
						s.sendTextMessage(sender, null, lastLocation, null, null);
					}
					
					//�رչ㲥
					abortBroadcast();
				}else if("#*lockscreen*#".equals(sendBody)){
					Log.d(TAG , "Զ��������#*lockscreen*#");
					
					//�رչ㲥
					abortBroadcast();
				}else if("#*wipedata*#".equals(sendBody)){
					Log.d(TAG , "Զ���������٣�#*wipedata*#");
					
					//�رչ㲥
					abortBroadcast();
				}else{
					Log.i(TAG , "fuck");
					System.out.println("fuck");
				}
				
				System.out.println("fuck2");
			}
	}
}	
	






