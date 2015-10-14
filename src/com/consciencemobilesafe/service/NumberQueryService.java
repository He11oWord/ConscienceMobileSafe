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

	//去电监听器
	private OutCallQueryReceiver outPhone;
	
	//窗口控件
	private WindowManager wm;
	private View view;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 监听去电的号码归属地
	 * @author 钊
	 *
	 */
	class OutCallQueryReceiver extends BroadcastReceiver {

		// 该广播接收者需要在该文本中进行注册,在下文中registerReceiver方法
		@Override
		public void onReceive(Context context, Intent intent) {
			// 得到打出去的电话号码
			String phone = getResultData();

			String address = NumberQueryUtil.numberQueryDB(phone);
			
			myToast(address);

		}

	}

	/**
	 * 手机服务
	 * @author 钊
	 *
	 */
	private class MyListenerPhone extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// state 状态，incomingNumber来电号码
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 来电铃声响起
				// 查询数据库操作
				String address = NumberQueryUtil.numberQueryDB(incomingNumber);
				myToast(address);			
				break;
			case TelephonyManager.CALL_STATE_IDLE://电话的空闲状态:挂电话，来电话，拒绝
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
		// 开启监听
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mlp = new MyListenerPhone();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		outPhone = new OutCallQueryReceiver();
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		

		// 监听来电的事件
		tm.listen(mlp, PhoneStateListener.LISTEN_CALL_STATE);

		Editor editor = sp.edit();
		editor.putBoolean("number_query", true);
		editor.commit();
		
		//注册监听去电显示事件
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL" );
		registerReceiver(outPhone, filter);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消监听
		tm.listen(mlp, PhoneStateListener.LISTEN_NONE);
		mlp = null;

		sp = getSharedPreferences("config", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("number_query", true);
		editor.commit();
		
		//取消注册监听去电显示
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
		
		
		//设置布局文件
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		
		//无法被点击，无法被触摸。
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		
		//半透明
		params.format = PixelFormat.TRANSLUCENT;
		
		//动画
		//params.windowAnimations = ""
		
		//类型
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		
		
		//窗口控件中添加上面2个东西
		wm.addView(view, params);
	}



}
