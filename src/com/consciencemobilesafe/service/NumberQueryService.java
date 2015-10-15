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
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class NumberQueryService extends Service {

	private TelephonyManager tm;
	private MyListenerPhone mlp;
	private SharedPreferences sp;

	// 去电监听器
	private OutCallQueryReceiver outPhone;

	// 窗口控件
	private WindowManager wm;
	private View view;

	//使归属地框框能够拖动,后面的XY为里面的参数
	private int startX;
	private int startY;
	private int newX;
	private int newY;
	private int dX;
	private int dY;
	private WindowManager.LayoutParams params;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 监听去电的号码归属地
	 * 
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
	 * 
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
			case TelephonyManager.CALL_STATE_IDLE:// 电话的空闲状态:挂电话，来电话，拒绝
				if (view != null) {
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

		// 注册监听去电显示事件
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
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

		// 取消注册监听去电显示
		unregisterReceiver(outPhone);

	}

	public void myToast(String address) {

		// view = new TextView(getApplicationContext());
		// view.setText(address);
		// view.setTextSize(22);
		// view.setTextColor(Color.RED);

		view = View.inflate(getApplicationContext(),
				R.layout.number_show_item_layout, null);
		params = new WindowManager.LayoutParams();

		// 设置初始位置
		params.x = sp.getInt("params_x", 100);
		params.y = sp.getInt("params_y", 100);

		//给View一个双击事件
		final long[] mHits = new long[2];
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//拷贝的原数组，原数组的位置，目标数组，到目标数组的那个位置，拷贝元素的个数
				System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
				mHits[mHits.length-1] = SystemClock.uptimeMillis();
				
				//因为该开始没有赋值，所以初始化都是0，直到最后一次才会有可能成立
				if(mHits[0] >= (SystemClock.uptimeMillis()-500)){
					params.x = wm.getDefaultDisplay().getWidth()/2 - view.getWidth()/2;
					wm.updateViewLayout(view, params);
					// 保存当前的位置值
					Editor editor = sp.edit();
					editor.putInt("params_x", params.x);
					editor.putInt("params_y", params.y);
					editor.commit();
					
				}
			}
		});
		
		
		// 给View对象设置一个触摸的监听器
		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 手指按下屏幕
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动
					newX = (int) event.getRawX();
					newY = (int) event.getRawY();
					dX = newX - startX;
					dY = newY - startY;
					// 更新ImageView的位置
					params.x += dX;
					params.y += dY;
					Log.d("x+y",params.x+"+"+params.y);
					// 考虑边界问题
					if (params.x < 0) {
						params.x = 0;
						
					}
					if (params.y < 0) {
						params.y = 0;
					}
					if (params.x > (wm.getDefaultDisplay().getWidth() - view
							.getWidth())) {
						params.x = (wm.getDefaultDisplay().getWidth() - view
								.getWidth());
					}
					if (params.y > (wm.getDefaultDisplay().getHeight() - view
							.getHeight())) {
						params.y = (wm.getDefaultDisplay().getHeight() - view
								.getHeight());
					}

					wm.updateViewLayout(view, params);

					// 初始化
					startX = newX;
					startY = newY;

					break;
				case MotionEvent.ACTION_UP:// 手指离开屏幕的一瞬间
					// 保存当前的位置值
					Editor editor = sp.edit();
					editor.putInt("params_x", params.x);
					editor.putInt("params_y", params.y);
					editor.commit();
					break;

				}

				return true;// 事件处理完毕，不要让父控件响应相应的触摸事件了
			}
		});

		TextView textView = (TextView) view.findViewById(R.id.show_number_tv);
		textView.setText(address);

		//
		// //设置归属地框框的背景
		// int number_style = sp.getInt("number_style", 0);
		// switch(number_style){
		// case 0:
		// view.setBackgroundResource(R.drawable.call_locate_gray);
		// break;
		// case 1:
		// view.setBackgroundResource(R.drawable.call_locate_orange);
		// break;
		// case 2:
		// view.setBackgroundResource(R.drawable.call_locate_blue);
		// break;
		// case 3:
		// view.setBackgroundResource(R.drawable.call_locate_white);
		// break;
		// case 4:
		// view.setBackgroundResource(R.drawable.call_locate_green);
		// break;
		// }
		// 简便的设置背景颜色
		int[] ids = { R.drawable.call_locate_gray,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_white, R.drawable.call_locate_green };
		view.setBackgroundResource(ids[sp.getInt("number_style", 0)]);

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;

		// 无法被点击，无法被触摸。
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

		// 半透明
		params.format = PixelFormat.TRANSLUCENT;

		// 动画
		// params.windowAnimations = ""

		// 类型
		// params.type = WindowManager.LayoutParams.TYPE_TOAST;

		// 这是电话优先级的窗口，就是最高级别的窗口，需要添加权限
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		// 窗口控件中添加上面2个东西
		wm.addView(view, params);
	}

}
