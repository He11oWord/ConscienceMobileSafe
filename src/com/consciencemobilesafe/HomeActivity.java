package com.consciencemobilesafe;


import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.utils.MD5Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	
	protected static final String TAG = "HomeActivity";
	private GridView homeGridView;
	private MyAdapter adapter;
	
	//��ҳ���е�ͼ��ͺ���
	private static String[] names = {
		"�ֻ�����","ͨѶ��ʿ","��������",
		"���̹���","����ͳ��","�ֻ�ɱ��",
		"��������","�߼�����","��������"
	};
	
	private static int[] ids = {
		R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
		R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
		R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings
	};
	
	//SharePreferences�洢�Ƿ���������
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		homeGridView = (GridView) findViewById(R.id.home_grid_view);
		adapter = new MyAdapter();
		homeGridView.setAdapter(adapter);
		homeGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> homeG, View view, int position,
					long id) {
				switch(position){
				case 0 ://�����������
					showLostPhoneDialog();
					break;
				case 8 ://ѡ����������
					Intent intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
					
				default:
					break;
				}
				
			}
		});
		
	}
	/**
	 * ��ʾ�Ի���
	 */
	protected void showLostPhoneDialog() {
		if(isSetupPassword()){
			//���ù����룬��ʱ����ʾ������������Ի���
			showEnterPasswordDialog();
			
		}else{
			//û�����ù����룬��ʱ����ʾ��������Ի���
			showSetupPasswordDialog();
			
		}
	}
	
	/**
	 * ��������Ի���
	 */
	private void showEnterPasswordDialog() {
		AlertDialog.Builder builder = new Builder(this);
		
		View view = View.inflate(this, R.layout.dialog_enterpassword_layout, null);
		builder.setView(view);
		dialog = builder.show();
		
		firstPassword = (EditText) view.findViewById(R.id.lost_dialog_password);
		okButton = (Button) view.findViewById(R.id.lost_dialog_ok);
		cancelButton = (Button) view.findViewById(R.id.lost_dialog_cancel);
		
		//���ȡ����ť
		cancelButton.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});		
		
		//���ȷ�ϰ�ť
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Editor editor = sp.edit();
				String password = sp.getString("password",null);
				String getPassword = firstPassword.getText().toString().trim();
				
				if(MD5Util.MD5Password(getPassword).equals(password)){
					//������һ��ҳ��
					dialog.dismiss();
					Toast.makeText(HomeActivity.this, "������ȷ",0 ).show();
					Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
					startActivity(intent);
					
				}else{
					Toast.makeText(HomeActivity.this, "���벻��ȷ", 0).show();
					firstPassword.setText("");
					return;
				}
				
				
				
			}
		});
		
		
	}
	
	


	/**
	 * ��������Ի���
	 */
	private AlertDialog dialog;
	private EditText firstPassword;
	private EditText confirmPassword;
	private Button okButton;
	private Button cancelButton;
	
	private void showSetupPasswordDialog() {
		AlertDialog.Builder builder = new Builder(this);
		
		//��ʾ���Ի���
		View view = View.inflate(HomeActivity.this, R.layout.dialog_setpassword_layout, null);
		builder.setView(view);
		dialog = builder.show();
		
		firstPassword = (EditText) view.findViewById(R.id.lost_first_password);
		confirmPassword = (EditText) view.findViewById(R.id.lost_dialog_secend_password);
		okButton = (Button) view.findViewById(R.id.lost_dialog_ok);
		cancelButton = (Button) view.findViewById(R.id.lost_dialog_cancel);
		
		//���ȡ����ť
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		//���ȷ�ϰ�ť
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�ж������Ƿ�գ���ʾ����Ϊ��
				//�ж������Ƿ�һ�£���ʾ���벻һ��
				//���������õ�����
				//������һ��ҳ��
				String fp = firstPassword.getText().toString().trim();
				String cp = confirmPassword.getText().toString().trim();
				
				if(TextUtils.isEmpty(fp) || TextUtils.isEmpty(cp)){
					Toast.makeText(HomeActivity.this, "���벻Ϊ��", 0).show();
					return;
				}else{
					if(fp.equals(cp)){
						dialog.dismiss();
						Editor editor = sp.edit();
						editor.putString("password", MD5Util.MD5Password(fp));
						editor.commit();
						Log.d(TAG, "���óɹ�");	
						Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
						startActivity(intent);
					}else{
						Toast.makeText(HomeActivity.this, "���벻һ��", 0).show();
						return;
					}
					
				}
				
			}
		});
		
		
	}
	/**
	 * ���������Ƿ�����������
	 * @return
	 */
	private boolean isSetupPassword() {
		String password = sp.getString("password", null);
//		if(TextUtils.isEmpty(password)){
//			return false;
//		}else{
//			return true;
//		}
		return !TextUtils.isEmpty(password); 
	}

	class MyAdapter extends BaseAdapter{
		
	
	
		//�����ܸ���
		public int getCount() {
			return names.length;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.item_layout, null);
			
			//ʹ�ø�view�е�findViewById
			ImageView itemIv = (ImageView) view.findViewById(R.id.item_iv);
			TextView itemTv = (TextView) view.findViewById(R.id.item_tv);
			
			itemIv.setImageResource(ids[position]);
			itemTv.setText(names[position]);
			
			
			
			return view;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	
		

	
	}


}