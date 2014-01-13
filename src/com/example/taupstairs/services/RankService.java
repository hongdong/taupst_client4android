package com.example.taupstairs.services;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.taupstairs.bean.Rank;
import com.example.taupstairs.db.DBHelper;

public class RankService {

	private DBHelper dbHelper;

	public RankService(Context context) {
		this.dbHelper = new DBHelper(context);
	}
	
	public List<Rank> getRanks() {
		List<Rank> ranks = new ArrayList<Rank>(Rank.RANK_COUNT_PERPAGE);
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from " + Rank.TB_NAME, null);
		if (null == cursor || cursor.getCount() <= 0) {
			return null;
		}
		while (cursor.moveToNext()) {
			Rank rank = new Rank();
			rank.setPersonId(cursor.getString(cursor.getColumnIndex(Rank.PERSON_ID)));
			rank.setPersonPhotoUrl(cursor.getString(cursor.getColumnIndex(Rank.PERSON_PHOTOURL)));
			rank.setPersonSex(cursor.getString(cursor.getColumnIndex(Rank.PERSON_SEX)));
			rank.setPersonNickname(cursor.getString(cursor.getColumnIndex(Rank.PERSON_NICKNAME)));
			rank.setRankPraise(cursor.getString(cursor.getColumnIndex(Rank.RANK_PRAISE)));
			rank.setRankRank(cursor.getString(cursor.getColumnIndex(Rank.RANK_RANK)));
			ranks.add(rank);
		}
		return ranks;
	}
	
	public void insertRanks(List<Rank> ranks) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		for (int i = 0; i < ranks.size() && i < Rank.RANK_COUNT_PERPAGE; i++) {
			Rank rank = ranks.get(i);
			db.execSQL("insert into " + Rank.TB_NAME + 
					" values(null, null, ?, ?, ?, ?, ?, ?)", 
					new String[] {rank.getPersonId(), rank.getPersonPhotoUrl(), rank.getPersonNickname(),
					rank.getPersonSex(), rank.getRankPraise(), rank.getRankRank()});
		}
	}
	
	/*
	 * 清空表
	 */
	public void emptyRankDb() {
		dbHelper.getReadableDatabase().execSQL("delete from " + Rank.TB_NAME);
	}
	
	/*关闭数据库缓存，一般在activity的onDestroy方法中调用*/
	public void closeDBHelper() {
		dbHelper.close();
	}
	
}
