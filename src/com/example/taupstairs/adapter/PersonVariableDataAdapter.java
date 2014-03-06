package com.example.taupstairs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.util.HttpClientUtil;

public class PersonVariableDataAdapter extends BaseAdapter {

	private Context context;
	private String photoUrl, nickname, signature;
	private static final int COUNT = 3;

	public PersonVariableDataAdapter(Context context, Person person) {
		super();
		this.context = context;
		this.photoUrl = person.getPersonPhotoUrl();
		this.nickname = person.getPersonNickname();
		this.signature = person.getPersonSignature();
	}

	@Override
	public int getCount() {
		return COUNT;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		switch (position) {
		case 0:
			view = LayoutInflater.from(context).inflate(R.layout.fm_me_variable_drawable, null);
			ImageView imageView = (ImageView)view.findViewById(R.id.img_fm_me_drawable);
			SimpleImageLoader.showImage(imageView, HttpClientUtil.PHOTO_BASE_URL + photoUrl);
			break;
		
		case 1:
			view = LayoutInflater.from(context).inflate(R.layout.fm_me_variable_nickname, null);
			TextView nicknameTextView = (TextView)view.findViewById(R.id.txt_variable_nickname);
			nicknameTextView.setText(nickname);
			break;
		
		case 2:
			view = LayoutInflater.from(context).inflate(R.layout.fm_me_variable_signatrue, null);
			TextView signatrueTextView = (TextView)view.findViewById(R.id.txt_variable_signatrue);		
			signatrueTextView.setText(signature);
			break;

		default:
			break;
		}
		return view;
	}

}
