package com.consciencemobilesafe;

import com.consciencemobilesafe.app.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.widget.Toast;

public abstract class SetupBaseActivity extends Activity {

	// 1.����������һ������ʶ����
	private GestureDetector detector;

	public SharedPreferences sp;
	
	// 2.ʵ��������ʶ����
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		detector = new GestureDetector(this, new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				
				//4.����б�Ż�
				if (Math.abs(e1.getY() - e2.getY()) > 100) {
					Toast.makeText(getApplicationContext(), "��Ҫб�Ż���", 0).show();
					return true;
				}
				
				//5.���λ�����
				if(Math.abs(velocityX)<200){
					Toast.makeText(getApplicationContext(), "�ӿ�����ٶ�", 0).show();
					return true;
				}
				

				if ((e1.getX() - e2.getX()) > 50) {
					// �������󻬶������ǽ�����һ��ҳ��
					onFlingNext();
					// ����true����
					return true;
				}

				if ((e2.getX() - e1.getX()) > 50) {
					// �������󻬶������ǽ�����һ��ҳ��
					onFlingBack();
					// ����true����
					return true;
				}

				return false;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
		});

	}

	// 3.���ø÷���
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	public abstract void onFlingBack();

	public abstract void onFlingNext();

	public void next(View view) {
		onFlingNext();
	}

	public void back(View view) {
		onFlingBack();
	}

}
