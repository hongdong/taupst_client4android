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

public class ServerDeclareActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private TextView txt_server_declare;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_declare);
		btn_back = (Button)findViewById(R.id.btn_back_serverdeclare);
		txt_server_declare = (TextView) findViewById(R.id.txt_server_declare);
		
		StringBuilder server_declare = new StringBuilder();
		server_declare.append("<html><body>");
		
		server_declare.append("<p><strong>1. 软件介绍</strong></p>");
		server_declare.append("<p>1.1</p>");
		server_declare.append("<p>“Ta楼上”使用学生的教务系统帐号和密码进行登录，登录后可享受丰富的资源。");
		server_declare.append("可进行任务的发布，报名任务，留言，以及其他一些相关设置。</p>");
		server_declare.append("<p>1.2</p>");
		server_declare.append("<p>“Ta楼上”采用的实名制方法，至今未有。通过用户输入的教务系统帐号与密码，");
		server_declare.append("进行用户基本资料的同步，并以非常安全的方式保存在我们的服务器上。");
		server_declare.append("在这里，你所查看到的对方基本资料百分百真实，");
		server_declare.append("且目前能接收到的任务都是本校学生所发布，信任度极高。</p>");
		server_declare.append("<p>1.3</p>");
		server_declare.append("没人帮你打包？女生宿舍楼下表白没人助阵？来不及上课没人替你去点个名？");
		server_declare.append("把你的困难说出来，也可以发挥自己的想象力，把你的需求说出来，让Ta来帮你解决吧！！！</p>");
		
		server_declare.append("<p><strong>2. 用户保障</strong></p>");
		server_declare.append("<p>2.1</p>");
		server_declare.append("<p>“Ta楼上”对用户资料均采用较先进的加密技术进行处理，");
		server_declare.append("之后再进行传输。保证不泄漏用户资料，隐私。</p>");
		
		server_declare.append("<p><strong>3. 其它声明</strong></p>");
		server_declare.append("<p>3.1</p>");
		server_declare.append("<p>凡是使用“Ta楼上”进行一些违法违规操作的，一切责任由用户自己承担。");
		server_declare.append("<p>3.2</p>");
		server_declare.append("<p>“Ta楼上”中所使用的任何资源，包括软件创意，");
		server_declare.append("未经相关权利人同意，不得盗用，否则必当追究法律责任。</p>");
		
		server_declare.append("</body></html>");
		
		
		txt_server_declare.setMovementMethod(ScrollingMovementMethod.getInstance());
		txt_server_declare.setText(Html.fromHtml(server_declare.toString()));
		
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... params) {

	}

}
