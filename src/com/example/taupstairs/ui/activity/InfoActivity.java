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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taupstairs.R;
import com.example.taupstairs.adapter.InfoAdapter;
import com.example.taupstairs.bean.Info;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.logic.TaUpstairsApplication;
import com.example.taupstairs.services.InfoService;
import com.example.taupstairs.string.NormalString;
import com.example.taupstairs.util.TimeUtil;
import com.example.taupstairs.view.XListView;
import com.example.taupstairs.view.XListView.IXListViewListener;

public class InfoActivity extends Activity implements ItaActivity {

	private XListView xlist_info;
	private InfoAdapter adapter;
	private List<Info> currentInfos;
	private int clickPosition;
	private InfoReceiver receiver;
	@SuppressWarnings("rawtypes")
	private Class[] infoDetail = {InfoMessageActivity.class, InfoExecTaskActivity.class, 
		InfoSignUpActivity.class, InfoEndTaskActivity.class, InfoPrivateLetterActivity.class};
	private InfoService infoService;
	private String lastestInfoId;
	private String oldestInfoId;
	private String lastestUpdata;
	private boolean isRefresh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hp_info);
		MainService.addActivity(this);
		init();
	}
	
	@Override
	public void init() {
		initData();
		initView();
		initReceiver();
	}
	
	private void initData() {
		isRefresh = false;
		infoService = new InfoService(this);
		currentInfos = infoService.getInfos();
	}
	
	private void initView() {
		xlist_info = (XListView) findViewById(R.id.xlist_hp_info);
		xlist_info.setPullLoadEnable(false);
		if (currentInfos != null) {
			adapter = new InfoAdapter(this, currentInfos);
			xlist_info.setAdapter(adapter);
			lastestInfoId = currentInfos.get(0).getInfoId();
			oldestInfoId = currentInfos.get(currentInfos.size() - 1).getInfoId();
			doGetInfoTask(Task.TA_GETINFO_MODE_PULLREFRESH, lastestInfoId);
		} else {
			doGetInfoTask(Task.TA_GETINFO_MODE_FIRSTTIME, null);
		}
		xlist_info.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (currentInfos.size() >= arg2) {
					clickPosition = arg2 - 1;
					int type = Integer.parseInt(currentInfos.get(clickPosition).getInfoType());
					if (type > 0 && type <= infoDetail.length) {
						TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
						app.setInfo(currentInfos.get(clickPosition));
						Intent intent = new Intent(InfoActivity.this, infoDetail[type - 1]);
						startActivity(intent);
					}
				}
			}
		});
		xlist_info.setXListViewListener(new IXListViewListener() {
			public void onRefresh() {
				if (null == lastestInfoId) {
					doGetInfoTask(Task.TA_GETINFO_MODE_FIRSTTIME, null);
				} else {
					doGetInfoTask(Task.TA_GETINFO_MODE_PULLREFRESH, lastestInfoId);
				}
			}
			public void onLoadMore() {
				doGetInfoTask(Task.TA_GETINFO_MODE_LOADMORE, oldestInfoId);
			}
		});
	}
	
	private void initReceiver() {
		receiver = new InfoReceiver();
		IntentFilter filter = new IntentFilter();  
        filter.addAction(NormalString.Receiver.NEW_INFO);
        registerReceiver(receiver, filter);  
	}
	
	private void doGetInfoTask(int mode, String infoId) {
		if (!isRefresh) {
			isRefresh = true;
			Map<String, Object> taskParams = new HashMap<String, Object>();
			taskParams.put(Task.TA_GETINFO_MODE, mode);
			taskParams.put(Task.TA_GETINFO_INFOID, infoId);
			Task task = new Task(Task.TA_GETINFO, taskParams);
			MainService.addTask(task);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Object... params) {
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_GETINFO:
			int mode = (Integer) params[1];
			List<Info> newInfos = (List<Info>) params[2];
			refreshInfo(mode, newInfos);
			break;

		default:
			break;
		}
		isRefresh = false;
	}
	
	private void refreshInfo(int mode, List<Info> newInfos) {
		if (newInfos != null) {
			switch (mode) {
			case Task.TA_GETINFO_MODE_FIRSTTIME:
				LinearLayout layout = (LinearLayout) findViewById(R.id.layout_no_info);
				if (newInfos.size() <= 0) {	
					TextView txt_no_info = (TextView) findViewById(R.id.txt_no_info);
					txt_no_info.setText("还没有消息");
					layout.setVisibility(View.VISIBLE);	//这个时候要显示没有消息
				} else {
					layout.setVisibility(View.GONE);	
				}
				currentInfos = newInfos;
				adapter = new InfoAdapter(this, currentInfos);
				xlist_info.setAdapter(adapter);	
				lastestUpdata = TimeUtil.setLastestUpdata();
				break;
				
			case Task.TA_GETINFO_MODE_PULLREFRESH:
				if (newInfos.size() < 20) {
					currentInfos.addAll(0, newInfos);
					adapter.notifyDataSetChanged();
				} else {
					currentInfos = newInfos;
					adapter.notifyDataSetInvalidated();
				}
				lastestUpdata = TimeUtil.setLastestUpdata();
				break;
				
			case Task.TA_GETINFO_MODE_LOADMORE:
				currentInfos.addAll(newInfos);
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
			
			changeListData();
		} else {
			xlist_info.stopRefresh();
		}
	}
	
	private void changeListData() {
		xlist_info.stopRefresh();
		xlist_info.stopLoadMore();
		xlist_info.setRefreshTime(lastestUpdata);
		if (currentInfos.size() > 0) {
			lastestInfoId = currentInfos.get(0).getInfoId();
			if (currentInfos.size() >= 20) {
				oldestInfoId = currentInfos.get(currentInfos.size() - 1).getInfoId();
				xlist_info.setPullLoadEnable(true);
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		getParent().onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (infoService != null && currentInfos != null) {
			infoService.emptyInfoDb();
			infoService.insertInfos(currentInfos);
			infoService.closeDBHelper();
		}
	}
	
	public class InfoReceiver extends BroadcastReceiver {
		public void onReceive(final Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(NormalString.Receiver.NEW_INFO)) {
				if (null == lastestInfoId) {
					doGetInfoTask(Task.TA_GETINFO_MODE_FIRSTTIME, null);
				} else {
					doGetInfoTask(Task.TA_GETINFO_MODE_PULLREFRESH, lastestInfoId);
				}
			} 
		}
	}

}
