package com.consciencemobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
	
	
	private String TAG = "SmsReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("fuck0");
		//写接收短信的代码
		Object[] object = (Object[]) intent.getExtras().get("pdus");
			for(Object o : object){
				//获得具体的某一条短信
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) o);
				//发送者
				String sender = sms.getOriginatingAddress();
				//短信的内容
				String sendBody = sms.getMessageBody();
				
				
				if("#*alarm*#".equals(sendBody)){
					Log.d(TAG , "播放报警音乐：#*alarm*#");
				}else if("#*location*#".equals(sendBody)){
					Log.d(TAG , "GPS追踪：#*location*#");
				}else if("#*lockscreen*#".equals(sendBody)){
					Log.d(TAG , "远程锁屏：#*lockscreen*#");
				}else if("#*wipedata*#".equals(sendBody)){
					Log.d(TAG , "远程数据销毁：#*wipedata*#");
				}else{
					Log.i(TAG , "fuck");
					System.out.println("fuck");
				}
				
				System.out.println("fuck2");
			}
		
	}

}	
	






