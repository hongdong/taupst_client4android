package com.example.taupstairs.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.PersonService;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class UpdataUserdataRealActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_ok;
	private String personId;
	private PersonService personService;
	private Person person;
	private RadioGroup radioGroup;
	private String permission;
	private ProgressDialog progressDialog;
	private static final String LIST_LEFT = "left";
	private static final String LIST_RIGHT = "right";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updata_userdata_real);
		MainService.addActivity(this);
		init();
	}
	
	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		personId = SharedPreferencesUtil.getDefaultUser(this).getUserId();
		personService = new PersonService(this);
		person = personService.getPersonById(personId);
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_updata_userdata_real);
		btn_ok = (Button)findViewById(R.id.btn_ok_updata_userdata_real_ok);
		radioGroup = (RadioGroup)findViewById(R.id.rg_updata_userdata_real);
		progressDialog = new ProgressDialog(this);
		
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (permission.equals(person.getPermission())) {
					Toast.makeText(UpdataUserdataRealActivity.this, "你并没有修改", Toast.LENGTH_SHORT).show();
				} else {
					String url = "hide=" + permission;
					doUpdataUserDataTask(url);
				}
			}
		});
		
		displayPersonData();
		
		permission = person.getPermission();
		if (permission.equals(Person.PERMISSION_PUBLIC)) {
			radioGroup.check(R.id.btn_real_data_public);
		} else if (permission.equals(Person.PERMISSION_HIDE)) {
			radioGroup.check(R.id.btn_real_data_hide);
		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.btn_real_data_public:
					permission = Person.PERMISSION_PUBLIC;
					break;
					
				case R.id.btn_real_data_hide:
					permission = Person.PERMISSION_HIDE;
					break;

				default:
					break;
				}
			}
		});
	}
	
	private void displayPersonData() {
		ListView list_base = (ListView) findViewById(R.id.list_updata_userdata_real);
		String[] baseLeft = new String[] {"院系:", "年级:", "专业:", "姓名:", "性别:", };
		String[] baseRight = new String[] {person.getPersonFaculty(), person.getPersonYear(),
				person.getPersonSpecialty(), person.getPersonName(), person.getPersonSex()};
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		for (int i = 0; i < baseLeft.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put(LIST_LEFT, baseLeft[i]);
			item.put(LIST_RIGHT, baseRight[i]);
			list.add(item);
		}
		SimpleAdapter base_adapter = new SimpleAdapter(this, list, R.layout.person_data_base, 
				new String[] {LIST_LEFT, LIST_RIGHT, }, new int[] {R.id.txt_base_left, R.id.txt_base_right});
		list_base.setAdapter(base_adapter);
	}
	
	private void showProgressDialog() {
		progressDialog.setCancelable(false);
		progressDialog.setMessage("    稍等片刻...");
		progressDialog.show();
	}
	
	private void dismissProgressDialog() {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	
	private void doUpdataUserDataTask(String url) {
		showProgressDialog();
		Map<String, Object> taskParams = new HashMap<String, Object>();
		taskParams.put(Task.TA_UPDATAUSERDATA_ACTIVITY, Task.TA_UPDATAUSERDATA_ACTIVITY_REAL);
		taskParams.put(Task.TA_UPDATAUSERDATA_URL, url);
		Task task = new Task(Task.TA_UPDATAUSERDATA, taskParams);
		MainService.addTask(task);
	}

	@Override
	public void refresh(Object... params) {
		dismissProgressDialog();
		if (params[1] != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_UPDATAUSERDATA:
				String result = (String) params[1];
				try {
					JSONObject jsonObject = new JSONObject(result);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
						personService.updataPersonInfo(personId, Person.PERMISSION, permission);
					} else {
						Toast.makeText(this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		} else {
			Toast.makeText(this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
		personService.closeDBHelper();
	}

}
