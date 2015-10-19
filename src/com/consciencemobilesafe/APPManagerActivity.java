package com.consciencemobilesafe;

import com.consciencemobilesafe.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.renderscript.ProgramFragmentFixedFunction.Builder.Format;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

public class APPManagerActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_manager_layout);

		TextView ram_tv = (TextView) findViewById(R.id.app_ram_tv);
		TextView sdcard_tv = (TextView) findViewById(R.id.app_sd_tv);

		//����ڴ�Ŀ��ÿռ�
		ram_tv.setText("�ڴ����Ϊ:"
				+ Formatter.formatFileSize(this, getAvailSpace(Environment
						.getDataDirectory().getAbsolutePath())));
		//���sd���Ŀ��ÿռ�
		sdcard_tv.setText("sdcard����Ϊ:"
				+ Formatter.formatFileSize(this, getAvailSpace(Environment
						.getExternalStorageDirectory().getAbsolutePath())));
	}

	/**
	 * ���ĳ��Ŀ¼�Ŀ��ÿռ�
	 * 
	 * @param path
	 *            Ŀ¼��λ��
	 * @return ���õĴ�С
	 */
	public long getAvailSpace(String path) {
		// ��ò鿴��С��ʵ��
		StatFs s = new StatFs(path);
		s.getBlockCount();// ��÷�������
		long size = s.getBlockSize();// ��÷����Ĵ�С
		long count = s.getAvailableBlocks();// ��ÿ�������ĸ���
		return size * count;
	}
}
