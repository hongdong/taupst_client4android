package com.example.taupstairs.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Rank;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.listener.PersonDataListener;
import com.example.taupstairs.util.HttpClientUtil;

public class RankAdapter extends BaseAdapter {

	private Context context;
	private List<Rank> ranks;
	
	public RankAdapter(Context context, List<Rank> ranks) {
		super();
		this.context = context;
		this.ranks = ranks;
	}

	@Override
	public int getCount() {
		return ranks.size();
	}

	@Override
	public Object getItem(int position) {
		return ranks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.rank_item, null);	
		Rank rank = ranks.get(position);
		
		Holder holder = new Holder();
		holder.img_photo = (ImageView) view.findViewById(R.id.img_fm_rank_photo);
		holder.txt_nickname = (TextView) view.findViewById(R.id.txt_fm_rank_nickname);
		holder.txt_praise = (TextView) view.findViewById(R.id.txt_fm_rank_praise);
		holder.txt_rank = (TextView) view.findViewById(R.id.txt_fm_rank_rank);
		
		String personSex = rank.getPersonSex().trim();
		String url = rank.getPersonPhotoUrl();
		if (url != null && !url.equals("")) {
			SimpleImageLoader.showImage(holder.img_photo, HttpClientUtil.PHOTO_BASE_URL + url);
		} else {
			if (personSex.equals(Person.MALE)) {
				holder.img_photo.setImageResource(R.drawable.default_drawable_male);
			} else if (personSex.equals(Person.FEMALE)) {
				holder.img_photo.setImageResource(R.drawable.default_drawable_female);
			}
		}
		holder.txt_nickname.setText(rank.getPersonNickname());
		holder.txt_praise.setText(rank.getRankPraise());
		holder.txt_rank.setText("第" + rank.getRankRank() + "名");
		if (position == ranks.size() - 1) {
			TextView textView = (TextView) view.findViewById(R.id.txt_fm_rank_me);
			textView.setVisibility(View.VISIBLE);
		}
		
		PersonDataListener personDataListener = new PersonDataListener(context, rank.getPersonId());
		holder.img_photo.setOnClickListener(personDataListener);
		
		return view;
	}
	
	/*头像，昵称，被赞次数，名次*/
	private class Holder {
		ImageView img_photo;
		TextView txt_nickname;
		TextView txt_praise;
		TextView txt_rank;
	}

}
