package com.example.taupstairs.ui;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.example.taupstairs.R;
import com.example.taupstairs.adapter.TaskAdapter;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.Time;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.StatusService;
import com.example.taupstairs.util.SharedPreferencesUtil;
import com.example.taupstairs.view.XListView;
import com.example.taupstairs.view.XListView.IXListViewListener;

public class TaskFragment extends Fragment implements ItaFragment {

	private Context context;
	private View view;
	private XListView xlist_task;
	private TaskAdapter adapter;
	private List<Status> currentStatus;
	
	/*是否正在加载任务*/
	private boolean isRefresh;
	
	private StatusService statusService;
	private String lastestStatusId;
	private String oldestStatusId;
	
	private String lastestUpdata;
	private Time lastestRefreshTime, now;
	
	public TaskFragment() {
		super();
	}
	
	public TaskFragment(Context context) {
		super();
		this.context = context;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainService.addFragment(TaskFragment.this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view =  inflater.inflate(R.layout.fm_task, container, false);
		init();
		return view;
	}

	@Override
	public void init() {
		initData();
		initView();
	}
	
	/*
	 * 初始化数据
	 */
	private void initData() {
		isRefresh = false;
		statusService = new StatusService(context);	
		lastestStatusId = SharedPreferencesUtil.getLastestStatusId(context);
	}
	
	/*
	 * 初始化ui，以及一些监听器
	 */
	private void initView() {
		xlist_task = (XListView) view.findViewById(R.id.xlist_fm_task);	
		
		initLoad();
		initListItem();	
		
	}
	
	/*
	 * 每一次打开都要联网加载
	 */
	private void initLoad() {
		if (null == lastestStatusId) {
			getStatusFromTask(Task.TA_GETSTATUS_MODE_FIRSTTIME, null);		
		} else {
			getStatusFromTask(Task.TA_GETSTATUS_MODE_PULLREFRESH, lastestStatusId);	
		}
	}
	
	/*
	 * 列表初始化
	 */
	private void initListItem() {
		getStatusFromFile();	
		xlist_task.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(context, TaskDetailActivity.class);
				startActivity(intent);
			}
		});
		xlist_task.setXListViewListener(new IXListViewListener() {
			public void onRefresh() {
				getStatusFromTask(Task.TA_GETSTATUS_MODE_PULLREFRESH, lastestStatusId);
			}
			
			public void onLoadMore() {
				getStatusFromTask(Task.TA_GETSTATUS_MODE_LOADMORE, oldestStatusId);
			}
		});
	}
	
	/*
	 * 上次获取到的任务保存在文件中，这次刚打开要先显示出来
	 */
	private void getStatusFromFile() {
		/*考虑到这一步的时间比较长，就不放到initData里面去初始化了，要先打开网络任务*/
		currentStatus = statusService.getListStatus();
		if (currentStatus != null) {
			adapter = new TaskAdapter(context, currentStatus);
			xlist_task.setAdapter(adapter);
		}
	}
	
	/*
	 * 加载任务。
	 * 参数标志着是不是第一次加载，下拉刷新，上拉加载更多
	 */
	private void getStatusFromTask(String mode, String statusId) {
		if (!isRefresh) {
			isRefresh = true;
			HashMap<String, Object> taskParams = new HashMap<String, Object>(2);
			taskParams.put(Task.TA_GETSTATUS_MODE, mode);
			taskParams.put(Task.TA_GETSTATUS_STATUSID, statusId);
			Task task = new Task(Task.TA_GETSTATUS, taskParams);
			MainService.addTask(task);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Object... params) {
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_GETSTATUS:
			String mode = (String) params[1];
			List<Status> newStatus = (List<Status>) params[2];
			refreshList(mode, newStatus);
			break;

		default:
			break;
		}
	}
	
	/*
	 * 刷新列表
	 */
	private void refreshList(String mode, List<Status> newStatus) {
		if (newStatus != null) {
			if (mode.equals(Task.TA_GETSTATUS_MODE_FIRSTTIME)) {
				currentStatus = newStatus;
				/*第一次上面不会设置这个，所以这里要设置*/
				xlist_task.setPullLoadEnable(true);
				adapter = new TaskAdapter(context, currentStatus);
				xlist_task.setAdapter(adapter);
				setLastestUpdata();
			} else if (mode.equals(Task.TA_GETSTATUS_MODE_PULLREFRESH)) {
				if (newStatus.size() < 20) {
					/*这里一定要放到最头部，那样显示才不会乱*/
					currentStatus.addAll(0, newStatus);
					adapter.notifyDataSetChanged();
				} else {
					currentStatus = newStatus;
					adapter.notifyDataSetInvalidated();
				}
				setLastestUpdata();
			} else if (mode.equals(Task.TA_GETSTATUS_MODE_LOADMORE)) {
				currentStatus.addAll(newStatus);
				adapter.notifyDataSetChanged();
			}
			changeListData();
		} else {
			xlist_task.stopRefresh();
			Toast.makeText(context, "没网络啊！！！亲", Toast.LENGTH_LONG).show();
		}
		
		/*把标志设为false，这样才能再开获取status的网络连接*/
		isRefresh = false;
	}
	
	/*
	 * lastestUpdata上次更新时间的设置。是给下次下拉的时候显示的
	 */
	private void setLastestUpdata() {
		now = getNow();
		/*刚刚打开软件，这个是为空的*/
		if (null == lastestRefreshTime) {
			lastestRefreshTime = now;
		}
		if (now.getDay() != lastestRefreshTime.getDay()) {
			lastestUpdata = "昨天    " + lastestRefreshTime.getHour() + ":" + lastestRefreshTime.getMinute();
		}
		lastestUpdata = lastestRefreshTime.getHour() + ":" + lastestRefreshTime.getMinute();
		lastestRefreshTime = now;
	}
	
	/*
	 * 获取当前时间的：天，时，分
	 */
	private Time getNow() {
		Time time = null;
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		time = new Time(day, hour, minute);
		
		return time;
	}
	
	/*
	 * 改变list相关的保存的数据
	 */
	private void changeListData() {
		xlist_task.stopRefresh();
		xlist_task.stopLoadMore();
		xlist_task.setPullLoadEnable(true);
		xlist_task.setRefreshTime(lastestUpdata);
		lastestStatusId = currentStatus.get(0).getStatusId();
		oldestStatusId = currentStatus.get(currentStatus.size() - 1).getStatusId();
	}

	/*
	 * 退出的时候保存一下最新任务ID和任务。
	 * 保存的方法里面只会保存一页（20条）的内容
	 */
	public void exit() {
		SharedPreferencesUtil.savaLastestStatusId(context, lastestStatusId);
		statusService.emptyStatusDb();
		statusService.insertListStatus(currentStatus);
		statusService.closeDBHelper();
	}
	
}
