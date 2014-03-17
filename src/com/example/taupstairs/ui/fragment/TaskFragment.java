package com.example.taupstairs.ui.fragment;

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
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaFragment;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.logic.TaUpstairsApplication;
import com.example.taupstairs.services.StatusService;
import com.example.taupstairs.string.NormalString;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.ui.activity.TaskDetailActivity;
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
	private String oldestStatusId;
	private String lastestUpdata;
	
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
		statusService = new StatusService(context);	
		currentStatus = statusService.getListStatus();
		getStatusFromTask(Task.TA_GETSTATUS_MODE_FIRSTTIME, null);
	}

	@Override
	public void initView() {
		xlist_task = (XListView) view.findViewById(R.id.xlist_fm_task);	
		xlist_task.setPullLoadEnable(false);
		if (currentStatus != null) {
			adapter = new TaskAdapter(context, currentStatus);
			xlist_task.setAdapter(adapter);
		}
		xlist_task.setOnItemClickListener(new OnItemClickListener() {
			@Override
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
			@Override
			public void onRefresh() {
				getStatusFromTask(Task.TA_GETSTATUS_MODE_FIRSTTIME, null);	
			}
			@Override
			public void onLoadMore() {
				getStatusFromTask(Task.TA_GETSTATUS_MODE_LOADMORE, oldestStatusId);
			}
		});
	}
	
	/*
	 * 加载任务。
	 * 参数标志着是不是第一次加载，下拉刷新，上拉加载更多
	 */
	private void getStatusFromTask(int mode, String statusId) {
		if (!isRefresh) {
			isRefresh = true;
			Map<String, Object> taskParams = new HashMap<String, Object>();
			taskParams.put(Task.TA_GETSTATUS_ACTIVITY, Task.TA_GETSTATUS_FRAGMENT);
			taskParams.put(Task.TA_GETSTATUS_TYPE, Task.TA_GETSTATUS_TYPE_ALL);
			taskParams.put(Task.TA_GETSTATUS_MODE, mode);
			taskParams.put(Status.STATUS_ID, statusId);
			Task task = new Task(Task.TA_GETSTATUS, taskParams);
			MainService.addTask(task);
		}
	}
	
	/*
	 * HomePage的本地回调
	 */
	public void localRefresh(int id, Map<String, Object> params) {
		switch (id) {
		case NormalString.LocalRefresh.UPDATA_PHOTO:
			String personId_p = (String) params.get(Person.PERSON_ID);
			String personPhotoUrl = (String) params.get(Person.PERSON_PHOTOURL);
			for (Status status : currentStatus) {
				if (personId_p.equals(status.getPersonId())) {
					status.setPersonPhotoUrl(personPhotoUrl);
				}
			}
			adapter.notifyDataSetChanged();
			break;
		case NormalString.LocalRefresh.UPDATA_NICKNAME:
			String personId_n = (String) params.get(Person.PERSON_ID);
			String personNickname = (String) params.get(Person.PERSON_NICKNAME);
			for (Status status : currentStatus) {
				if (personId_n.equals(status.getPersonId())) {
					status.setPersonNickname(personNickname);
				}
			}
			adapter.notifyDataSetChanged();
			break;
			
		case NormalString.LocalRefresh.RELEASE_STATUS_SUCCESS:
			Toast.makeText(context, "成功发布任务", Toast.LENGTH_SHORT).show();
			getStatusFromTask(Task.TA_GETSTATUS_MODE_FIRSTTIME, null);	
			break;

		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Object... params) {
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_GETSTATUS:
			int mode = (Integer) params[1];
			List<Status> newStatus = (List<Status>) params[2];
			refreshList(mode, newStatus);
			break;

		default:
			break;
		}
		/*把标志设为false，这样才能再开获取status的网络连接*/
		isRefresh = false;
	}
	
	/*
	 * 刷新列表
	 */
	private void refreshList(int mode, List<Status> newStatus) {
		if (newStatus != null && newStatus.size() > 0) {
			switch (mode) {
			case Task.TA_GETSTATUS_MODE_FIRSTTIME:
				currentStatus = newStatus;
				adapter = new TaskAdapter(context, currentStatus);
				xlist_task.setAdapter(adapter);
				lastestUpdata = TimeUtil.setLastestUpdata();
				break;
				
			case Task.TA_GETSTATUS_MODE_LOADMORE:
				currentStatus.addAll(newStatus);
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
			
			changeListData();
		} else {
			xlist_task.stopRefresh();
		}
	}
	
	/*
	 * 改变list相关的保存的数据
	 */
	private void changeListData() {
		xlist_task.stopRefresh();
		xlist_task.stopLoadMore();
		xlist_task.setRefreshTime(lastestUpdata);
		if (currentStatus.size() > 0) {
			oldestStatusId = currentStatus.get(currentStatus.size() - 1).getStatusId();
			xlist_task.setPullLoadEnable(true);
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

		default:
			break;
		}
	}

	/*
	 * 退出的时候保存一下最新任务ID和任务。
	 * 保存的方法里面只会保存一页（20条）的内容
	 */
	@Override
	public void exit() {
		if (statusService != null) {
			statusService.emptyStatusDb();
			statusService.insertListStatus(currentStatus);
			statusService.closeDBHelper();
		}
	}
	
}
