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
			// ���֮ǰ�����sim�����к�
			String savedSim = sp.getString("simSerial", null)+"sdad";
			// ��ȡ��ǰsim�������к�
			String realSim = tm.getSimSerialNumber();
			// �Ƚ����к��Ƿ���ͬ
			if (savedSim.equals(realSim)) {
				// ��ͬ�Ͳ���ʲô
				Toast.makeText(context, "sim��δ���", 1).show();
				System.out.println("sim��δ���");
			} else {
				// 2�ο�����Sim��Ϣ��ͬ,��ʱ���Ҫ���Ͷ��Ÿ���ȫ����
				Toast.makeText(context, "sim���Ѿ����", 1).show();
				System.out.println("sim���Ѿ����");
				SmsManager.getDefault().sendTextMessage(
						sp.getString("safe_phone", ""), null, "sim�����", null,
						null);
			}
		}
	}
}