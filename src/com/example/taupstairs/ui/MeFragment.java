package com.example.taupstairs.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.taupstairs.R;
import com.example.taupstairs.adapter.PersonVariableDataAdapter;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.PersonService;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class MeFragment extends Fragment implements ItaFragment {

	private Context context;
	private String defaultPersonId;
	private Person defaultPerson;
	private PersonService personService;
	private boolean flag_get_user_data;
	private View view;
	private ListView list_variable, list_base;
	private TextView txt_setting, txt_get_user_data;
	private static final String LIST_LEFT = "left";
	private static final String LIST_RIGHT = "right";
	
	/*若Fragement定义有带参构造函数，则一定要定义public的默认的构造函数，
	 * 否则有可能报出android.app.Fragment$InstantiationException的运行时异常*/
	public MeFragment() {
		super();
	}

	/*传入上下文的构造方法
	 * 因为如果用getActivity()的话，有时候会为null，就会出现空指针异常*/
	public MeFragment(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainService.addFragment(MeFragment.this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_me, container, false);
		init();
		return view;
	}

	@Override
	public void init() {
		initData();
		initView();
	}
	
	/*初始化一些全局变量*/
	private void initData() {
		defaultPersonId = SharedPreferencesUtil.getDefaultUser(context).getUserId();
		personService = new PersonService(context);
		defaultPerson = personService.getPersonById(defaultPersonId);
		flag_get_user_data = false;			//为false表示当前不在更新中
	}
	
	/*初始化View组件，list列表显示，设置监听器*/
	private void initView() {
		list_variable = (ListView)view.findViewById(R.id.list_fm_me_variable);
		list_base = (ListView)view.findViewById(R.id.list_fm_me_base);
		if (defaultPerson != null) {		//在initData里面已经从数据库中读数据了
			displayPerson(defaultPerson);	//如果数据库中有数据，就直接显示出来
		} else {
			getUserData();					//没有的话，就从服务器获取
		}
		list_variable.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {				//arg2为组件的位置，这个是系统定的，从0开始
				case 0: 
					break;					
				case 1:
					break;			
				case 2:
					break;
				default:
					break;
				}
			}
		});
		txt_setting = (TextView)view.findViewById(R.id.txt_fm_me_setting);
		txt_setting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {		
				Intent intent = new Intent(context, SettingActivity.class);
				startActivity(intent);	
			}
		});
		txt_get_user_data = (TextView)view.findViewById(R.id.txt_fm_me_getuserdata);
		txt_get_user_data.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	//到时候应该会弄成下拉刷新
				getUserData();
			}
		});
	}
	
	/*从服务器获取Person信息*/
	private void getUserData() {
		if (!flag_get_user_data) {
			flag_get_user_data = true;		//为true表示当前正在更新，再按这个键不会再新建任务
			Map<String, Object> taskParams = new HashMap<String, Object>();
			taskParams.put(Task.TA_GETUSERDATA_TASKPARAMS, defaultPersonId);
			Task task = new Task(Task.TA_GETUSERDATA, taskParams);
			MainService.addTask(task);
		}
	}

	@Override
	public void refresh(Object... params) {
		defaultPerson = (Person) params[0];
		displayPerson(defaultPerson);
		System.out.println(defaultPerson.toString());
//		personService.insertPerson(defaultPerson);	//更新数据库中的默认Person
		flag_get_user_data = false;			//更新完之后设为false，表示当前不在更新，可以再次相应按键进行更新
	}
	
	/*显示Person资料*/
	private void displayPerson(Person defaultPerson) {
		PersonVariableDataAdapter variable_adapter = 
				new PersonVariableDataAdapter(context, defaultPerson.getPersonDrawable(), 
				defaultPerson.getPersonNickname(), defaultPerson.getPersonSignatrue());
		list_variable.setAdapter(variable_adapter);	//把三个可改变资料显示出来
		
		String[] baseLeft = new String[] {"院系:", "年级:", "专业:", "姓名:", "性别:", };
		String[] baseRight = new String[] {defaultPerson.getPersonFaculty(), defaultPerson.getPersonYear(),
				defaultPerson.getPersonSpecialty(), defaultPerson.getPersonName(), defaultPerson.getPersonSex()};
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < baseLeft.length; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put(LIST_LEFT, baseLeft[i]);
			item.put(LIST_RIGHT, baseRight[i]);
			list.add(item);
		}
		SimpleAdapter base_adapter = new SimpleAdapter(context, list, R.layout.fm_me_base, 
				new String[] {LIST_LEFT, LIST_RIGHT, }, new int[] {R.id.txt_base_left, R.id.txt_base_right});
		list_base.setAdapter(base_adapter);			//把五个基本资料显示出来
	}

}
