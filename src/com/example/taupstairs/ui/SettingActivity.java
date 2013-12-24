package com.example.taupstairs.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.User;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class SettingActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_logout;
	private ListView list;
	private String[] setting = {"关于我们", "服务声名", "检查更新", "用户反馈"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		init();
	}

	@Override
	public void init() {
		btn_back = (Button)findViewById(R.id.btn_back_setting);
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		list = (ListView)findViewById(R.id.list_setting);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingActivity.this, 
				R.layout.common_txt_item, setting);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case 0:
					Intent intent1 = new Intent(SettingActivity.this, AboutUsActivity.class);
					startActivity(intent1);
					break;

				case 1:
					Intent intent2 = new Intent(SettingActivity.this, ServerDeclareActivity.class);
					startActivity(intent2);
					break;
					
				case 2:
					break;
					
				case 3:
					break;
					
				default:
					break;
				}
			}
		});
		btn_logout = (Button)findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
//				startActivity(intent);
//				finish();
				SharedPreferencesUtil.saveDefaultUser(SettingActivity.this, new User());
				//此处也可以删除数据库中的defaultPerson
				System.exit(0);
			}
		});
	}

	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub
		
	}
}
