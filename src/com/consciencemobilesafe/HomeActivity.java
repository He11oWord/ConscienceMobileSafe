package com.consciencemobilesafe;


import com.consciencemobilesafe.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {
	
	private GridView homeGridView;
	private MyAdapter adapter;
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
	
	private SharedPreferences sp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		homeGridView = (GridView) findViewById(R.id.home_grid_view);
		adapter = new MyAdapter();
		homeGridView.setAdapter(adapter);
		sp  = getSharedPreferences("config", MODE_PRIVATE);
		
		homeGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> homeG, View view, int position,
					long id) {
				switch(position){
				case 0://进入防盗中心
					showLostFindDialog();
					break;
				
				case 8 ://选中设置中心
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
	 * 显示输入密码对话框
	 * 
	 */
	protected void showLostFindDialog() {
		if(isSetPassword()){
			//设置过密码，弹出输入密码对话框
		}else{
			//没有设置过密码，弹出设置密码框
		}
	}

	private boolean isSetPassword() {
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
			// TODO Auto-generated method stub
			return names.length;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.item_layout, null);
			
			//使用该view中的findViewById
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