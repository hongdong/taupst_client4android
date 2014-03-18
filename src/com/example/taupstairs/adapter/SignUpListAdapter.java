package com.example.taupstairs.adapter;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.SignUp;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.PersonDataListener;
import com.example.taupstairs.logic.TaUpstairsApplication;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.ui.activity.EvaluateActivity;
import com.example.taupstairs.ui.activity.SignUpListActivity;
import com.example.taupstairs.ui.activity.SignUpListInfoDetailActivity;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.TimeUtil;

public class SignUpListAdapter extends BaseAdapter {

	private SignUpListActivity context;
	private List<SignUp> signUps;
	private String statusId;
	
	public SignUpListAdapter(SignUpListActivity context, List<SignUp> signUps, String statusId) {
		this.context = context;
		this.signUps = signUps;
		this.statusId = statusId;
	}

	@Override
	public int getCount() {
		return signUps.size();
	}

	@Override
	public Object getItem(int arg0) {
		return signUps.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.signup_list_item, null);	
		final int clickPosition = position;
		final SignUp signUp = signUps.get(clickPosition);
		
		Holder holder = new Holder();
		holder.img_photo = (ImageView) view.findViewById(R.id.img_signup_list);
		holder.txt_nickname = (TextView) view.findViewById(R.id.txt_signup_list_nickname);
		holder.txt_time = (TextView) view.findViewById(R.id.txt_signup_list_time);
		holder.txt_isexe = (TextView) view.findViewById(R.id.txt_signup_list_isexe);
		holder.txt_detail = (TextView) view.findViewById(R.id.txt_signup_list_detail);
		holder.txt_evaluate = (TextView) view.findViewById(R.id.txt_signup_list_evaluate);
		holder.layout = (RelativeLayout) view.findViewById(R.id.layout_signup_list);
		holder.txt_message = (TextView) view.findViewById(R.id.txt_signup_list_message);
		holder.txt_praise = (TextView) view.findViewById(R.id.txt_signup_list_praise);
		
		SimpleImageLoader.showImage(holder.img_photo, 
				HttpClientUtil.PHOTO_BASE_URL + signUp.getPersonPhotoUrl());
		PersonDataListener personDataListener = 
				new PersonDataListener(context, signUp.getPersonId(), Person.PERMISSION_PUBLIC);
		holder.img_photo.setOnClickListener(personDataListener);
		holder.txt_nickname.setText(signUp.getPersonNickname());
		String displayTime = TimeUtil.getDisplayTime(TimeUtil.getNow(), signUp.getSignUpTime());
		holder.txt_time.setText(displayTime);
		
		final boolean flag_exe, flag_eva;
		if (signUp.getIsExe().trim().equals("0")) {
			flag_exe = true;
			holder.txt_isexe.setTextColor(Color.RED);
			holder.txt_isexe.setText("执行者");
		} else  {
			flag_exe = false;
			holder.txt_isexe.setText("打酱油");
		}
		
		if (signUp.getSignUpPraise().trim().equals("")) {	//赞为空说明还没有对些报名人进行过评价
			flag_eva = true;
			holder.txt_evaluate.setText("评价");	
		} else {
			flag_eva = false;
			holder.txt_evaluate.setText("已评价");
			holder.txt_message.setText(signUp.getSignUpMessage());
			holder.txt_praise.setText(signUp.getSignUpPraise() + "赞");
			holder.layout.setVisibility(View.VISIBLE);
		}
		
		holder.txt_detail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TaUpstairsApplication app = (TaUpstairsApplication) context.getApplication();
				app.setSignUp(signUp);
				Intent intent = new Intent(context, SignUpListInfoDetailActivity.class);
				intent.putExtra(SignUp.CLICK_POSITION, clickPosition);
				context.startActivityForResult(intent, IntentString.RequestCode.SIGNUPLIST_INFODETAIL);
			}
		});
		
		holder.txt_evaluate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (flag_exe && flag_eva) {
					TaUpstairsApplication app = (TaUpstairsApplication) context.getApplication();
					app.setSignUp(signUp);
					Intent intent = new Intent(context, EvaluateActivity.class);
					intent.putExtra(Status.STATUS_ID, statusId);
					intent.putExtra(SignUp.CLICK_POSITION, clickPosition);
					context.startActivityForResult(intent, IntentString.RequestCode.SIGNUPLIST_EVALUATE);
				} else {
					if (!flag_exe) {
						Toast.makeText(context, "只能对执行者进行评价", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		return view;
	}
	
	private class Holder {
		private ImageView img_photo;
		private TextView txt_nickname;
		private TextView txt_time;
		private TextView txt_isexe;
		private TextView txt_detail;
		private TextView txt_evaluate;
		private RelativeLayout layout;
		private TextView txt_message;
		private TextView txt_praise;
	}

}
