package com.consciencemobilesafe.service;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.utils.NumberQueryUtil;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class NumberQueryService extends Service {

	private TelephonyManager tm;
	private MyListenerPhone mlp;
	private SharedPreferences sp;

	//ȥ�������
	private OutCallQueryReceiver outPhone;
	
	//���ڿؼ�
	private WindowManager wm;
	private View view;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * ����ȥ��ĺ��������
	 * @author ��
	 *
	 */
	class OutCallQueryReceiver extends BroadcastReceiver {

		// �ù㲥��������Ҫ�ڸ��ı��н���ע��,��������registerReceiver����
		@Override
		public void onReceive(Context context, Intent intent) {
			// �õ����ȥ�ĵ绰����
			String phone = getResultData();

			String address = NumberQueryUtil.numberQueryDB(phone);
			
			myToast(address);

		}

	}

	/**
	 * �ֻ�����
	 * @author ��
	 *
	 */
	private class MyListenerPhone extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// state ״̬��incomingNumber�������
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// ������������
				// ��ѯ���ݿ����
				String address = NumberQueryUtil.numberQueryDB(incomingNumber);
				myToast(address);			
				break;
			case TelephonyManager.CALL_STATE_IDLE://�绰�Ŀ���״̬:�ҵ绰�����绰���ܾ�
				if(view != null){
					wm.removeView(view);
				}
				break;
			default:
				break;
			}
		}
	}

	
	
	@Override
	public void onCreate() {
		super.onCreate();
		// ��������
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mlp = new MyListenerPhone();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		outPhone = new OutCallQueryReceiver();
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		

		// ����������¼�
		tm.listen(mlp, PhoneStateListener.LISTEN_CALL_STATE);

		Editor editor = sp.edit();
		editor.putBoolean("number_query", true);
		editor.commit();
		
		//ע�����ȥ����ʾ�¼�
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL" );
		registerReceiver(outPhone, filter);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ȡ������
		tm.listen(mlp, PhoneStateListener.LISTEN_NONE);
		mlp = null;

		sp = getSharedPreferences("config", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("number_query", true);
		editor.commit();
		
		//ȡ��ע�����ȥ����ʾ
		unregisterReceiver(outPhone);
		
	}

	public void myToast(String address) {
//		
//		view = new TextView(getApplicationContext());	
//		view.setText(address);
//		view.setTextSize(22);
//		view.setTextColor(Color.RED);
		
		View view = View.inflate(getApplicationContext(), R.layout.number_show_item_layout, null);
		TextView textView = (TextView) view.findViewById(R.id.show_number_tv);
		textView.setText(address);
		view.setBackgroundResource(R.drawable.call_locate_green);
		
		
		//���ò����ļ�
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		
		//�޷���������޷���������
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		
		//��͸��
		params.format = PixelFormat.TRANSLUCENT;
		
		//����
		//params.windowAnimations = ""
		
		//����
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		
		
		//���ڿؼ����������2������
		wm.addView(view, params);
	}



}
