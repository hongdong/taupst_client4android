package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import com.example.taupstairs.R;
import com.example.taupstairs.adapter.TaskAdapter;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.logic.TaUpstairsApplication;
import com.example.taupstairs.services.StatusService;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.string.NormalString;
import com.example.taupstairs.util.TimeUtil;
import com.example.taupstairs.view.XListView;
import com.example.taupstairs.view.XListView.IXListViewListener;

public class TaskActivity extends Activity implements ItaActivity {

	private Button btn_write;
	private XListView xlist_task;
	private TaskAdapter adapter;
	private List<Status> currentStatus;
	private int clickPosition;
	private StatusService statusService;
	private String oldestStatusId;
	private String lastestUpdata;
	private TaskReceiver receiver;
	
	/*是否正在加载任务*/
	private boolean isRefresh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hp_task);
		MainService.addActivity(this);
		init();
	}
	
	@Override
	public void init() {
		initData();
		initView();
		initReceiver();
	}
	
	public void initData() {
		isRefresh = false;		
		statusService = new StatusService(this);	
		currentStatus = statusService.getListStatus();
		getStatusFromTask(Task.TA_GETSTATUS_MODE_FIRSTTIME, null);
	}
	
	public void initView() {
		btn_write = (Button)findViewById(R.id.btn_write);
		btn_write.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TaskActivity.this, WriteActivity.class);
				startActivityForResult(intent, IntentString.RequestCode.TASKACTIVITY_WRITE);
			}
		});
		xlist_task = (XListView) findViewById(R.id.xlist_hp_task);	
		xlist_task.setPullLoadEnable(false);
		if (currentStatus != null) {
			oldestStatusId = currentStatus.get(currentStatus.size() - 1).getStatusId();
			adapter = new TaskAdapter(this, currentStatus);
			xlist_task.setAdapter(adapter);
		}
		xlist_task.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (currentStatus.size() >= arg2) {
					clickPosition = arg2 - 1;
					/*全局变量传递数据*/
					Intent intent = new Intent(TaskActivity.this, TaskDetailActivity.class);
					TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
					app.setStatus(currentStatus.get(arg2 - 1));
					startActivityForResult(intent, IntentString.RequestCode.TASKACTIVITY_TASKDETAIL);
				}
			}
		});
		xlist_task.setXListViewListener(new IXListViewListener() {
			public void onRefresh() {
				getStatusFromTask(Task.TA_GETSTATUS_MODE_FIRSTTIME, null);	
			}
			public void onLoadMore() {
				getStatusFromTask(Task.TA_GETSTATUS_MODE_LOADMORE, oldestStatusId);
			}
		});
	}
	
	private void initReceiver() {
		receiver = new TaskReceiver();
		IntentFilter filter = new IntentFilter();  
        filter.addAction(NormalString.Receiver.UPDATA_NICKNAME);
        filter.addAction(NormalString.Receiver.UPDATA_PHOTO);
        registerReceiver(receiver, filter);  
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
			taskParams.put(Task.TA_GETSTATUS_ACTIVITY, Task.TA_GETSTATUS_ACTIVITY_TASK);
			taskParams.put(Task.TA_GETSTATUS_TYPE, Task.TA_GETSTATUS_TYPE_ALL);
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
		/*把标志设为false，这样才能再开获取status的网络连接*/
		isRefresh = false;
	}
	
	private void refreshList(int mode, List<Status> newStatus) {
		if (newStatus != null && newStatus.size() > 0) {
			switch (mode) {
			case Task.TA_GETSTATUS_MODE_FIRSTTIME:
				currentStatus = newStatus;
				adapter = new TaskAdapter(this, currentStatus);
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
	
	private void changeListData() {
		xlist_task.stopRefresh();
		xlist_task.stopLoadMore();
		xlist_task.setRefreshTime(lastestUpdata);
		oldestStatusId = currentStatus.get(currentStatus.size() - 1).getStatusId();
		xlist_task.setPullLoadEnable(true);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentString.RequestCode.TASKACTIVITY_TASKDETAIL:
			if (IntentString.ResultCode.TASKDETAIL_TASKACTIVITY == resultCode) {
				TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
				Status status = app.getStatus();
				currentStatus.add(clickPosition, status);
				currentStatus.remove(clickPosition + 1);
				adapter.notifyDataSetChanged();
			}
			break;

		case IntentString.RequestCode.TASKACTIVITY_WRITE:
			if (IntentString.ResultCode.WRITE_TASKACTIVITY == resultCode) {
				Toast.makeText(this, "成功发布任务", Toast.LENGTH_SHORT).show();
				getStatusFromTask(Task.TA_GETSTATUS_MODE_FIRSTTIME, null);
			}
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		getParent().onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (statusService != null && currentStatus != null) {
			statusService.emptyStatusDb();
			statusService.insertListStatus(currentStatus);
			statusService.closeDBHelper();
		}
	}
	
	public class TaskReceiver extends BroadcastReceiver {
		public void onReceive(final Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(NormalString.Receiver.UPDATA_NICKNAME)) {
				String personId = intent.getStringExtra(Person.PERSON_ID);
				String personNickname = intent.getStringExtra(Person.PERSON_NICKNAME);
				for (Status status : currentStatus) {
					if (personId.equals(status.getPersonId())) {
						status.setPersonNickname(personNickname);
					}
				}
				adapter.notifyDataSetChanged();
			} else if (action.equals(NormalString.Receiver.UPDATA_PHOTO)) {
				String personId = intent.getStringExtra(Person.PERSON_ID);
				String personPhotoUrl = intent.getStringExtra(Person.PERSON_PHOTOURL);
				for (Status status : currentStatus) {
					if (personId.equals(status.getPersonId())) {
						status.setPersonPhotoUrl(personPhotoUrl);
					}
				}
				adapter.notifyDataSetChanged();
			}
		}
	}

}
