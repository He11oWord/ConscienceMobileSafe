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
	//�񶯹���
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.number_query_layout);

		number_query_button = (Button) findViewById(R.id.number_query_button);
		number_query_et = (EditText) findViewById(R.id.number_query_et);
		number_query_tv = (TextView) findViewById(R.id.number_query_tv);

		//ʵ�����񶯣���Ȩ�ޣ�
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		
		number_query_button.setOnClickListener(this);

		// �����ı�������
		number_query_et.addTextChangedListener(new TextWatcher() {

			/**
			 * ���ı��ı�ʱ�ص�
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
			 * ���ı��ı�ǰ�ص�
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			/**
			 * ���ı��ı��ص�
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	// ��ѯ��������ط���
	@Override
	public void onClick(View v) {
		String phoneNumber = number_query_et.getText().toString();
		if (TextUtils.isEmpty(phoneNumber)) {
			// ����Ϊ�գ���˾��ʾ
			Toast.makeText(this, "����Ϊ��", 0).show();
			//����Ч��
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			number_query_et.startAnimation(shake);
//			//�Զ��嶯��
//			shake.setInterpolator(new Interpolator() {
//				
//				@Override
//				public float getInterpolation(float input) {
//					// TODO Auto-generated method stub
//					return 0;
//				}
//			});
			//��Ч��
			//ʱ��
			vibrator.vibrate(2000);
			//���ɣ���200ms��ͣ200ms����300ms��ͣ300ms��
			long[] pattern = {200,200,300,300};
			//-1���ظ���0ѭ��
			vibrator.vibrate(pattern, -1);
			return;
		} else {
			// ���벻Ϊ�գ���˾��ʾ
			String adress = NumberQueryUtil.numberQueryDB(phoneNumber);
			number_query_tv.setText(adress);
			Toast.makeText(this, "���벻Ϊ��", 0).show();
		}
	}

}
