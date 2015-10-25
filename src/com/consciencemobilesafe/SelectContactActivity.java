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

		// ��һ���������ģ��ڶ��������ݣ��������ǲ����ļ������ĸ�����Щ���ݣ����������Щ���ݷֱ��Ӧ�ĸ��ؼ�
		select_contact_list_view.setAdapter(new SimpleAdapter(this, data,
				R.layout.select_contact_item_layout, new String[] { "name",
						"phone" }, new int[] { R.id.name, R.id.number }));
		
		//�����б����¼�
		select_contact_list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
					Map<String, String> map = data.get(arg2);
					String phone = map.get("phone");
					
					//��Intent����ֵ
					Intent data = new Intent();
					data.putExtra("phone", phone);
					setResult(0, data);
					//�رյ�ǰҳ��
					finish();
			}
		});
	
	}

	
	
	
	
	//����ֻ���ϵ����Ϣ
	private List<Map<String, String>> getContact() {

		// ����һ���б�����������ֺ���ϵ��
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		// ���ݿ��ѯ����ֵ�ñ���
		Cursor cursor = null;
		ContentResolver resolver = getContentResolver();

		// 2�ַ���������ݿ��Uri
		Uri idUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		Uri dataUri = Uri.parse("content://com.android.contacts/data");

		// ��ѯ���ݿ⣬new String[] {"contact_id"}�����ѯ����contact_id��һ��
		cursor = resolver.query(idUri, new String[] { "contact_id" }, null,
				null, null);

		while (cursor.moveToNext()) {
			// ��ʱ��cursor��һ�����е����飬���Զ����ڵ�0��λ��
			String contactId = cursor.getString(0);
			// ������ֺ���ϵ��
			Map<String, String> map = new HashMap<String, String>();

			if (contactId != null) {
				Cursor dataCursor = resolver.query(dataUri, new String[] {
						"data1", "mimetype" }, "contact_id = ?",
						new String[] { contactId }, null);

				// ������ѯ����������
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
