package com.consciencemobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.consciencemobilesafe.APPManagerActivity.APPListAdapter;
import com.consciencemobilesafe.APPManagerActivity.ViewHolder;
import com.consciencemobilesafe.app.R;
import com.consciencemobilesafe.domain.AppInfo;
import com.consciencemobilesafe.domain.TaskInfo;
import com.consciencemobilesafe.engine.TaskInfoProvider;
import com.consciencemobilesafe.utils.AppInfoProvider;
import com.consciencemobilesafe.utils.TaskManagerUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class TaskManagerActivity extends Activity {
	private TaskManagerUtil tmu;

	// ListView��������Ҫ�Ķ���
	private ListView taskInfo_lv;
	private ProgressBar pb;
	private TaskInfoProvider taskInfoProvider;
	private List<TaskInfo> taskList;
	protected TaskListAdapter adapter;
	private List<TaskInfo> userTaskList;
	private List<TaskInfo> sysTaskList;

	// ����ʱ�õ�TextView
	private TextView tv_gun;

	// ������Ϣ��
	private TaskInfo info;
	// �����Ƿ񱻵����
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.task_manager_layout);

		TextView number_tv = (TextView) findViewById(R.id.task_manager_tv1);
		TextView ram_tv = (TextView) findViewById(R.id.task_manager_tv2);
		List<RunningAppProcessInfo> runningAppProcesses = TaskManagerUtil
				.getRunningAppProcesses(this);
		int number_text = runningAppProcesses.size();
		number_tv.setText("��������:" + number_text);
		String totalMem = Formatter.formatFileSize(this, tmu.getTotalMem(this));
		String aVilMem = Formatter.formatFileSize(this, tmu.getAVilMem(this));

		String ram_text = "ʣ���ڴ�" + aVilMem + "/" + totalMem;
		ram_tv.setText(ram_text);
		// Toast.makeText(this, "221", 0).show();

		tv_gun = (TextView) findViewById(R.id.tv_gun);
		taskInfo_lv = (ListView) findViewById(R.id.task_manager_listview);
		pb = (ProgressBar) findViewById(R.id.task_manager_pb);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		taskInfoProvider = new TaskInfoProvider();
		resetListView();

		// ����ListView�Ĺ����¼�
		taskInfo_lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			// ������ʱ������������
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// dismissPupopWindow();

				if (userTaskList != null) {
					if (firstVisibleItem > userTaskList.size()) {
						runOnUiThread(new Runnable() {
							public void run() {
								tv_gun.setText("ϵͳ����(" + sysTaskList.size()
										+ ")");
							}
						});
					} else {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								tv_gun.setText("�û�����(" + userTaskList.size()
										+ ")");
							}
						});
					}
				}

			}
		});

		// ����ListView����Ŀ����¼�
		taskInfo_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// �ж�����λ�ã�����λ�ú�������ֵ
				TaskInfo info;
				if (position == (userTaskList.size())) {
					return;
				} else if (position < userTaskList.size()) {
					int newPosition = position;
					info = (TaskInfo) userTaskList.get(newPosition);
				} else {
					int newPosition = position - userTaskList.size() - 1;
					info = (TaskInfo) sysTaskList.get(newPosition);
				}
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				if (info.isCheck()) {
					viewHolder.cb.setChecked(false);
					info.setCheck(false);
				} else {
					viewHolder.cb.setChecked(true);
					info.setCheck(true);
				}
			}
		});
	}

	/**
	 * ����������
	 */
	public class TaskListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userTaskList.size() + sysTaskList.size() + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TextView tv = new TextView(getApplicationContext());
			// tv.setText(taskList.get(position).toString());
			// tv.setTextColor(Color.BLACK);
			// return tv;

			// �ж�����λ�ã�����λ�ú�������ֵ
			if (position == (userTaskList.size())) {
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.RED);
				tv.setText("ϵͳ����(" + sysTaskList.size() + ")");
				tv.setTextColor(Color.GRAY);
				tv.setTextSize(18);
				return tv;
			} else if (position < userTaskList.size()) {
				int newPosition = position;
				info = (TaskInfo) userTaskList.get(newPosition);
			} else {
				int newPosition = position - userTaskList.size() - 1;
				info = (TaskInfo) sysTaskList.get(newPosition);
			}
			View view;
			final ViewHolder viewHolder;

			// �������Ϊ�գ����һ�����Բ��ֵ�ʱ�򣬵��÷���
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();

			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.task_manager_listview_layout, null);
				viewHolder = new ViewHolder();
				viewHolder.iv = (ImageView) view
						.findViewById(R.id.task_manager_lv_item_iv);
				viewHolder.tv1 = (TextView) view
						.findViewById(R.id.task_manager_lv_item_tv1);
				viewHolder.tv2 = (TextView) view
						.findViewById(R.id.task_manager_lv_item_tv2);
				view.setTag(viewHolder);
				viewHolder.cb = (CheckBox) view
						.findViewById(R.id.task_manager_lv_item_cb);

			}

			viewHolder.cb.setClickable(false);
			viewHolder.iv.setImageDrawable(info.getIcon());
			viewHolder.tv1.setText(info.getName());
			viewHolder.tv2.setText("�ڴ�ռ�ã�"
					+ Formatter.formatFileSize(TaskManagerActivity.this,
							info.getUseRam()));

			viewHolder.cb.setChecked(info.isCheck());
			//

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
		private CheckBox cb;

	}

	/**
	 * ����ListView
	 */
	private void resetListView() {
		pb.setVisibility(View.VISIBLE);
		new Thread() {

			public void run() {

				userTaskList = new ArrayList<TaskInfo>();
				sysTaskList = new ArrayList<TaskInfo>();
				try {
					taskList = taskInfoProvider
							.getTaskInfo(TaskManagerActivity.this);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				for (TaskInfo info : taskList) {
					if (info.isUserTask()) {
						// ������û����򣬾���ӵ��û������б���
						userTaskList.add(info);
					} else {
						// ϵͳӦ�ó���
						sysTaskList.add(info);
					}
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						if (adapter == null) {
							adapter = new TaskListAdapter();
							taskInfo_lv.setAdapter(adapter);
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
	 * ȫѡ
	 */
	public void selectAll(View view) {
		for (TaskInfo ti : taskList) {
			ti.setCheck(true);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * ��ѡ
	 */
	public void selectOther(View view) {
		for (TaskInfo ti : taskList) {
			ti.setCheck(!ti.isCheck());
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * ����
	 */
	public void setting(View view) {

	}

	/**
	 * һ������
	 */
	public void clean(View view) {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (TaskInfo ti : taskList) {
			if (ti.isCheck()) {
				activityManager.killBackgroundProcesses(ti.getPackageName());
			}
		}
		resetListView();

	}
}
