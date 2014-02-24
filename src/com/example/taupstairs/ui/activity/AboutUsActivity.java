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

public class AboutUsActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private TextView txt_about_us;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		btn_back = (Button)findViewById(R.id.btn_back_aboutus);
		txt_about_us = (TextView)findViewById(R.id.txt_about);
		btn_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		StringBuilder about_us = new StringBuilder();
		about_us.append("<html><body>");
		about_us.append("<p><strong>1. 团队介绍</strong></p>");
		about_us.append("<p>1.1</p>");
		about_us.append("<p>“Ta楼上”由Starry工作室制作。</p>");
		
		about_us.append("<p><strong>2. 联系我们</strong></p>");
		about_us.append("<p>2.1</p>");
		about_us.append("<p>用户使用过程中有任务想反馈的信息，都可通过设置页面的“用户反馈”项进行反馈，");
		about_us.append("欢迎提出你们宝贵的意见。</p>");
		about_us.append("</body></html>");
		txt_about_us.setMovementMethod(ScrollingMovementMethod.getInstance());
		txt_about_us.setText(Html.fromHtml(about_us.toString()));
	}
	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... params) {

	}

}
