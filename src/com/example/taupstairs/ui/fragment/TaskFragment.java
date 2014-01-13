package com.example.taupstairs.ui.fragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.example.taupstairs.R;
import com.example.taupstairs.adapter.TaskAdapter;
import com.example.taupstairs.app.TaUpstairsApplication;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.Time;
import com.example.taupstairs.logic.ItaFragment;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.StatusService;
import com.example.taupstairs.string.HomePageString;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.ui.activity.TaskDetailActivity;
import com.example.taupstairs.util.SharedPreferencesUtil;
import com.example.taupstairs.util.TimeUtil;
import com.example.taupstairs.view.XListView;
import com.example.taupstairs.view.XListView.IXListViewListener;

public class TaskFragment extends Fragment implements ItaFragment {

	private Context context;
	private View view;
	private XListView xlist_task;
	private TaskAdapter adapter;
	private List<Status> currentStatus;
	private int clickPosition;
	
	/*是否正在加载任务*/
	private boolean isRefresh;
	
	private StatusService statusService;
	private String lastestStatusId;
	private String oldestStatusId;
	
	private String lastestUpdata;
	private Time lastestRefreshTime;
	
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
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view =  inflater.inflate(R.layout.fm_task, container, false);
		initView();
		return view;
	}
	
	@Override
	public void initData() {
		isRefresh = false;
		lastestStatusId = SharedPreferencesUtil.getLastestStatusId(context);
		if (null == lastestStatusId) {
			getStatusFromTask(Task.TA_GETSTATUS_MODE_FIRSTTIME, null);		
		} else {
			getStatusFromTask(Task.TA_GETSTATUS_MODE_PULLREFRESH, lastestStatusId);	
		}
		statusService = new StatusService(context);	
		currentStatus = statusService.getListStatus();
	}

	@Override
	public void initView() {
		xlist_task = (XListView) view.findViewById(R.id.xlist_fm_task);	
		if (currentStatus != null) {
			adapter = new TaskAdapter(context, currentStatus);
			xlist_task.setAdapter(adapter);
		}
		xlist_task.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (currentStatus.size() >= arg2) {
					clickPosition = arg2 - 1;
					/*全局变量传递数据*/
					Intent intent = new Intent(context, TaskDetailActivity.class);
					TaUpstairsApplication app = (TaUpstairsApplication) getActivity().getApplication();
					app.setStatus(currentStatus.get(arg2 - 1));
					startActivityForResult(intent, IntentString.RequestCode.TASKFRAGMENT_TASKDETAIL);
				}
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
	 * HomePage的本地回调
	 */
	public void localRefresh(int id, Map<String, Object> params) {
		switch (id) {
		case HomePageString.UPDATA_PHOTO:
			String personId_p = (String) params.get(Person.PERSON_ID);
			String personPhotoUrl = (String) params.get(Person.PERSON_PHOTOURL);
			for (Status status : currentStatus) {
				if (personId_p.equals(status.getPersonId())) {
					status.setPersonPhotoUrl(personPhotoUrl);
				}
			}
			adapter.notifyDataSetChanged();
			break;
		case HomePageString.UPDATA_NICKNAME:
			String personId_n = (String) params.get(Person.PERSON_ID);
			String personNickname = (String) params.get(Person.PERSON_NICKNAME);
			for (Status status : currentStatus) {
				if (personId_n.equals(status.getPersonId())) {
					status.setPersonNickname(personNickname);
				}
			}
			adapter.notifyDataSetChanged();
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
				adapter = new TaskAdapter(context, currentStatus);
				xlist_task.setAdapter(adapter);
				setLastestUpdata();
			} else if (mode.equals(Task.TA_GETSTATUS_MODE_PULLREFRESH)) {
				if (newStatus.size() < 20) {
					if (currentStatus != null) {
						/*这里一定要放到最头部，那样显示才不会乱*/
						currentStatus.addAll(0, newStatus);
						adapter.notifyDataSetChanged();
					} else {	/*读取数据库可能失败。或许是上次没保存好*/
						currentStatus = newStatus;
						adapter = new TaskAdapter(context, currentStatus);
						xlist_task.setAdapter(adapter);
					}
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
		String hour, minute;
		lastestRefreshTime = TimeUtil.getNow(Calendar.getInstance());
		
		if (lastestRefreshTime.getHour() < 10) {
			hour = "0" + lastestRefreshTime.getHour();
		} else {
			hour = "" + lastestRefreshTime.getHour();
		}
		if (lastestRefreshTime.getMinute() < 10) {
			minute = "0" + lastestRefreshTime.getMinute();
		} else {
			minute = "" + lastestRefreshTime.getMinute();
		}
		
		lastestUpdata = hour + ":" + minute;
	}
	
	/*
	 * 改变list相关的保存的数据
	 */
	private void changeListData() {
		xlist_task.stopRefresh();
		xlist_task.stopLoadMore();
		xlist_task.setPullLoadEnable(true);
		xlist_task.setRefreshTime(lastestUpdata);
		if (currentStatus.size() > 0) {
			lastestStatusId = currentStatus.get(0).getStatusId();
			oldestStatusId = currentStatus.get(currentStatus.size() - 1).getStatusId();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentString.RequestCode.TASKFRAGMENT_TASKDETAIL:
			if (IntentString.ResultCode.TASKDETAIL_TASKFRAGMENT == resultCode) {
				TaUpstairsApplication app = (TaUpstairsApplication) getActivity().getApplication();
				Status status = app.getStatus();
				currentStatus.add(clickPosition, status);
				currentStatus.remove(clickPosition + 1);
				adapter.notifyDataSetChanged();
			}
			break;
			
		/*这里捕获不到，看来要在homepage捕获到，再调用*/
		case IntentString.RequestCode.HOMEPAGE_WRITE:
			if (IntentString.ResultCode.WRITE_HOMEPAGE == resultCode) {
				Toast.makeText(context, "成功发布任务", Toast.LENGTH_SHORT).show();
				getStatusFromTask(Task.TA_GETSTATUS_MODE_PULLREFRESH, lastestStatusId);
			}
			break;

		default:
			break;
		}
	}
	
	public void releaseTaskSuccess() {
		Toast.makeText(context, "成功发布任务", Toast.LENGTH_SHORT).show();
		getStatusFromTask(Task.TA_GETSTATUS_MODE_PULLREFRESH, lastestStatusId);
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
