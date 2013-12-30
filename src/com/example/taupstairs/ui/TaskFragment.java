package com.example.taupstairs.ui;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.taupstairs.R;
import com.example.taupstairs.adapter.TaskAdapter;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.view.XListView;

public class TaskFragment extends Fragment implements ItaFragment {

	private Context context;
	private View view;
	private XListView xlist_task;
	private List<Status> listStatus;
	
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
		
	}
	
	/*
	 * 初始化ui，以及一些监听器
	 */
	private void initView() {
		xlist_task = (XListView) view.findViewById(R.id.xlist_fm_task);
		xlist_task.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				System.out.println("onItemClick");
				Intent intent = new Intent(context, TaskDetailActivity.class);
				startActivity(intent);
			}
		});
		
		getStatusTask();
		
		/*打开上拉加载更多的那个view*/
		xlist_task.setPullLoadEnable(true);
	}
	
	/*
	 * 在MainService里面开一个下载任务，下载Status（任务界面）
	 */
	private void getStatusTask() {
		Task task = new Task(Task.TA_GETSTATUS, null);
		MainService.addTask(task);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Object... params) {
		listStatus = (List<Status>) params[0];
		if (listStatus != null) {
			TaskAdapter adapter = new TaskAdapter(context, listStatus);
			xlist_task.setAdapter(adapter);
		} else {
//			System.out.println("应该是没网络");
		}
	}
}
