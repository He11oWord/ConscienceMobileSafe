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

		//获得内存的可用空间
		ram_tv.setText("内存可用为:"
				+ Formatter.formatFileSize(this, getAvailSpace(Environment
						.getDataDirectory().getAbsolutePath())));
		//获得sd卡的可用空间
		sdcard_tv.setText("sdcard可用为:"
				+ Formatter.formatFileSize(this, getAvailSpace(Environment
						.getExternalStorageDirectory().getAbsolutePath())));
	}

	/**
	 * 获得某个目录的可用空间
	 * 
	 * @param path
	 *            目录的位置
	 * @return 可用的大小
	 */
	public long getAvailSpace(String path) {
		// 获得查看大小的实例
		StatFs s = new StatFs(path);
		s.getBlockCount();// 获得分区个数
		long size = s.getBlockSize();// 获得分区的大小
		long count = s.getAvailableBlocks();// 获得可用区块的个数
		return size * count;
	}
}
