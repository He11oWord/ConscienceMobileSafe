package com.consciencemobilesafe;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.utils.NumberQueryUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberQueryActivity extends Activity implements OnClickListener {

	
	
	
	
	private Button number_query_button;
	private TextView number_query_tv;
	private EditText number_query_et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.number_query_layout);
		
		number_query_button = (Button) findViewById(R.id.number_query_button);
		number_query_et = (EditText) findViewById(R.id.number_query_et);
		number_query_tv = (TextView) findViewById(R.id.number_query_tv);
		
		number_query_button.setOnClickListener(this);
		
	}

	//��ѯ��������ط���
	@Override
	public void onClick(View v) {
		String phoneNumber = number_query_et.getText().toString();
		if(TextUtils.isEmpty(phoneNumber)){
			//����Ϊ�գ���˾��ʾ
			Toast.makeText(this, "����Ϊ��", 0).show();
			return;
		}else{
			//���벻Ϊ�գ���˾��ʾ
			String adress = NumberQueryUtil.numberQueryDB(phoneNumber);
			number_query_tv.setText(adress);
			Toast.makeText(this, "���벻Ϊ��", 0).show();
		}
	}
	
}
