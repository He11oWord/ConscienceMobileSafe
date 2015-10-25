package com.consciencemobilesafe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.consciencemobilesafe.app.R;

public class SelectContactActivity extends Activity {

	private ListView select_contact_list_view;

	private List<Map<String, String>> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_contact_layout);
		data = getContact();
		select_contact_list_view = (ListView) findViewById(R.id.select_contact_list_view);

		// 第一个是上下文，第二个是数据，第三个是布局文件，第四个是哪些内容，第五个是这些内容分别对应哪个控件
		select_contact_list_view.setAdapter(new SimpleAdapter(this, data,
				R.layout.select_contact_item_layout, new String[] { "name",
						"phone" }, new int[] { R.id.name, R.id.number }));
		
		//设置列表点击事件
		select_contact_list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
					Map<String, String> map = data.get(arg2);
					String phone = map.get("phone");
					
					//用Intent传递值
					Intent data = new Intent();
					data.putExtra("phone", phone);
					setResult(0, data);
					//关闭当前页面
					finish();
			}
		});
	
	}

	
	
	
	
	//获得手机联系人信息
	private List<Map<String, String>> getContact() {

		// 创建一个列表，里面存有名字和联系人
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		// 数据库查询后存放值得变量
		Cursor cursor = null;
		ContentResolver resolver = getContentResolver();

		// 2种方法获得数据库的Uri
		Uri idUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		Uri dataUri = Uri.parse("content://com.android.contacts/data");

		// 查询数据库，new String[] {"contact_id"}代表查询的是contact_id那一列
		cursor = resolver.query(idUri, new String[] { "contact_id" }, null,
				null, null);

		while (cursor.moveToNext()) {
			// 这时候cursor是一个单列的数组，所以都是在第0个位置
			String contactId = cursor.getString(0);
			// 存放名字和联系人
			Map<String, String> map = new HashMap<String, String>();

			if (contactId != null) {
				Cursor dataCursor = resolver.query(dataUri, new String[] {
						"data1", "mimetype" }, "contact_id = ?",
						new String[] { contactId }, null);

				// 遍历查询出来的数据
				while (dataCursor.moveToNext()) {
					String date1 = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);

					if ("vnd.android.cursor.item/name".equals(mimetype)) {
						map.put("name", date1);
					} else if ("vnd.android.cursor.item/phone_v2"
							.equals(mimetype)) {
						map.put("phone", date1);
					}
				}
				list.add(map);
				dataCursor.close();
			}

		}

		cursor.close();
		return list;
	}

}
