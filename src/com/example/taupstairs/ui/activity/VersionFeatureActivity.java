package com.example.taupstairs.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.taupstairs.R;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;

public class VersionFeatureActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private TextView txt_version_feature;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.version_feature);
		MainService.addActivity(this);
		init();
	}
	
	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		
	}
	
	private void initView() {
		btn_back = (Button)findViewById(R.id.btn_back_version_feature);
		txt_version_feature = (TextView)findViewById(R.id.txt_version_feature);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		StringBuilder version_feature = new StringBuilder();
		version_feature.append("<html><body>");
		version_feature.append("<p><strong>1. 当前版本</strong></p>");
		version_feature.append("<p>1.1</p>");
		version_feature.append("<p>当前版本为本软件第一版本，版本号为1.0</p>");
		
		version_feature.append("<p><strong>2. 版本功能</strong></p>");
		version_feature.append("<p>2.1</p>");
		version_feature.append("<p>用户可进行任务的发布，报名任务，留言，以及其他一些相关设置。</p>");
		version_feature.append("</body></html>");
		txt_version_feature.setMovementMethod(ScrollingMovementMethod.getInstance());
		txt_version_feature.setText(Html.fromHtml(version_feature.toString()));
	}

	@Override
	public void refresh(Object... params) {

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}

}
