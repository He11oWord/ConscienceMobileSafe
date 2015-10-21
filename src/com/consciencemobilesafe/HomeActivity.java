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
	
	//主页面中的图标和汉字
	private static String[] names = {
		"手机防盗","通讯卫士","软件管理",
		"进程管理","流量统计","手机杀毒",
		"缓存清理","高级工具","设置中心"
	};
	
	private static int[] ids = {
		R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
		R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
		R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings
	};
	
	//SharePreferences存储是否设置密码
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		homeGridView = (GridView) findViewById(R.id.home_grid_view);
		adapter = new MyAdapter();
		homeGridView.setAdapter(adapter);
		homeGridView.setOnItemClickListener(new OnItemClickListener() {

			private Intent intent;

			@Override
			public void onItemClick(AdapterView<?> homeG, View view, int position,
					long id) {
				switch(position){
				case 0 ://进入防盗中心
					showLostPhoneDialog();
					break;
				case 1 :// 进入通讯卫士
					intent = new Intent(HomeActivity.this,NumberSmsSafeActivity.class);
					startActivity(intent);
					break;
				case 2://进入软件管理
					intent = new Intent(HomeActivity.this,APPManagerActivity.class);
					startActivity(intent);
					break;
				case 3://进入软件管理
					intent = new Intent(HomeActivity.this,TaskManagerActivity.class);
					startActivity(intent);
					break;
				case 7 ://选中设置中心
					intent = new Intent(HomeActivity.this,AtoolActivity.class);
					startActivity(intent);
					break;
				case 8 ://选中设置中心
					intent = new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;
					
				default:
					break;
				}
				
			}
		});
		
	}
	/**
	 * 显示对话框
	 */
	protected void showLostPhoneDialog() {
		if(isSetupPassword()){
			//设置过密码，这时候显示的是输入密码对话框
			showEnterPasswordDialog();
			
		}else{
			//没有设置过密码，这时候显示设置密码对话框
			showSetupPasswordDialog();
			
		}
	}
	
	/**
	 * 输入密码对话框
	 */
	private void showEnterPasswordDialog() {
		AlertDialog.Builder builder = new Builder(this);
		
		View view = View.inflate(this, R.layout.dialog_enterpassword_layout, null);
		builder.setView(view);
		dialog = builder.show();
		
		firstPassword = (EditText) view.findViewById(R.id.lost_dialog_password);
		okButton = (Button) view.findViewById(R.id.lost_dialog_ok);
		cancelButton = (Button) view.findViewById(R.id.lost_dialog_cancel);
		
		//点击取消按钮
		cancelButton.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});		
		
		//点击确认按钮
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Editor editor = sp.edit();
				String password = sp.getString("password",null);
				String getPassword = firstPassword.getText().toString().trim();
				
				if(MD5Util.MD5Password(getPassword).equals(password)){
					//进入下一个页面
					dialog.dismiss();
					Toast.makeText(HomeActivity.this, "密码正确",0 ).show();
					Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
					startActivity(intent);
					
				}else{
					Toast.makeText(HomeActivity.this, "密码不正确", 0).show();
					firstPassword.setText("");
					return;
				}
				
				
				
			}
		});
		
		
	}
	
	


	/**
	 * 设置密码对话框
	 */
	private AlertDialog dialog;
	private EditText firstPassword;
	private EditText confirmPassword;
	private Button okButton;
	private Button cancelButton;
	
	private void showSetupPasswordDialog() {
		AlertDialog.Builder builder = new Builder(this);
		
		//显示出对话框
		View view = View.inflate(HomeActivity.this, R.layout.dialog_setpassword_layout, null);
		builder.setView(view);
		dialog = builder.show();
		
		firstPassword = (EditText) view.findViewById(R.id.lost_first_password);
		confirmPassword = (EditText) view.findViewById(R.id.lost_dialog_secend_password);
		okButton = (Button) view.findViewById(R.id.lost_dialog_ok);
		cancelButton = (Button) view.findViewById(R.id.lost_dialog_cancel);
		
		//点击取消按钮
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		//点击确认按钮
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//判断密码是否空，提示密码为空
				//判断密码是否一致，提示密码不一致
				//保存已设置的密码
				//进入下一个页面
				String fp = firstPassword.getText().toString().trim();
				String cp = confirmPassword.getText().toString().trim();
				
				if(TextUtils.isEmpty(fp) || TextUtils.isEmpty(cp)){
					Toast.makeText(HomeActivity.this, "密码不为空", 0).show();
					return;
				}else{
					if(fp.equals(cp)){
						dialog.dismiss();
						Editor editor = sp.edit();
						editor.putString("password", MD5Util.MD5Password(fp));
						editor.commit();
						Log.d(TAG, "设置成功");	
						Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
						startActivity(intent);
					}else{
						Toast.makeText(HomeActivity.this, "密码不一致", 0).show();
						return;
					}
					
				}
				
			}
		});
		
		
	}
	/**
	 * 防盗中心是否设置了密码
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
		
	
	
		//返回总个数
		public int getCount() {
			return names.length;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.home_item_layout, null);
			
			//使用该view中的findViewById
			ImageView itemIv = (ImageView) view.findViewById(R.id.item_iv);
			TextView itemTv = (TextView) view.findViewById(R.id.item_tv);
			
			itemIv.setImageResource(ids[position]);
			itemTv.setText(names[position]);
			
			
			
			return view;
		}
		
		
	
		@Override
		public long getItemId(int position) {
			return 0;
		}
	
		public Object getItem(int position) {
			return null;
		}

	
	}


}