package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.example.taupstairs.R;
import com.example.taupstairs.adapter.RankAdapter;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Rank;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.RankService;
import com.example.taupstairs.string.NormalString;
import com.example.taupstairs.util.TimeUtil;
import com.example.taupstairs.view.XListView;
import com.example.taupstairs.view.XListView.IXListViewListener;

public class RankActivity extends Activity implements ItaActivity {

	private XListView xlist_rank;
	private RankAdapter adapter;
	private List<Rank> ranks;
	private boolean isRefresh = false;
	private boolean hasRefresh = false;
	private String lastestUpdata;
	private RankService rankService;
	private RankReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hp_rank);
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
		rankService = new RankService(this);
		ranks = rankService.getRanks();
	}
	
	private void initView() {
		xlist_rank = (XListView)findViewById(R.id.xlist_hp_rank);
		xlist_rank.setPullLoadEnable(false);
		if (ranks != null) {
			adapter = new RankAdapter(this, ranks);
			xlist_rank.setAdapter(adapter);
		} else {
			doGetRankTask();
		}
		xlist_rank.setXListViewListener(new IXListViewListener() {
			public void onRefresh() {
				doGetRankTask();
			}
			public void onLoadMore() {
				
			}
		});
	}
	
	private void initReceiver() {
		receiver = new RankReceiver();
		IntentFilter filter = new IntentFilter();  
        filter.addAction(NormalString.Receiver.UPDATA_NICKNAME);
        filter.addAction(NormalString.Receiver.UPDATA_PHOTO);
        registerReceiver(receiver, filter);  
	}
	
	private void doGetRankTask() {
		if (!isRefresh) {
			isRefresh = true;
			HashMap<String, Object> taskParams = new HashMap<String, Object>(1);
			taskParams.put(Task.TA_GETRANK_ACTIVITY, Task.TA_GETRANK_ACTIVITY);
			taskParams.put(Task.TA_GETRANK_MODE, Task.TA_GETRANK_MODE_OVERALL);
			Task task = new Task(Task.TA_GETRANK, taskParams);
			MainService.addTask(task);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Object... params) {
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_GETRANK:
			ranks = (List<Rank>) params[1];
			if (ranks != null && ranks.size() > 0) {
				adapter = new RankAdapter(this, ranks);
				xlist_rank.setAdapter(adapter);
				xlist_rank.stopRefresh();
				lastestUpdata = TimeUtil.setLastestUpdata();
				xlist_rank.setRefreshTime(lastestUpdata);
				hasRefresh = true;
			} else {
				xlist_rank.stopRefresh();
			}
			break;

		default:
			break;
		}
		isRefresh = false;
	}
	
	@Override
	public void onBackPressed() {
		getParent().onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (hasRefresh) {
			rankService.emptyRankDb();
			rankService.insertRanks(ranks);
			rankService.closeDBHelper();
		}
	}
	
	public class RankReceiver extends BroadcastReceiver {
		public void onReceive(final Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(NormalString.Receiver.UPDATA_NICKNAME)) {
				String personId = intent.getStringExtra(Person.PERSON_ID);
				String personNickname = intent.getStringExtra(Person.PERSON_NICKNAME);
				for (Rank rank : ranks) {
					if (personId.equals(rank.getPersonId())) {
						rank.setPersonNickname(personNickname);
					}
				}
				adapter.notifyDataSetChanged();
			} else if (action.equals(NormalString.Receiver.UPDATA_PHOTO)) {
				String personId = intent.getStringExtra(Person.PERSON_ID);
				String personPhotoUrl = intent.getStringExtra(Person.PERSON_PHOTOURL);
				for (Rank rank : ranks) {
					if (personId.equals(rank.getPersonId())) {
						rank.setPersonPhotoUrl(personPhotoUrl);
					}
				}
				adapter.notifyDataSetChanged();
			}
			hasRefresh = true;
		}
	}

}
