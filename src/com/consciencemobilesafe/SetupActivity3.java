package com.consciencemobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.consciencemobilesafe.app.R;

public class SetupActivity3 extends SetupBaseActivity {

	private EditText set_safe_phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup3_layout);
		set_safe_phone = (EditText) findViewById(R.id.set_safe_phone);

		if (TextUtils.isEmpty(sp.getString("safe_phone", null))) {

		} else {
			set_safe_phone.setText(sp.getString("safe_phone", null));
		}
	}

	@Override
	public void onFlingBack() {
		Intent intent = new Intent(SetupActivity3.this, SetupActivity2.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_back_in, R.anim.tran_back_out);
	}

	@Override
	public void onFlingNext() {

		// �����Ѿ����õİ�ȫ����
		Editor editor = sp.edit();
		editor.putString("safe_phone", set_safe_phone.getText().toString());
		editor.commit();
		if (TextUtils.isEmpty(sp.getString("safe_phone", null))) {
			Toast.makeText(this, "����δ���ð�ȫ����", 0).show();
			return;
		} else {
			Intent intent = new Intent(SetupActivity3.this,
					SetupActivity4.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);

		}
	}

	public void selectContact(View view) {
		Intent intent = new Intent(this, SelectContactActivity.class);
		startActivityForResult(intent, 0);
	}

	// ��������ҳ�淵�ص�ֵ
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		// ���û�ֱ�ӵ������ʱ
		if (data == null) {
			return;
		}

		String s = data.getStringExtra("phone").replace("-", "");

		set_safe_phone.setText(s);
	}
}
