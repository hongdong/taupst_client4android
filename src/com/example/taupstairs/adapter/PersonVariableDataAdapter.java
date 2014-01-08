package com.example.taupstairs.adapter;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.util.HttpClientUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonVariableDataAdapter extends BaseAdapter {

	private Context context;
	private String photoUrl, nickname, signatrue, sex;
	private static final int COUNT = 3;

	public PersonVariableDataAdapter(Context context, Person person) {
		super();
		this.context = context;
		this.photoUrl = person.getPersonPhotoUrl();
		this.nickname = person.getPersonNickname();
		this.signatrue = person.getPersonSignature();
		this.sex = person.getPersonSex();
	}

	@Override
	public int getCount() {
		return COUNT;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
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
			/*头像和昵称可能是空的。空的时候还要分男女，用上默认的*/
			if (photoUrl != null) {
				SimpleImageLoader.showImage(imageView, HttpClientUtil.PHOTO_BASE_URL + photoUrl);
			} else {
				if (sex.equals(Person.MALE)) {
					imageView.setImageResource(R.drawable.default_drawable);
				} else if (sex.equals(Person.FEMALE)) {
					imageView.setImageResource(R.drawable.default_drawable);
				}
			}
			break;
		
		case 1:
			view = LayoutInflater.from(context).inflate(R.layout.fm_me_variable_nickname, null);
			TextView nicknameTextView = (TextView)view.findViewById(R.id.txt_variable_nickname);
			if (nickname != null) {
				nicknameTextView.setText(nickname);
			} else {
				if (sex.equals(Person.MALE)) {
					nicknameTextView.setText(Person.MALE_NICKNAME);
				} else if (sex.equals(Person.FEMALE)) {
					nicknameTextView.setText(Person.FEMALE_NICKNAME);
				}
			}
			break;
		
		case 2:
			view = LayoutInflater.from(context).inflate(R.layout.fm_me_variable_signatrue, null);
			TextView signatrueTextView = (TextView)view.findViewById(R.id.txt_variable_signatrue);		
			if (signatrue != null) {
				signatrueTextView.setText(signatrue);
			} else {
				if (sex.equals(Person.MALE)) {
					signatrueTextView.setText(Person.MALE_NICKNAME);
				} else if (sex.equals(Person.FEMALE)) {
					signatrueTextView.setText(Person.FEMALE_NICKNAME);
				}
			}
			break;

		default:
			break;
		}
		return view;
	}

}
