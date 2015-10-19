package com.consciencemobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.util.Xml;

/**
 * ���ŵĹ����� �����û��Ķ���
 * 
 */
public class SmsUtil {

	/**
	 * ���ű��ݵĽӿ� �����������ֵ�뵱ǰ����
	 */
	public interface SmsCopyProcess {

		/**
		 * �����ܽ���
		 * 
		 * @param max
		 *            �ܽ���ֵ
		 */
		public void setMax(int max);

		/**
		 * ���õ�ǰ����ֵ
		 * 
		 * @param Process
		 *            ��ǰ����
		 */
		public void setProgress(int process);
	}

	
	/**
	 * ���ű��ݵĽӿ� �����������ֵ�뵱ǰ����
	 */
	public interface SmsRestoreProcess {

		/**
		 * �����ܽ���
		 * 
		 * @param max
		 *            �ܽ���ֵ
		 */
		public void setMax(int max);

		/**
		 * ���õ�ǰ����ֵ
		 * 
		 * @param Process
		 *            ��ǰ����
		 */
		public void setProgress(int process);
	}
	
	public static void smsCopy(Context context, SmsCopyProcess scp)
			throws Exception {
		// �����ṩ�߲鿴����
		ContentResolver resolver = context.getContentResolver();

		// ��Ҫһ���ļ������ݵ�λ��
		File file = new File("data/data/com.consciencemobilesafe.app/files",
				"copy.xml");
		FileOutputStream fos = new FileOutputStream(file);
		// ���û��Ķ���һ��һ���Ķ�����������һ���ĸ�ʽд��xml�ļ���
		// XML������,���xml�������������л�����
		XmlSerializer serializer = Xml.newSerializer();
		// ��ʼ��������
		serializer.setOutput(fos, "utf-8");
		// ͷ�ļ����������壬�Ƿ����,�������endDocument��Ӧ
		serializer.startDocument("utf-8", true);
		// �����ռ䣬ͷ���������Ӧ
		serializer.startTag(null, "smss");
		// ��ѯÿһ������
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = resolver.query(uri, new String[] { "address", "body",
				"type", "date" }, null, null, null);
		// ���ý����������ֵ
		int max = cursor.getCount();
		scp.setMax(max);
		// �������Ĺ���
		int process = 0;
		serializer.attribute(null, "max", "" + max);
		while (cursor.moveToNext()) {
			Thread.sleep(500);
			String address = cursor.getString(0);
			String body = cursor.getString(1);
			String type = cursor.getString(2);
			String date = cursor.getString(3);

			// ��ʼдxml�ļ���
			serializer.startTag(null, "sms");
			serializer.startTag(null, "address");
			serializer.text(address);
			serializer.endTag(null, "address");

			serializer.startTag(null, "body");
			serializer.text(body);
			serializer.endTag(null, "body");

			serializer.startTag(null, "type");
			serializer.text(type);
			serializer.endTag(null, "type");

			serializer.startTag(null, "date");
			serializer.text(date);
			serializer.endTag(null, "date");

			serializer.endTag(null, "sms");

			// ÿ����һ����������+1
			process++;
			scp.setProgress(process);
		}
		serializer.endTag(null, "smss");
		serializer.endDocument();
		fos.close();
	}

	public static void smsRestore(Context context,SmsRestoreProcess srp) throws Exception {

		// �Ƿ�ɾ�����м�¼
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		contentResolver.delete(uri, null, null);

		// 1.��ȡXML�ļ�
		List<SmsS> list = new ArrayList<SmsS>();
		int max = 0;
		int process = 0;
		SmsS smsTemp = null;
		String address = null;
		String body = null;
		String type = null;
		String date = null;

		//����XML�¼���Ҫһ����
		File xmlFlie = new File("data/data/com.consciencemobilesafe.app/files",
				"copy.xml");
		InputStream inputStream = new FileInputStream(xmlFlie);
		XmlPullParser newPullParser = Xml.newPullParser();
		//�������ý�����������
		newPullParser.setInput(inputStream, "utf-8");
		//���ǽ�����������
		int eventType = newPullParser.getEventType();

		// ��ʼ�������ݣ�����β������ֹͣ
		while (newPullParser.getEventType() != XmlResourceParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				//Log.d("", "������ͷ");
				break;
			case XmlPullParser.START_TAG:

				// ����ǩȡ����
				String tagName = newPullParser.getName();
				Log.d("", "====XmlPullParser.START_TAG=== tagName: " + tagName);

				// ��ȡ��Ϣ��
				if (tagName.equals("smss")) {
					Log.d("", "====XmlPullParser.ST: " + tagName);
					max = Integer.parseInt(newPullParser.getAttributeValue(0));
				} else if (tagName.equals("sms")) {
					smsTemp = new SmsS();
				} else if (tagName.equals("address")) {
					address = newPullParser.nextText();
				} else if (tagName.equals("body")) {
					body = newPullParser.nextText();
				} else if (tagName.equals("type")) {
					type = newPullParser.nextText();
				} else if (tagName.equals("date")) {
					// ����ʱ�������ʱ�򣬾Ϳ��Դ洢һ����Ϣ��
					date = newPullParser.nextText();
					Log.v("", "id getText: " + address);
					Log.v("", "id getText: " + body);
					Log.v("", "id getText: " + type);
					Log.v("", "id getText: " + date);

					smsTemp.setAddress(address);
					smsTemp.setBody(body);
					smsTemp.setDate(date);
					smsTemp.setType(type);

					list.add(smsTemp);
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			case XmlPullParser.END_DOCUMENT:
				break;
			}
			eventType = newPullParser.next();
		}

		// 2.��ȡMax
		srp.setMax(max);
		// 3.��ÿһ�����ŵ���Ϣ��ȡ����

		// 4.�Ѷ������ݲ��뵽ϵͳ����Ӧ�����棨���ݿ⣩
		for (SmsS s : list) {
			ContentValues values = new ContentValues();

			values.put("address", s.getAddress());
			values.put("body", s.getBody());
			values.put("type", s.getType());
			values.put("date", s.getDate());
			contentResolver.insert(uri, values);
			process++;
			srp.setProgress(process);
		}
	}
}
