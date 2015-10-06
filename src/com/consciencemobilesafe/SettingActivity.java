package edu.gxut.consciencemobilesafe;

import com.consciencemobilesafe.app.R;

import edu.gxut.consciencemobilesafe.ui.SettingItemView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

public class SettingActivity extends Activity{
	
	private SettingItemView siv_update;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		
		
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		if(sp.getBoolean("update", false)){
			siv_update.setCheck(true);
			siv_update.setText("�Զ������Ѿ���");
		}else{
			siv_update.setCheck(false);
			siv_update.setText("�Զ������Ѿ��ر�");
		}
		siv_update.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				Editor edit = sp.edit();
				
				if(siv_update.isChecked()){
					//�ر��Զ����¹���
					siv_update.setCheck(false);
					siv_update.setText("�Զ������Ѿ��ر�");
					edit.putBoolean("update", false);
				}else{
					//���Զ����¹���
					siv_update.setCheck(true);
					siv_update.setText("�Զ������Ѿ���");
					edit.putBoolean("update", true);
				}
				edit.commit();
				
			}
		});
	
	}




}
