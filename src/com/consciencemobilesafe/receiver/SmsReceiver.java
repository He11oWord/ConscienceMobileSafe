package com.consciencemobilesafe.receiver;


import com.consciencemobilesafe.app.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
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
				}else if("#*location*#".equals(sendBody)){
					Log.d(TAG , "GPS׷�٣�#*location*#");
					
					
				}else if("#*lockscreen*#".equals(sendBody)){
					Log.d(TAG , "Զ��������#*lockscreen*#");
					
					
				}else if("#*wipedata*#".equals(sendBody)){
					Log.d(TAG , "Զ���������٣�#*wipedata*#");
					
					
				}else{
					Log.i(TAG , "fuck");
					System.out.println("fuck");
				}
				
				System.out.println("fuck2");
			}
	}
}	
	






