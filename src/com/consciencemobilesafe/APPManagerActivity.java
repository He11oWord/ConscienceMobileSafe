package com.consciencemobilesafe;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.domain.AppInfo;
import com.consciencemobilesafe.utils.AppInfoProvider;
import com.consciencemobilesafe.utils.DensityUtil;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.renderscript.ProgramFragmentFixedFunction.Builder.Format;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class APPManagerActivity extends Activity implements OnClickListener {

	@Override
	protected void onDestroy() {
		dismissPupopWindow();
		super.onDestroy();
	}

	// ListView��������Ҫ�Ķ���
	private ListView appInfo_lv;
	private ProgressBar pb;
	private AppInfoProvider appinfoProvider;
	private List<AppInfo> appList;
	protected APPListAdapter adapter;
	private List<AppInfo> userAppList;
	private List<AppInfo> sysAppList;

	// ����ʱ�õ�TextView
	private TextView tv_gun;
	// ��������
	private PopupWindow pw;

	/**
	 * ����
	 */
	private LinearLayout app_start;

	/**
	 * ж��
	 */
	private LinearLayout app_uninstall;
	/**
	 * ��������
	 */
	private LinearLayout app_share;
	/**
	 * ���������Ŀ
	 */
	private AppInfo info;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_manager_layout);

		TextView ram_tv = (TextView) findViewById(R.id.app_ram_tv);
		TextView sdcard_tv = (TextView) findViewById(R.id.app_sd_tv);

		// ����ڴ�Ŀ��ÿռ�
		ram_tv.setText("�ڴ����Ϊ:"
				+ Formatter.formatFileSize(this, getAvailSpace(Environment
						.getDataDirectory().getAbsolutePath())));
		// ���sd���Ŀ��ÿռ�
		sdcard_tv.setText("sdcard����Ϊ:"
				+ Formatter.formatFileSize(this, getAvailSpace(Environment
						.getExternalStorageDirectory().getAbsolutePath())));

		// �����Ǹ���listView�Ĳ���
		pb = (ProgressBar) findViewById(R.id.app_manager_pb);
		appInfo_lv = (ListView) findViewById(R.id.app_manager_listview);
		appinfoProvider = new AppInfoProvider();

		tv_gun = (TextView) findViewById(R.id.tv_gun);

		resetListView();

		// ����listView�Ĺ����¼�
		appInfo_lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			// ������ʱ������������
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPupopWindow();

				if (userAppList != null) {
					if (firstVisibleItem > userAppList.size()) {
						runOnUiThread(new Runnable() {
							public void run() {
								tv_gun.setText("ϵͳ����(" + sysAppList.size()
										+ ")");
							}
						});
					} else {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								tv_gun.setText("�û�����(" + userAppList.size()
										+ ")");
							}
						});
					}
				}

			}
		});

		// ����listView�ĵ�������¼�
		appInfo_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// �жϱ��������Ŀλ�ã�����app��Ϣ
				if (position == (userAppList.size())) {
					return;
				} else if (position < userAppList.size()) {
					int newPosition = position;
					info = (AppInfo) userAppList.get(newPosition);
				} else {
					int newPosition = position - userAppList.size() - 1;
					info = (AppInfo) sysAppList.get(newPosition);
				}
				// ȡ����ʾ������
				dismissPupopWindow();

				
				View tempView = view.inflate(getApplicationContext(),
						R.layout.ap_manager_pupop_item, null);
				pw = new PopupWindow(tempView, -2,
						ViewGroup.LayoutParams.WRAP_CONTENT);

				// ʵ�����������Բ���
				app_start = (LinearLayout) tempView
						.findViewById(R.id.app_manager_pupop_start);
				app_uninstall = (LinearLayout) tempView
						.findViewById(R.id.app_manager_pupop_uninstall);
				app_share = (LinearLayout) tempView
						.findViewById(R.id.app_manager_pupop_share);

				// �����ĵ���¼���ʵ�ֽӿ�
				app_start.setOnClickListener(APPManagerActivity.this);
				app_uninstall.setOnClickListener(APPManagerActivity.this);
				app_share.setOnClickListener(APPManagerActivity.this);
				// ��õ�ǰ�����λ��
				int location[] = new int[2];
				view.getLocationInWindow(location);

				// ����Ҫ�����������ɫ����Ȼû�ж���Ч��
				pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				// ת��px��dip
				int xPx = 30;
				int xDip = DensityUtil.px2dip(APPManagerActivity.this, xPx);

				// �����壬��ߺ��ϱߣ�����Ļ��ߺ��ϱߵľ���
				pw.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, xDip,
						location[1]);

				// ��Ӷ���,��С�䵽��
				// Сx��x
				ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0);
				sa.setDuration(300);
				// ���һ�鶯��
				// AnimationSet set = (AnimationSet) new AnimationSet(false);
				// set.addAnimation(sa);
				tempView.startAnimation(sa);

			}

		});

	}

	/**
	 * ˢ�½���
	 */
	private void resetListView() {
		pb.setVisibility(View.VISIBLE);
		new Thread() {

			public void run() {

				userAppList = new ArrayList<AppInfo>();
				sysAppList = new ArrayList<AppInfo>();
				appList = appinfoProvider.getAppInfo(APPManagerActivity.this);
				for (AppInfo info : appList) {
					if (info.isUserApp()) {
						// ������û����򣬾���ӵ��û������б���
						userAppList.add(info);
					} else {
						// ϵͳӦ�ó���
						sysAppList.add(info);
					}
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						if (adapter == null) {
							adapter = new APPListAdapter();
							appInfo_lv.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						pb.setVisibility(View.INVISIBLE);
					}
				});
			}

		}.start();
	}
	
	/**
	 * ȡ����ʾ������
	 */
	private void dismissPupopWindow() {
		if (pw != null && pw.isShowing()) {
			pw.dismiss();
			pw = null;
		}
	}

	/**
	 * ������
	 */
	class APPListAdapter extends BaseAdapter {

		private AppInfo info;

		// ����ListView��ʾ����Ŀ����
		@Override
		public int getCount() {
			return userAppList.size() + sysAppList.size() + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// �ж�����λ�ã�����λ�ú�������ֵ
			if (position == (userAppList.size())) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.RED);
				tv.setText("ϵͳ����(" + sysAppList.size() + ")");
				tv.setTextColor(Color.GRAY);
				tv.setTextSize(18);
				return tv;
			} else if (position < userAppList.size()) {
				int newPosition = position;
				info = (AppInfo) userAppList.get(newPosition);
			} else {
				int newPosition = position - userAppList.size() - 1;
				info = (AppInfo) sysAppList.get(newPosition);
			}
			View view;
			ViewHolder viewHolder;

			// �������Ϊ�գ����һ�����Բ��ֵ�ʱ�򣬵��÷���
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();

			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.app_manager_listview_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.iv = (ImageView) view
						.findViewById(R.id.app_manager_lv_item_iv);
				viewHolder.tv1 = (TextView) view
						.findViewById(R.id.app_manager_lv_item_tv1);
				viewHolder.tv2 = (TextView) view
						.findViewById(R.id.app_manager_lv_item_tv2);
				view.setTag(viewHolder);
			}

			viewHolder.iv.setImageDrawable(info.getIcon());
			viewHolder.tv1.setText(info.getName());
			if (info.isUserApp()) {
				viewHolder.tv2.setText("�ֻ��ڴ�");
			} else {
				viewHolder.tv2.setText("�ⲿ�洢");
			}

			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	class ViewHolder {
		private TextView tv1;
		private TextView tv2;
		private ImageView iv;
		private Button b;

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

	@Override
	public void onClick(View v) {
		dismissPupopWindow();
		switch (v.getId()) {
		case R.id.app_manager_pupop_start:
			Log.d("", "statrt");
			startApplication();
			break;
		case R.id.app_manager_pupop_uninstall:
			if(info.isUserApp()){
				unistallApk();				
			}else{
				Toast.makeText(getApplicationContext(), "ϵͳ����Ӧ��", 0).show();
			}
			break;
		case R.id.app_manager_pupop_share:
			shareApplication();
			break;
		}
	}

	/**
	 * �������
	 */
	private void shareApplication() {
		Intent intent =new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "�Ƽ�����һ����������ֽ�����"+info.getName());
		startActivity(intent);
	}

	/**
	 * ж��Ӧ�ó���
	 */
	private void unistallApk() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + info.getPackName()));
		startActivityForResult(intent, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		resetListView();
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ��Ӧ�ó���
	 */
	private void startApplication() {

		// //��ȡȫ���ܹ�������Ӧ�ó���
		PackageManager pm = getPackageManager();
		// Intent intent = new Intent();
		// intent.setAction("android.intent.action.MAIN");
		// intent.addCategory("android.intent.category.LAUNCHER");
		// List<ResolveInfo> queryIntentActivities =
		// pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);

		// ��ȡĳһ����������Ӧ�ó���
		Intent intentForPackage = pm.getLaunchIntentForPackage(info
				.getPackName());
		if (intentForPackage != null) {
			startActivity(intentForPackage);
		} else {
			Toast.makeText(this, "��������", 0).show();
		}
	}

}
