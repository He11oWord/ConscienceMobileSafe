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

	// ListView里面所需要的东西
	private ListView appInfo_lv;
	private ProgressBar pb;
	private AppInfoProvider appinfoProvider;
	private List<AppInfo> appList;
	protected APPListAdapter adapter;
	private List<AppInfo> userAppList;
	private List<AppInfo> sysAppList;

	// 滚动时用的TextView
	private TextView tv_gun;
	// 悬浮窗体
	private PopupWindow pw;

	/**
	 * 启动
	 */
	private LinearLayout app_start;

	/**
	 * 卸载
	 */
	private LinearLayout app_uninstall;
	/**
	 * 启动分享
	 */
	private LinearLayout app_share;
	/**
	 * 被点击的条目
	 */
	private AppInfo info;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_manager_layout);

		TextView ram_tv = (TextView) findViewById(R.id.app_ram_tv);
		TextView sdcard_tv = (TextView) findViewById(R.id.app_sd_tv);

		// 获得内存的可用空间
		ram_tv.setText("内存可用为:"
				+ Formatter.formatFileSize(this, getAvailSpace(Environment
						.getDataDirectory().getAbsolutePath())));
		// 获得sd卡的可用空间
		sdcard_tv.setText("sdcard可用为:"
				+ Formatter.formatFileSize(this, getAvailSpace(Environment
						.getExternalStorageDirectory().getAbsolutePath())));

		// 下面是更新listView的操作
		pb = (ProgressBar) findViewById(R.id.app_manager_pb);
		appInfo_lv = (ListView) findViewById(R.id.app_manager_listview);
		appinfoProvider = new AppInfoProvider();

		tv_gun = (TextView) findViewById(R.id.tv_gun);

		resetListView();

		// 设置listView的滚动事件
		appInfo_lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			// 滚动的时候调用这个方法
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPupopWindow();

				if (userAppList != null) {
					if (firstVisibleItem > userAppList.size()) {
						runOnUiThread(new Runnable() {
							public void run() {
								tv_gun.setText("系统程序(" + sysAppList.size()
										+ ")");
							}
						});
					} else {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								tv_gun.setText("用户程序(" + userAppList.size()
										+ ")");
							}
						});
					}
				}

			}
		});

		// 设置listView的单条点击事件
		appInfo_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 判断被点击的条目位置，返回app信息
				if (position == (userAppList.size())) {
					return;
				} else if (position < userAppList.size()) {
					int newPosition = position;
					info = (AppInfo) userAppList.get(newPosition);
				} else {
					int newPosition = position - userAppList.size() - 1;
					info = (AppInfo) sysAppList.get(newPosition);
				}
				// 取消显示悬浮窗
				dismissPupopWindow();

				
				View tempView = view.inflate(getApplicationContext(),
						R.layout.ap_manager_pupop_item, null);
				pw = new PopupWindow(tempView, -2,
						ViewGroup.LayoutParams.WRAP_CONTENT);

				// 实例化各个线性布局
				app_start = (LinearLayout) tempView
						.findViewById(R.id.app_manager_pupop_start);
				app_uninstall = (LinearLayout) tempView
						.findViewById(R.id.app_manager_pupop_uninstall);
				app_share = (LinearLayout) tempView
						.findViewById(R.id.app_manager_pupop_share);

				// 启动的点击事件，实现接口
				app_start.setOnClickListener(APPManagerActivity.this);
				app_uninstall.setOnClickListener(APPManagerActivity.this);
				app_share.setOnClickListener(APPManagerActivity.this);
				// 获得当前窗体的位置
				int location[] = new int[2];
				view.getLocationInWindow(location);

				// 必须要给这个设置颜色，不然没有动画效果
				pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				// 转化px到dip
				int xPx = 30;
				int xDip = DensityUtil.px2dip(APPManagerActivity.this, xPx);

				// 父窗体，左边和上边，距屏幕左边和上边的距离
				pw.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, xDip,
						location[1]);

				// 添加动画,从小变到大
				// 小x，x
				ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0);
				sa.setDuration(300);
				// 添加一组动画
				// AnimationSet set = (AnimationSet) new AnimationSet(false);
				// set.addAnimation(sa);
				tempView.startAnimation(sa);

			}

		});

	}

	/**
	 * 刷新界面
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
						// 如果是用户程序，就添加到用户程序列表中
						userAppList.add(info);
					} else {
						// 系统应用程序
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
	 * 取消显示悬浮框
	 */
	private void dismissPupopWindow() {
		if (pw != null && pw.isShowing()) {
			pw.dismiss();
			pw = null;
		}
	}

	/**
	 * 适配器
	 */
	class APPListAdapter extends BaseAdapter {

		private AppInfo info;

		// 控制ListView显示的条目数量
		@Override
		public int getCount() {
			return userAppList.size() + sysAppList.size() + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// 判断它的位置，根据位置和来设置值
			if (position == (userAppList.size())) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.RED);
				tv.setText("系统程序(" + sysAppList.size() + ")");
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

			// 当这个不为空，而且还是相对布局的时候，调用方法
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
				viewHolder.tv2.setText("手机内存");
			} else {
				viewHolder.tv2.setText("外部存储");
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
				Toast.makeText(getApplicationContext(), "系统级别应用", 0).show();
			}
			break;
		case R.id.app_manager_pupop_share:
			shareApplication();
			break;
		}
	}

	/**
	 * 分享软件
	 */
	private void shareApplication() {
		Intent intent =new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "推荐给你一款软件，名字叫做："+info.getName());
		startActivity(intent);
	}

	/**
	 * 卸载应用程序
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
	 * 打开应用程序
	 */
	private void startApplication() {

		// //获取全部能够开启的应用程序
		PackageManager pm = getPackageManager();
		// Intent intent = new Intent();
		// intent.setAction("android.intent.action.MAIN");
		// intent.addCategory("android.intent.category.LAUNCHER");
		// List<ResolveInfo> queryIntentActivities =
		// pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);

		// 获取某一个能启动的应用程序
		Intent intentForPackage = pm.getLaunchIntentForPackage(info
				.getPackName());
		if (intentForPackage != null) {
			startActivity(intentForPackage);
		} else {
			Toast.makeText(this, "不能启动", 0).show();
		}
	}

}
