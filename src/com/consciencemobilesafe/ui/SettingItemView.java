package edu.gxut.consciencemobilesafe.ui;

import com.consciencemobilesafe.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * �Զ�����Ͽؼ�
 * ������2��TextView��1��CheckBox��1��View
 */
public class SettingItemView extends RelativeLayout {
	
	private CheckBox item_cb;
	private TextView item_desc;
	
	
	public void iniView(Context context){
		
		//��һ�������ļ�ת����View���Ҽ��ص�SettingItemView��
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
	 * �ж��Ƿ�ѡ��
	 */
	public boolean isChecked(){
		return item_cb.isChecked();
	}

	/**
	 * ���ø�Item��״̬
	 */
	public void setCheck(boolean checked){
		item_cb.setChecked(checked);
	}
	
	/**
	 * ������������
	 */
	public void setText(String text){
		item_desc.setText(text);
	}
}
