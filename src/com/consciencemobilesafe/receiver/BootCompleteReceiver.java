package com.consciencemobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;

	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		tm = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);

		if (sp.getBoolean("protecting", false)) {
			// 获得之前保存的sim卡序列号
			String savedSim = sp.getString("simSerial", null)+"sdad";
			// 获取当前sim卡的序列号
			String realSim = tm.getSimSerialNumber();
			// 比较序列号是否相同
			if (savedSim.equals(realSim)) {
				// 相同就不做什么
				Toast.makeText(context, "sim卡未变更", 1).show();
				System.out.println("sim卡未变更");
			} else {
				// 2次开机的Sim信息不同,这时候就要发送短信给安全号码
				Toast.makeText(context, "sim卡已经变更", 1).show();
				System.out.println("sim卡已经变更");
				SmsManager.getDefault().sendTextMessage(
						sp.getString("safe_phone", ""), null, "sim卡变更", null,
						null);
			}
		}
	}
}
