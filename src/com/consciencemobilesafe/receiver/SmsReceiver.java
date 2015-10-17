package com.consciencemobilesafe.receiver;


import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.service.GPSService;

public class SmsReceiver extends BroadcastReceiver {
	
	
	private String TAG = "SmsReceiver";
	//最高管理员权限
	private DevicePolicyManager dpm;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
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
					MediaPlayer mp = MediaPlayer.create(context,R.raw.ylzs);
					//设置循环
					mp.setLooping(false);
					mp.setVolume(1.0f, 1.0f);
					mp.start();
					
					//关闭广播
					abortBroadcast();
				}else if("#*location*#".equals(sendBody)){
					Log.d(TAG , "GPS追踪：#*location*#");
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
					
					//关闭广播
					abortBroadcast();
					
					//远程锁屏
				}else if("#*lockscreen*#".equals(sendBody)){
					Log.d(TAG , "远程锁屏：#*lockscreen*#");
					//创建意图
					Intent intent1 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
					// mDeviceAdminSample是我要激活谁
					ComponentName mDeviceAdminSample = new ComponentName(context, MyAdmin.class);
					
			        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
			        
			        //描述字段
			        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
			              "打开即可享受一键锁屏的快感");
			        context.startActivity(intent1);
					
			      //判断是否打开了管理员权限
					if(dpm.isAdminActive(mDeviceAdminSample)){
						dpm.lockNow();//锁屏
						//dpm.resetPassword("1",0);//设置锁屏密码
						//清除SD卡的数据
						//dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
						//恢复出厂设置
						//dpm.wipeData(0);
					}else{
						Toast.makeText(context, "还未打开管理员权限", 0).show();
						return;
					}
			        
					//关闭广播
					abortBroadcast();
				}else if("#*wipedata*#".equals(sendBody)){
					Log.d(TAG , "远程数据销毁：#*wipedata*#");
					
					//关闭广播
					abortBroadcast();
				}else{
				}
			}
			
			
			
	}

	


}	
	






