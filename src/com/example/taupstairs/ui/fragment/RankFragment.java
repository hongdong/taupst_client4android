package com.example.taupstairs.ui.fragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.taupstairs.R;
import com.example.taupstairs.adapter.RankAdapter;
import com.example.taupstairs.bean.Rank;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.Time;
import com.example.taupstairs.logic.ItaFragment;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.RankService;
import com.example.taupstairs.ui.activity.HomePageActivity;
import com.example.taupstairs.util.TimeUtil;
import com.example.taupstairs.view.XListView;
import com.example.taupstairs.view.XListView.IXListViewListener;

public class RankFragment extends Fragment implements ItaFragment {

	private View view;
	private HomePageActivity context;
	private XListView xlist_rank;
	private RankAdapter adapter;
	private List<Rank> ranks;
	private boolean isRefresh;
	private boolean hasRefresh;
	private String lastestUpdata;
	private Time lastestRefreshTime;
	private RankService rankService;
	
	public RankFragment() {
		super();
	}

	public RankFragment(HomePageActivity context) {
		super();
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainService.addFragment(RankFragment.this);
		initData();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_rank, container, false);
		initView();
		return view;
	}
	
	@Override
	public void initData() {
		isRefresh = false;
		hasRefresh = false;
		rankService = new RankService(context);
		ranks = rankService.getRanks();
	}

	@Override
	public void initView() {
		xlist_rank = (XListView)view.findViewById(R.id.xlist_fm_rank);
		xlist_rank.setPullLoadEnable(false);
		if (ranks != null) {
			adapter = new RankAdapter(context, ranks);
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
			if (ranks != null) {
				adapter = new RankAdapter(context, ranks);
				xlist_rank.setAdapter(adapter);
				xlist_rank.stopRefresh();
				setLastestUpdata();
				hasRefresh = true;
			}
			isRefresh = false;
			break;

		default:
			break;
		}
	}
	
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
		xlist_rank.setRefreshTime(lastestUpdata);
	}

	@Override
	public void exit() {
		if (hasRefresh) {
			rankService.emptyRankDb();
			rankService.insertRanks(ranks);
		}
		rankService.closeDBHelper();
	}
	
}
