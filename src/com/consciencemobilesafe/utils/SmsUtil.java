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
 * 短信的工具类 备份用户的短信
 * 
 */
public class SmsUtil {
	
	/**
	 * 短信备份的接口
	 * 用来设置最大值与当前进度
	 */
	public interface SmsCopyProcess{
		
		/**
		 * 设置总进度
		 * @param max 总进度值
		 */
		public void setMax(int max);
		
		/**
		 * 设置当前进度值
		 * @param Process 当前进度
		 */
		public void setProgress(int Process);
	}

	public static void smsCopy(Context context,SmsCopyProcess scp)
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
		//进度条的过程
		int process = 0;
		serializer.attribute(null, "max", ""+max);
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
			
			//每复制一个进度条就+1
			process++;
			scp.setProgress(process);
		}
		serializer.endTag(null, "smss");
		serializer.endDocument();
		fos.close();
	}

	public static void smsRestore(Context context){
		//1.读取XML文件
		XmlResourceParser 
		XmlPullParser newPullParser = Xml.newPullParser();
		newPullParser.getAttributeValue(null, "max");
		
		
		//2.读取Max
		
		//3.把每一条短信的信息读取出来
		
		//4.把短信内容插入到系统短信应用里面（数据库）
		
		
	}

}
