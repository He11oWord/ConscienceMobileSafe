package com.consciencemobilesafe.ui;

import com.consciencemobilesafe.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义组合控件
 * 里面有2个TextView，1个CheckBox，1个View
 */
public class SettingItemView extends RelativeLayout {
	
	private CheckBox item_cb;
	private TextView item_desc;
	private TextView item_title;
	
	
	private String desc_off;
	private String desc_on;
	
	public void iniView(Context context){
		
		//把一个布局文件转换成View并且加载到SettingItemView上
		View.inflate(context, R.layout.setting_item_view, this);
		
		item_cb= (CheckBox) this.findViewById(R.id.item_checkbox);
		item_desc = (TextView) this.findViewById(R.id.item_desc);
		item_title = (TextView) this.findViewById(R.id.item_title);
	}
	
	
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}

	
	/**
	 * 在布局文件中调用的时候，使用这个方法
	 * 带有2个参数
	 * @param context
	 * @param attrs
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		String tv_title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.consciencemobilesafe.app", "title");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.consciencemobilesafe.app", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.consciencemobilesafe.app", "desc_off");
		
		item_title.setText(tv_title);
		setText(desc_off);
		
		
	}

	public SettingItemView(Context context) {
		super(context);
		iniView(context);
	}
	
	
	
	
	/**
	 * 判断是否选中
	 */
	public boolean isChecked(){
		return item_cb.isChecked();
	}

	/**
	 * 设置该Item的状态
	 */
	public void setCheck(boolean checked){
		if(checked){
			setText(desc_on);
		}else{
			setText(desc_off);
		}
		item_cb.setChecked(checked);
	}
	
	/**
	 * 设置描述文字
	 */
	public void setText(String text){
		item_desc.setText(text);
	}
}
