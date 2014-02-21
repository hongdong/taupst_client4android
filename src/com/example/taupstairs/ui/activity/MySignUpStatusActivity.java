package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import com.example.taupstairs.R;
import com.example.taupstairs.adapter.TaskAdapter;
import com.example.taupstairs.app.TaUpstairsApplication;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.MySignUpStatusService;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.util.SharedPreferencesUtil;
import com.example.taupstairs.util.TimeUtil;
import com.example.taupstairs.view.XListView;
import com.example.taupstairs.view.XListView.IXListViewListener;

public class MySignUpStatusActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private XListView xlist_task;
	private TaskAdapter adapter;
	private List<Status> currentStatus;
	private int clickPosition;
	
	/*是否正在加载任务*/
	private boolean isRefresh;
	
	private MySignUpStatusService statusService;
	private String lastestStatusId;
	private String oldestStatusId;
	private String lastestUpdata;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_signup_status);
		MainService.addActivity(this);
		init();
	}
	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		isRefresh = false;
		lastestStatusId = SharedPreferencesUtil.getLastestId(this, SharedPreferencesUtil.LASTEST_MY_SIGNUP_STATUS_ID);
		if (null == lastestStatusId) {
			getStatusFromTask(Task.TA_GETSTATUS_MODE_FIRSTTIME, null);		
		} else {
			getStatusFromTask(Task.TA_GETSTATUS_MODE_PULLREFRESH, lastestStatusId);	
		}
		statusService = new MySignUpStatusService(this);	
		currentStatus = statusService.getListStatus();
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_my_signup_status);
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		xlist_task = (XListView) findViewById(R.id.xlist_my_signup_status);	
		xlist_task.setPullLoadEnable(false);
		if (currentStatus != null) {
			adapter = new TaskAdapter(this, currentStatus);
			xlist_task.setAdapter(adapter);
		}
		xlist_task.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (currentStatus.size() >= arg2) {
					clickPosition = arg2 - 1;
					/*全局变量传递数据*/
					Intent intent = new Intent(MySignUpStatusActivity.this, TaskDetailActivity.class);
					TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
					app.setStatus(currentStatus.get(arg2 - 1));
					startActivityForResult(intent, IntentString.RequestCode.MYRELEASESTATUS_TASKDETAIL);
				}
			}
		});
		xlist_task.setXListViewListener(new IXListViewListener() {
			public void onRefresh() {
				if (null == lastestStatusId) {
					getStatusFromTask(Task.TA_GETSTATUS_MODE_FIRSTTIME, null);	
				} else {
					getStatusFromTask(Task.TA_GETSTATUS_MODE_PULLREFRESH, lastestStatusId);
				}
			}
			public void onLoadMore() {
				getStatusFromTask(Task.TA_GETSTATUS_MODE_LOADMORE, oldestStatusId);
			}
		});
	}
	
	/**
	 * 
	 * @param mode
	 * @param statusId
	 */
	private void getStatusFromTask(int mode, String statusId) {
		if (!isRefresh) {
			isRefresh = true;
			Map<String, Object> taskParams = new HashMap<String, Object>();
			taskParams.put(Task.TA_GETSTATUS_ACTIVITY, Task.TA_GETSTATUS_MYSIGNUPSTATUS);
			taskParams.put(Task.TA_GETSTATUS_TYPE, Task.TA_GETSTATUS_TYPE_MY_SIGNUP);
			taskParams.put(Person.PERSON_ID, SharedPreferencesUtil.getDefaultUser(this).getUserId());
			taskParams.put(Task.TA_GETSTATUS_MODE, mode);
			taskParams.put(Status.STATUS_ID, statusId);
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
			int mode = (Integer) params[1];
			List<Status> newStatus = (List<Status>) params[2];
			refreshList(mode, newStatus);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	 * @param mode
	 * @param newStatus
	 */
	private void refreshList(int mode, List<Status> newStatus) {
		if (newStatus != null) {
			switch (mode) {
			case Task.TA_GETSTATUS_MODE_FIRSTTIME:
				currentStatus = newStatus;
				/*第一次上面不会设置这个，所以这里要设置*/
				adapter = new TaskAdapter(this, currentStatus);
				xlist_task.setAdapter(adapter);
				lastestUpdata = TimeUtil.setLastestUpdata();
				break;
				
			case Task.TA_GETSTATUS_MODE_PULLREFRESH:
				if (newStatus.size() < 20) {
					if (currentStatus != null) {
						/*这里一定要放到最头部，那样显示才不会乱*/
						currentStatus.addAll(0, newStatus);
						adapter.notifyDataSetChanged();
					} else {	/*读取数据库可能失败。或许是上次没保存好*/
						currentStatus = newStatus;
						adapter = new TaskAdapter(this, currentStatus);
						xlist_task.setAdapter(adapter);
					}
				} else {
					currentStatus = newStatus;
					adapter.notifyDataSetInvalidated();
				}
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
		
		/*把标志设为false，这样才能再开获取status的网络连接*/
		isRefresh = false;
	}
	
	/*
	 * 改变list相关的保存的数据
	 */
	private void changeListData() {
		xlist_task.stopRefresh();
		xlist_task.stopLoadMore();
		xlist_task.setRefreshTime(lastestUpdata);
		if (currentStatus.size() > 0) {
			lastestStatusId = currentStatus.get(0).getStatusId();
			oldestStatusId = currentStatus.get(currentStatus.size() - 1).getStatusId();
			xlist_task.setPullLoadEnable(true);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentString.RequestCode.MYSIGNUPSTATUS_TASKDETAIL:
			if (IntentString.ResultCode.TASKDETAIL_MYSIGNUPSTATUS == resultCode) {
				TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
		if (statusService != null && lastestStatusId != null) {
			SharedPreferencesUtil.savaLastestId(this, 
					SharedPreferencesUtil.LASTEST_MY_SIGNUP_STATUS_ID, lastestStatusId);
			statusService.emptyStatusDb();
			statusService.insertListStatus(currentStatus);
			statusService.closeDBHelper();
		}
	}

}
