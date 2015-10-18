package com.consciencemobilesafe.utils;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.GetChars;
import android.util.Xml;

/**
 * ���ŵĹ����� �����û��Ķ���
 * 
 */
public class SmsUtil {
	
	/**
	 * ���ű��ݵĽӿ�
	 * �����������ֵ�뵱ǰ����
	 */
	public interface SmsCopyProcess{
		
		/**
		 * �����ܽ���
		 * @param max �ܽ���ֵ
		 */
		public void setMax(int max);
		
		/**
		 * ���õ�ǰ����ֵ
		 * @param Process ��ǰ����
		 */
		public void setProgress(int Process);
	}

	public static void smsCopy(Context context,SmsCopyProcess scp)
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
		//�������Ĺ���
		int process = 0;
		serializer.attribute(null, "max", ""+max);
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
			
			//ÿ����һ����������+1
			process++;
			scp.setProgress(process);
		}
		serializer.endTag(null, "smss");
		serializer.endDocument();
		fos.close();
	}

	public static void smsRestore(Context context){
		//1.��ȡXML�ļ�
		XmlResourceParser 
		XmlPullParser newPullParser = Xml.newPullParser();
		newPullParser.getAttributeValue(null, "max");
		
		
		//2.��ȡMax
		
		//3.��ÿһ�����ŵ���Ϣ��ȡ����
		
		//4.�Ѷ������ݲ��뵽ϵͳ����Ӧ�����棨���ݿ⣩
		
		
	}

}
