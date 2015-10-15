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

	// ȥ�������
	private OutCallQueryReceiver outPhone;

	// ���ڿؼ�
	private WindowManager wm;
	private View view;

	//ʹ�����ؿ���ܹ��϶�,�����XYΪ����Ĳ���
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
	 * ����ȥ��ĺ��������
	 * 
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
	 * 
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
			case TelephonyManager.CALL_STATE_IDLE:// �绰�Ŀ���״̬:�ҵ绰�����绰���ܾ�
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

		// ע�����ȥ����ʾ�¼�
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
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

		// ȡ��ע�����ȥ����ʾ
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

		// ���ó�ʼλ��
		params.x = sp.getInt("params_x", 100);
		params.y = sp.getInt("params_y", 100);

		//��Viewһ��˫���¼�
		final long[] mHits = new long[2];
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//������ԭ���飬ԭ�����λ�ã�Ŀ�����飬��Ŀ��������Ǹ�λ�ã�����Ԫ�صĸ���
				System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
				mHits[mHits.length-1] = SystemClock.uptimeMillis();
				
				//��Ϊ�ÿ�ʼû�и�ֵ�����Գ�ʼ������0��ֱ�����һ�βŻ��п��ܳ���
				if(mHits[0] >= (SystemClock.uptimeMillis()-500)){
					params.x = wm.getDefaultDisplay().getWidth()/2 - view.getWidth()/2;
					wm.updateViewLayout(view, params);
					// ���浱ǰ��λ��ֵ
					Editor editor = sp.edit();
					editor.putInt("params_x", params.x);
					editor.putInt("params_y", params.y);
					editor.commit();
					
				}
			}
		});
		
		
		// ��View��������һ�������ļ�����
		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// ��ָ������Ļ
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:// ��ָ����Ļ���ƶ�
					newX = (int) event.getRawX();
					newY = (int) event.getRawY();
					dX = newX - startX;
					dY = newY - startY;
					// ����ImageView��λ��
					params.x += dX;
					params.y += dY;
					Log.d("x+y",params.x+"+"+params.y);
					// ���Ǳ߽�����
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

					// ��ʼ��
					startX = newX;
					startY = newY;

					break;
				case MotionEvent.ACTION_UP:// ��ָ�뿪��Ļ��һ˲��
					// ���浱ǰ��λ��ֵ
					Editor editor = sp.edit();
					editor.putInt("params_x", params.x);
					editor.putInt("params_y", params.y);
					editor.commit();
					break;

				}

				return true;// �¼�������ϣ���Ҫ�ø��ؼ���Ӧ��Ӧ�Ĵ����¼���
			}
		});

		TextView textView = (TextView) view.findViewById(R.id.show_number_tv);
		textView.setText(address);

		//
		// //���ù����ؿ��ı���
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
		// �������ñ�����ɫ
		int[] ids = { R.drawable.call_locate_gray,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_white, R.drawable.call_locate_green };
		view.setBackgroundResource(ids[sp.getInt("number_style", 0)]);

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;

		// �޷���������޷���������
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

		// ��͸��
		params.format = PixelFormat.TRANSLUCENT;

		// ����
		// params.windowAnimations = ""

		// ����
		// params.type = WindowManager.LayoutParams.TYPE_TOAST;

		// ���ǵ绰���ȼ��Ĵ��ڣ�������߼���Ĵ��ڣ���Ҫ���Ȩ��
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		// ���ڿؼ����������2������
		wm.addView(view, params);
	}

}
