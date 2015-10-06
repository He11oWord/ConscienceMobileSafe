package edu.gxut.consciencemobilesafe.ui;

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
	
	
	public void iniView(Context context){
		
		//把一个布局文件转换成View并且加载到SettingItemView上
		View.inflate(context, R.layout.setting_item_view, this);
		
		item_cb= (CheckBox) this.findViewById(R.id.item_checkbox);
		item_desc = (TextView) this.findViewById(R.id.item_desc);
	}
	
	
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
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
		item_cb.setChecked(checked);
	}
	
	/**
	 * 设置描述文字
	 */
	public void setText(String text){
		item_desc.setText(text);
	}
}
