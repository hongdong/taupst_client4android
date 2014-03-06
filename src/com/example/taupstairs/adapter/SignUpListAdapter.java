package com.example.taupstairs.adapter;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.SignUp;
import com.example.taupstairs.bean.Time;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.PersonDataListener;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.TimeUtil;

public class SignUpListAdapter extends BaseAdapter {

	private Context context;
	private List<SignUp> signUps;
	
	public SignUpListAdapter(Context context, List<SignUp> signUps) {
		this.context = context;
		this.signUps = signUps;
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
		SignUp signUp = signUps.get(position);
		
		Holder holder = new Holder();
		holder.img_photo = (ImageView) view.findViewById(R.id.img_signup_list);
		holder.txt_nickname = (TextView) view.findViewById(R.id.txt_signup_list_nickname);
		holder.txt_time = (TextView) view.findViewById(R.id.txt_signup_list_time);
		holder.txt_isexe = (TextView) view.findViewById(R.id.txt_signup_list_isexe);
		holder.layout = (RelativeLayout) view.findViewById(R.id.layout_signup_list);
		holder.txt_message = (TextView) view.findViewById(R.id.txt_signup_list_message);
		holder.txt_praise = (TextView) view.findViewById(R.id.txt_signup_list_praise);
		
		SimpleImageLoader.showImage(holder.img_photo, 
				HttpClientUtil.PHOTO_BASE_URL + signUp.getPersonPhotoUrl());
		PersonDataListener personDataListener = 
				new PersonDataListener(context, signUp.getPersonId(), Person.PERMISSION_HIDE);
		holder.img_photo.setOnClickListener(personDataListener);
		
		holder.txt_nickname.setText(signUp.getPersonNickname());
		
		Time now = TimeUtil.getNow(Calendar.getInstance());
		String displayTime = TimeUtil.getDisplayTime(now, signUp.getSignUpTime());
		holder.txt_time.setText(displayTime);
		
		if (signUp.getIsExe().trim().equals("0")) {
			holder.txt_isexe.setTextColor(Color.RED);
			holder.txt_isexe.setText("执行者");
		} else  {
			holder.txt_isexe.setText("打酱油");
		}
		
		if (signUp.getSignUpPraise().trim().equals("")) {	//赞为空说明还没有对些报名人进行过评价
			
		} else {
			holder.txt_message.setText(signUp.getSignUpMessage());
			holder.txt_praise.setText(signUp.getSignUpPraise() + "赞");
			holder.layout.setVisibility(View.VISIBLE);
		}
		
		return view;
	}
	
	private class Holder {
		private ImageView img_photo;
		private TextView txt_nickname;
		private TextView txt_time;
		private TextView txt_isexe;
		private RelativeLayout layout;
		private TextView txt_message;
		private TextView txt_praise;
	}

}
