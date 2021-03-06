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
 * 短信的工具类 备份用户的短信
 * 
 */
public class SmsUtil {

	/**
	 * 短信备份的接口 用来设置最大值与当前进度
	 */
	public interface SmsCopyProcess {

		/**
		 * 设置总进度
		 * 
		 * @param max
		 *            总进度值
		 */
		public void setMax(int max);

		/**
		 * 设置当前进度值
		 * 
		 * @param Process
		 *            当前进度
		 */
		public void setProgress(int process);
	}

	
	/**
	 * 短信备份的接口 用来设置最大值与当前进度
	 */
	public interface SmsRestoreProcess {

		/**
		 * 设置总进度
		 * 
		 * @param max
		 *            总进度值
		 */
		public void setMax(int max);

		/**
		 * 设置当前进度值
		 * 
		 * @param Process
		 *            当前进度
		 */
		public void setProgress(int process);
	}
	
	public static void smsCopy(Context context, SmsCopyProcess scp)
			throws Exception {
		// 内容提供者查看短信
		ContentResolver resolver = context.getContentResolver();

		// 需要一个文件，备份的位置
		File file = new File("data/data/com.consciencemobilesafe.app/files",
				"copy.xml");
		FileOutputStream fos = new FileOutputStream(file);
		// 把用户的短信一条一条的读出来，按照一定的格式写到xml文件中
		// XML生成器,获得xml的生成器（序列化器）
		XmlSerializer serializer = Xml.newSerializer();
		// 初始化生成器
		serializer.setOutput(fos, "utf-8");
		// 头文件：编码字体，是否独立,和下面的endDocument对应
		serializer.startDocument("utf-8", true);
		// 命名空间，头部和下面对应
		serializer.startTag(null, "smss");
		// 查询每一条数据
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = resolver.query(uri, new String[] { "address", "body",
				"type", "date" }, null, null, null);
		// 设置进度条的最大值
		int max = cursor.getCount();
		scp.setMax(max);
		// 进度条的过程
		int process = 0;
		serializer.attribute(null, "max", "" + max);
		while (cursor.moveToNext()) {
			Thread.sleep(500);
			String address = cursor.getString(0);
			String body = cursor.getString(1);
			String type = cursor.getString(2);
			String date = cursor.getString(3);

			// 开始写xml文件了
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

			// 每复制一个进度条就+1
			process++;
			scp.setProgress(process);
		}
		serializer.endTag(null, "smss");
		serializer.endDocument();
		fos.close();
	}

	public static void smsRestore(Context context,SmsRestoreProcess srp) throws Exception {

		// 是否删除所有记录
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		contentResolver.delete(uri, null, null);

		// 1.读取XML文件
		List<SmsS> list = new ArrayList<SmsS>();
		int max = 0;
		int process = 0;
		SmsS smsTemp = null;
		String address = null;
		String body = null;
		String type = null;
		String date = null;

		//解析XML事件需要一个流
		File xmlFlie = new File("data/data/com.consciencemobilesafe.app/files",
				"copy.xml");
		InputStream inputStream = new FileInputStream(xmlFlie);
		XmlPullParser newPullParser = Xml.newPullParser();
		//将流设置进解析器里面
		newPullParser.setInput(inputStream, "utf-8");
		//这是解析到的类型
		int eventType = newPullParser.getEventType();

		// 开始遍历数据，到了尾部，才停止
		while (newPullParser.getEventType() != XmlResourceParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				//Log.d("", "这里是头");
				break;
			case XmlPullParser.START_TAG:

				// 给标签取名字
				String tagName = newPullParser.getName();
				Log.d("", "====XmlPullParser.START_TAG=== tagName: " + tagName);

				// 获取信息，
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
					// 到了时间这里的时候，就可以存储一条信息了
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

		// 2.读取Max
		srp.setMax(max);
		// 3.把每一条短信的信息读取出来

		// 4.把短信内容插入到系统短信应用里面（数据库）
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
