package com.example.taupstairs.adapter;

import com.example.taupstairs.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonVariableDataAdapter extends BaseAdapter {

	private Context context;
	private Drawable drawable;
	private String nickname;
	private String signatrue;
	private static final int COUNT = 3;

	public PersonVariableDataAdapter(Context context, Drawable drawable,
			String nickname, String signatrue) {
		super();
		this.context = context;
		this.drawable = drawable;
		this.nickname = nickname;
		this.signatrue = signatrue;
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
			imageView.setImageDrawable(drawable);
			break;
		
		case 1:
			view = LayoutInflater.from(context).inflate(R.layout.fm_me_variable_nickname, null);
			TextView nicknameTextView = (TextView)view.findViewById(R.id.txt_variable_nickname);
			nicknameTextView.setText(nickname);
			break;
		
		case 2:
			view = LayoutInflater.from(context).inflate(R.layout.fm_me_variable_signatrue, null);
			TextView signatrueTextView = (TextView)view.findViewById(R.id.txt_variable_signatrue);
			signatrueTextView.setText(signatrue);
			break;

		default:
			break;
		}
		return view;
	}

}
