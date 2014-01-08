package com.example.taupstairs.ui.activity;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class UpdataUserdataBaseActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_ok;
	private EditText edit_userdata;
	private String type, content, updata;
	private String personId;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updata_userdata_base);
		MainService.addActivity(this);
		init();
	}
	
	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		Intent intent = getIntent();
		type = intent.getStringExtra(IntentString.Extra.TYPE);
		content = intent.getStringExtra(IntentString.Extra.CONTENT);
		personId = SharedPreferencesUtil.getDefaultUser(this).getUserId();
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_updata_userdata_base);
		btn_ok = (Button)findViewById(R.id.btn_ok_updata_userdata_base);
		edit_userdata = (EditText)findViewById(R.id.edit_updata_userdata_base);
		
		edit_userdata.setText(content);
		if (type.equals(Person.PERSON_NICKNAME)) {
			edit_userdata.setSingleLine();
			edit_userdata.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
		} else if (type.equals(Person.PERSON_SIGNATURE)) {
			edit_userdata.setMaxLines(5);
			edit_userdata.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
		}
		
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updata = edit_userdata.getText().toString();
				if (updata.equals(content)) {
					finish();
				} else {			
					if (type.equals(Person.PERSON_NICKNAME)) {	
						String url = "users_id=" + personId + "&username=" + updata;
						doUpdataUserDataTask(url);				
					} else if (type.equals(Person.PERSON_SIGNATURE)) {
						String url = "users_id=" + personId + "&signature=" + updata;
						doUpdataUserDataTask(url);	
					}
				}
			}
		});	
	}
	
	/*
	 * 网络任务
	 */
	private void doUpdataUserDataTask(String url) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("    稍等片刻...");
		progressDialog.show();
		HashMap<String, Object> taskParams = new HashMap<String, Object>(2);
		taskParams.put(Task.TA_UPDATAUSERDATA_ACTIVITY, Task.TA_UPDATAUSERDATA_ACTIVITY_UPDATAUSERDATABASE);
		taskParams.put(Task.TA_UPDATAUSERDATA_URL, url);
		Task task = new Task(Task.TA_UPDATAUSERDATA, taskParams);
		MainService.addTask(task);
	}
	
	@Override
	public void refresh(Object... params) {
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_UPDATAUSERDATA:
			progressDialog.dismiss();
			String result = (String) params[1];
			if (null == result) {
				Toast.makeText(this, "未连接网络", Toast.LENGTH_SHORT).show();
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						if (type.equals(Person.PERSON_NICKNAME)) {	
							Intent intent = new Intent();
							intent.putExtra(Person.PERSON_NICKNAME, updata);
							setResult(IntentString.ResultCode.UPDATAUSERDATABASE_MEFRAGMENT_NICKNAME, intent);
							finish();
						} else if (type.equals(Person.PERSON_SIGNATURE)) {
							Intent intent = new Intent();
							intent.putExtra(Person.PERSON_SIGNATURE, updata);
							setResult(IntentString.ResultCode.UPDATAUSERDATABASE_MEFRAGMENT_SIGNATURE, intent);
							finish();
						}
					} else {
						Toast.makeText(this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
	}
	
}
