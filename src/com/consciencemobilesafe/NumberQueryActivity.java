package com.consciencemobilesafe;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.utils.NumberQueryUtil;

import android.app.Activity;
import android.media.audiofx.Virtualizer;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberQueryActivity extends Activity implements OnClickListener {

	private Button number_query_button;
	private TextView number_query_tv;
	private EditText number_query_et;
	//振动管理
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.number_query_layout);

		number_query_button = (Button) findViewById(R.id.number_query_button);
		number_query_et = (EditText) findViewById(R.id.number_query_et);
		number_query_tv = (TextView) findViewById(R.id.number_query_tv);

		//实例化振动，加权限！
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		
		number_query_button.setOnClickListener(this);

		// 设置文本监听器
		number_query_et.addTextChangedListener(new TextWatcher() {

			/**
			 * 当文本改变时回调
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String number = s.toString();
				if (!(TextUtils.isEmpty(number)) && number.length() > 6) {
					number_query_tv.setText(NumberQueryUtil
							.numberQueryDB(number));
				}
			}

			/**
			 * 当文本改变前回调
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			/**
			 * 当文本改变后回调
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	// 查询号码归属地方法
	@Override
	public void onClick(View v) {
		String phoneNumber = number_query_et.getText().toString();
		if (TextUtils.isEmpty(phoneNumber)) {
			// 号码为空，吐司提示
			Toast.makeText(this, "号码为空", 0).show();
			//抖动效果
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			number_query_et.startAnimation(shake);
//			//自定义动画
//			shake.setInterpolator(new Interpolator() {
//				
//				@Override
//				public float getInterpolation(float input) {
//					// TODO Auto-generated method stub
//					return 0;
//				}
//			});
			//振动效果
			//时间
			vibrator.vibrate(2000);
			//规律（振动200ms，停200ms，振动300ms，停300ms）
			long[] pattern = {200,200,300,300};
			//-1不重复，0循环
			vibrator.vibrate(pattern, -1);
			return;
		} else {
			// 号码不为空，吐司提示
			String adress = NumberQueryUtil.numberQueryDB(phoneNumber);
			number_query_tv.setText(adress);
			Toast.makeText(this, "号码不为空", 0).show();
		}
	}

}
