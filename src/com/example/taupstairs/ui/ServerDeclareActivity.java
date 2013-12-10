package com.example.taupstairs.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.example.taupstairs.R;

public class ServerDeclareActivity extends Activity implements ItaActivity {

	private Button btn_back;
	private TextView txt_server_declare;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_declare);
		btn_back = (Button)findViewById(R.id.btn_back_serverdeclare);
		txt_server_declare = (TextView) findViewById(R.id.txt_server_declare);
		
		StringBuilder server_declare = new StringBuilder();
		server_declare.append("<html><body>");
		
		server_declare.append("<p><strong>1. 服务内容</strong></p>");
		server_declare.append("<p>1.1</p>");
		server_declare.append("<p>Ta楼上服务的具体内容由星空公司根据实际情况提供，包括但不限于授权用户通过其帐号，");
		server_declare.append("使用Ta楼上服务发布观点、评论、图片、视频、转发链接等，星空公司有权对其提供的服务或产品形态");
		server_declare.append("进行升级或其他调整，并将及时更新页面/告知用户。</p>");
		server_declare.append("<p>1.2</p>");
		server_declare.append("<p>星空公司提供的部分网络服务为收费的网络服务，用户使用收费网络服务需要向星空公司支付一定的费用。对于收费的网络服务，");
		server_declare.append("星空公司会在用户使用之前给予用户明确的提示，只有用户根据提示确认其愿意支付相关费用，");
		server_declare.append("用户才能使用该等收费网络服务。如用户拒绝支付相关费用，则星空公司有权不向用户提供该等收费网络服务。</p>");
		server_declare.append("<p>1.3</p>");
		server_declare.append("用户理解，星空公司仅提供与Ta楼上服务相关的技术服务等，除此之外与相关网络服务有关的设备");
		server_declare.append("（如个人电脑、手机、及其他与接入互联网或移动网有关的装置）及所需的费用");
		server_declare.append("（如为接入互联网而支付的电话费及上网费、为使用移动网而支付的手机费）均应由用户自行负担。</p>");
		
		server_declare.append("<p><strong>2. 知识产权</strong></p>");
		server_declare.append("<p>2.1</p>");
		server_declare.append("<p>星空公司提供的网络服务中包含的任何文本、图片、图形、音频和/或视频资料均受著作权、商标和/或其它");
		server_declare.append("财产所有权法律的保护，未经相关权利人同意，上述资料均不得在任何媒体直接或间接发布、播放、出于");
		server_declare.append("播放或发布目的而改写或再发行，或者被用于其他任何商业目的。所有这些资料或资料的任何部分仅可作");
		server_declare.append("为私人和非商业用途而保存在某台计算机内。星空公司不就由上述资料产生或在传送或递交全部或部分上");
		server_declare.append("述资料过程中产生的延误、不准确、错误和遗漏或从中产生或由此产生的任何损害赔偿，以任何形式，向");
		server_declare.append("用户或任何第三方负责。</p>");
		server_declare.append("<p>2.2</p>");
		server_declare.append("<p>星空公司为提供Ta楼上服务而使用的，或者第三方针对微博服务开发的在Ta楼上平台上运行，供用户使");
		server_declare.append("用的任何软件（包括但不限于软件中所含的任何图象、照片、动 画、录像、录音、音乐、文字和附加程序、");
		server_declare.append("随附的帮助材料）的一切权利均属于该软件的著作权人，未经该软件的著作权人许可，用户不得对该软件");
		server_declare.append("进行反向工程 （reverse engineer）、反向编译（decompile）或反汇编（disassemble）。</p>");
		
		server_declare.append("<p><strong>隐私保护</strong></p>");
		server_declare.append("<p>3.1</p>");
		server_declare.append("<p>本协议所指的“隐私”包括《电信和互联网用户个人信息保护规定》第4条规定的用户个人信息的内容以及");
		server_declare.append("未来不时制定或修订的法律法规中明确规定的隐私应包括的内容。</p>");
		server_declare.append("<p>3.2</p>");
		server_declare.append("<p>保护用户隐私是星空公司的一项基本政策，星空公司保证不会将单个用户的注册资料及用户在使用星空服");
		server_declare.append("务时存储在星空公司的非公开内容用于任何非法的用途，且保证将单个用户的注册资料进行商业上的利用");
		server_declare.append("时应事先获得用户的同意，但下列情况除外：</p>");
		server_declare.append("<p>3.2.1 事先获得用户的明确授权；</p>");
		server_declare.append("<p>3.2.2 根据有关的法律法规要求；</p>");
		server_declare.append("<p>3.2.3 按照相关政府主管部门的要求；</p>");
		server_declare.append("<p>3.2.4 为维护社会公众的利益；</p>");
		server_declare.append("<p>3.2.5 用户侵害本协议项下星空公司的合法权益的情况下而为维护星空公司的合法权益所必须。</p>");
		server_declare.append("<p>3.3</p>");
		server_declare.append("<p>为提升Ta楼上服务的质量，星空公司可能会与第三方合作共同向用户提供相关的Ta楼上服务，此类合作");
		server_declare.append("可能需要包括但不限于Ta楼上用户数据与第三方用户数据的互通。在此情况下，用户知晓并同意如该第");
		server_declare.append("三方同意承担与星空公司同等的保护用户隐私的责任，则星空公司有权将用户的注册资料等提供给该第三");
		server_declare.append("方，并与第三方约定用户数据仅为双方合作的Ta楼上服务之目的使用；并且，星空公司将对该等第三方");
		server_declare.append("使用用户数据的行为进行监督和管理，尽一切合理努力保护用户个人信息的安全性。</p>");
		
		server_declare.append("<p><strong>4. 免责声明</strong></p>");
		server_declare.append("<p>4.1</p>");
		server_declare.append("<p>用户在使用Ta楼上服务的过程中应遵守国家法律法规及政策规定，因其使用Ta楼上服务而产生的行为后果由用户自行承担。</p>");
		server_declare.append("<p>4.2</p>");
		server_declare.append("<p>通过Ta楼上服务发布的任何信息，及通过Ta楼上服务传递的任何观点不代表星空公司之立场，星空公司");
		server_declare.append("亦不对其完整性、真实性、准确性或可靠性负责。用户对于可能会接触到的非法的、非道德的、错误的或");
		server_declare.append("存在其他失宜之处的信息，及被错误归类或是带有欺骗性的发布内容，应自行做出判断。在任何情况下，");
		server_declare.append("对于任何信息，包括但不仅限于其发生的任何错误或遗漏；或是由于使用通过星空发布、私信、传达、其");
		server_declare.append("他方式所释出的或在别处传播的信息，而造成的任何损失或伤害，应由相关行为主体承担全部责任。</p>");
		server_declare.append("<p>4.3</p>");
		server_declare.append("<p>鉴于外部链接指向的网页内容非星空公司实际控制的，因此星空公司无法保证为向用户提供便利而设置的外部链接的准确性和完整性。</p>");
		server_declare.append("<p>4.4</p>");
		server_declare.append("<p>对于因不可抗力或星空公司不能控制的原因造成的星空服务中断或其它缺陷，星空公司不承担任何责任，");
		server_declare.append("但将尽力减少因此而给用户造成的损失和影响。</p>");
		server_declare.append("<p>4.5</p>");
		server_declare.append("<p>用户同意，对于星空公司向用户提供的下列产品或者服务的质量缺陷本身及其引发的任何损失，星空公司无需承担任何责任：</p>");
		server_declare.append("<p>4.5.1 星空公司向用户免费提供的Ta楼上服务；</p>");
		server_declare.append("<p>4.5.2 星空公司向用户赠送的任何产品或者服务；</p>");
		server_declare.append("<p>4.5.3 星空公司向收费Ta楼上服务用户附赠的各种产品或者服务。</p>");
		server_declare.append("<p>4.6</p>");
		server_declare.append("<p>用户知悉并同意，星空公司可能会与第三方合作向用户提供产品（包括但不限于游戏、第三方应用等）并");
		server_declare.append("由第三方向用户提供该产品的升级、维护、客服等后续工作，由该等第三方对该产品的质量问题或其本身");
		server_declare.append("的原因导致的一切纠纷或用户损失承担责任，用户在此同意将向该第三方主张与此有关的一切权利和损失。</p>");
		server_declare.append("</body></html>");
		
		
		txt_server_declare.setMovementMethod(ScrollingMovementMethod.getInstance());
		txt_server_declare.setText(Html.fromHtml(server_declare.toString()));
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub

	}

}
