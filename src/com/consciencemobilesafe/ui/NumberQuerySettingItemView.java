package com.consciencemobilesafe.ui;

import com.consciencemobilesafe.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * �Զ�����Ͽؼ�
 * ������2��TextView��1��ImageView��1��View
 */
public class NumberQuerySettingItemView extends RelativeLayout {
	
	private ImageView numberImageView;
	private TextView numberQureyDesc;
	private TextView numberQueryTitle;
	
	
	private String desc_off;
	private String desc_on;
	
	public void iniView(Context context){
		
		//��һ�������ļ�ת����View���Ҽ��ص�SettingItemView��
		View.inflate(context, R.layout.number_address_setting_item_view, this);
		
		numberImageView= (ImageView) this.findViewById(R.id.item_number_query_image);
		numberQureyDesc = (TextView) this.findViewById(R.id.item_number_query_desc);
		numberQueryTitle = (TextView) this.findViewById(R.id.item_number_query_title);
	}
	
	
	public NumberQuerySettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}

	
	/**
	 * �ڲ����ļ��е��õ�ʱ��ʹ���������
	 * ����2������
	 * @param context
	 * @param attrs
	 */
	public NumberQuerySettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		String tv_title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.consciencemobilesafe.app", "title");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.consciencemobilesafe.app", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.consciencemobilesafe.app", "desc_off");
		
		numberQueryTitle.setText(tv_title);
		setText(desc_off);
		
		
	}

	public NumberQuerySettingItemView(Context context) {
		super(context);
		iniView(context);
	}
	
	
	
	
//	/**
//	 * �ж��Ƿ�ѡ��
//	 */
//	public boolean isChecked(){
//		return item_cb.isChecked();
//	}
//
	/**
	 * ���ø�Item��״̬
	 */
	public void setCheck(boolean checked){
		if(checked){
			setText(desc_on);
		}else{
			setText(desc_off);
		}	
	}
	/**
	 * ������������
	 */
	public void setText(String text){
		numberQureyDesc.setText(text);
	}
}
