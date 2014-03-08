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
		version_feature.append("<p>当前版本为本软件第一版本，版本名为1.1。</p>");
		
		version_feature.append("<p><strong>2. 版本功能</strong></p>");
		version_feature.append("<p>2.1</p>");
		version_feature.append("<p>用户可进行任务的发布，报名任务，留言，以及其他一些相关设置。</p>");
		version_feature.append("<p>2.2</p>");
		version_feature.append("<p>消息页面有4类消息，分别是：有人留言、有人报名、选你执行、评价了你。</p>");
		version_feature.append("<p>2.3</p>");
		version_feature.append("<p>用户可自由发布任务，有人报名后可选择任意个报名人执行你的任务。");
		version_feature.append("任务过期或完结任务后别人将不可报名你的任务。同时完结任务后可对执行者进行评价和给赞。");
		version_feature.append("对于别人发布的任务，用户在可报名的情况下只能报名一次。报名后流程同上。</p>");
		version_feature.append("<p>2.4</p>");
		version_feature.append("<p>用户可随意对任务进行留言，可回复任务发布人也可以是留言列表里的任何人。</p>");
		version_feature.append("<p>2.5</p>");
		version_feature.append("<p>排行页面只显示当前排名的1到20名。敢快报名任务得到更多的赞吧！！！</p>");
		
		version_feature.append("<p><strong>3. 关于点赞</strong></p>");
		version_feature.append("<p>3.1</p>");
		version_feature.append("<p>用户报名任务且任务发布者选你执行，当发布者完结此任务后将可以给你0到32个赞，");
		version_feature.append("而不是由用户在页面上直接点赞。</p>");
		version_feature.append("</body></html>");
		
		version_feature.append("<p><strong>4. 关于实名资料</strong></p>");
		version_feature.append("<p>4.1</p>");
		version_feature.append("<p>1.1版本将由用户设置是否对所有人公开实名资料，可以点击设置页面“实名资料”项进行设置。");
		version_feature.append("用户设置不公开实名资料后，只有在这三种情况下，实名资料才强制向对方公开，");
		version_feature.append("分别是用户报名任务后，或有人报名你的任务且你选他执行，或对你任务的执行者给赞，");
		version_feature.append("即在“有人报名”、“选我执行”、“评价了你”的消息详情页面点击头像可查看对方实名资料。</p>");
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
