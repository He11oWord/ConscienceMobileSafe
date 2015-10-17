package com.consciencemobilesafe;

import java.util.List;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.bean.BlcakNumber;
import com.consciencemobilesafe.service.BlackSmsService;
import com.consciencemobilesafe.utils.BlackNumberDBUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NumberSmsSafeActivity extends Activity {

	private ListView number_sms_safe_lv;
	private BlackNumberListAdapter adapter;
	private List<BlcakNumber> infos;
	private BlackNumberDBUtil b;

	//private Button button = (Button) findViewById(R.id.button);
	public void button(View view){
		Intent intent = new Intent(this,BlackSmsService.class);
		startService(intent);
	
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.number_sms_safe_layout);
		number_sms_safe_lv = (ListView) findViewById(R.id.number_sms_safe_listview);
		b = new BlackNumberDBUtil(this);
		infos = b.queryAll();
		adapter = new BlackNumberListAdapter();

		number_sms_safe_lv.setAdapter(adapter);

	}



	class BlackNumberListAdapter extends BaseAdapter {

		public int getCount() {
			return infos.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// 有多少个条目显示,这个方法就会调用多少次
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;
			ViewHolder viewHolder;
			// 1.减少view的个数
			// 当还没有view时要去new，有了之后直接实现动态平衡
			if (convertView == null) {

				view = View.inflate(NumberSmsSafeActivity.this,
						R.layout.black_number_item_layout, null);

				// 2.减少Id的查询次数
				viewHolder = new ViewHolder();
				// 第一次new出来的时候获得地址
				viewHolder.tv_number = (TextView) view
						.findViewById(R.id.black_number_lv_tv);
				viewHolder.tv_mode = (TextView) view
						.findViewById(R.id.black_mode_lv_tv);
				view.setTag(viewHolder);
				viewHolder.ib_delete = (ImageButton) view
						.findViewById(R.id.black_mode_button);
			} else {
				view = convertView;
				// 复用之前的地址
				viewHolder = (ViewHolder) view.getTag();
			}

			viewHolder.tv_number.setText(infos.get(position).getNumber());
			String mode = infos.get(position).getMode();
			if ("1".equals(mode)) {
				viewHolder.tv_mode.setText("电话拦截");
			} else if ("2".equals(mode)) {
				viewHolder.tv_mode.setText("短信拦截");
			} else if ("3".equals(mode)) {
				viewHolder.tv_mode.setText("电话和短信拦截");
			}

			viewHolder.ib_delete.setOnClickListener(new OnClickListener() {

				private AlertDialog deleteDialog;

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(
							NumberSmsSafeActivity.this);

					builder.setPositiveButton("杀意已决",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 确定删除
									// 删除数据
									String deleteNumber = infos.get(position)
											.getNumber();
									BlackNumberDBUtil b = new BlackNumberDBUtil(
											NumberSmsSafeActivity.this);
									b.delete(deleteNumber);
									// 更新显示
									// 将该属性添加至集合里面
									infos.remove(position);
									// 通知适配器进行更新
									adapter.notifyDataSetChanged();
								}
							});
					builder.setNegativeButton("容我想想",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									deleteDialog.dismiss();
								}
							});

					deleteDialog = builder.create();
					deleteDialog.setTitle("你确定删除吗？");
					deleteDialog.show();
				}
			});

			return view;
		}
	}

	/**
	 * view对象的容器 记录2个孩子的地址 相当于一个记事本
	 * 
	 */
	class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
		ImageButton ib_delete;
	}

	/**
	 * 添加黑名单
	 */
	private Button cancel_button;
	private Button ok_button;
	private CheckBox sms_cb;
	private CheckBox phone_cb;
	private TextView blackNumber_et;
	private AlertDialog dialog;
	// 短信接收器
	
	public void addBlackNumber(View view) {
		AlertDialog.Builder builder = new Builder(this);
		dialog = builder.create();
		View v = View.inflate(this, R.layout.dialog_set_black_number_layout,
				null);
		dialog.setView(v, 0, 0, 0, 0);
		dialog.show();

		blackNumber_et = (TextView) v.findViewById(R.id.black_number_dialog_et);
		phone_cb = (CheckBox) v.findViewById(R.id.black_number_dialog_phone_cb);
		sms_cb = (CheckBox) v.findViewById(R.id.black_number_dialog_sms_cb);
		ok_button = (Button) v.findViewById(R.id.black_number_dialog_ok_button);
		cancel_button = (Button) v
				.findViewById(R.id.black_number_dialog_cancel_button);

		phone_cb.setChecked(true);
		sms_cb.setChecked(true);

		// 点击取消按钮
		cancel_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// 点击确认按钮
		ok_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = blackNumber_et.getText().toString();
				if (TextUtils.isEmpty(number)) {
					Toast.makeText(NumberSmsSafeActivity.this, "号码不为空", 0)
							.show();
					return;
				}

				BlackNumberDBUtil b = new BlackNumberDBUtil(
						NumberSmsSafeActivity.this);
				boolean phone_click = phone_cb.isChecked();
				boolean sms_click = sms_cb.isChecked();
				if (!phone_click && !sms_click) {
					Toast.makeText(NumberSmsSafeActivity.this, "请不要逗我", 0)
							.show();
					return;
				}
				String mode = "3";

				if (phone_click && sms_click) {
					mode = "3";
				} else {
					if (phone_click) {
						mode = "1";
					} else if (sms_click) {
						mode = "2";
					}
				}
				b.insert(number, mode);

				// 将该属性添加至集合里面
				BlcakNumber info = new BlcakNumber();
				info.setNumber(number);
				info.setMode(mode);
				infos.add(0, info);
				// 通知适配器进行更新
				adapter.notifyDataSetChanged();
				number = null;
				mode = null;
				dialog.dismiss();

			}
		});
	}

	
}
