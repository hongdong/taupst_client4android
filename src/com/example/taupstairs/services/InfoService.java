package com.example.taupstairs.services;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.taupstairs.bean.Info;
import com.example.taupstairs.db.DBHelper;

public class InfoService {

	private DBHelper dbHelper;
	
	public InfoService(Context context) {
		this.dbHelper = new DBHelper(context);
	}
	
	public List<Info> getInfos() {
		List<Info> infos = new ArrayList<Info>(Info.INFO_COUNT_PERPAGE);
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from " + Info.TB_NAME, null);
		if (null == cursor || cursor.getCount() <= 0) {
			return null;
		}
		while (cursor.moveToNext()) {
			Info info = new Info();
			info.setInfoId(cursor.getString(cursor.getColumnIndex(Info.INFO_ID)));
			info.setPersonId(cursor.getString(cursor.getColumnIndex(Info.PERSON_ID)));
			info.setPersonPhotoUrl(cursor.getString(cursor.getColumnIndex(Info.PERSON_PHOTOURL)));
			info.setPersonNickname(cursor.getString(cursor.getColumnIndex(Info.PERSON_NICKNAME)));
			info.setInfoReleaseTime(cursor.getString(cursor.getColumnIndex(Info.INFO_RELEASETIME)));
			info.setInfoContent(cursor.getString(cursor.getColumnIndex(Info.INFO_CONTENT)));
			infos.add(info);
		}
		return infos;
	}
	
	public void insertInfos(List<Info> infos) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		for (int i = 0; i < infos.size() && i < Info.INFO_COUNT_PERPAGE; i++) {
			Info info = infos.get(i);
			db.execSQL("insert into " + Info.TB_NAME + 
					" values(null, ?, ?, ?, ?, ?, ?)", 
					new String[] {info.getInfoId(), info.getPersonId(), info.getPersonPhotoUrl(), 
					info.getPersonNickname(), info.getInfoReleaseTime(), info.getInfoContent()});
		}
	}
	
	public void emptyInfoDb() {
		dbHelper.getReadableDatabase().execSQL("delete from " + Info.TB_NAME);
	}
	
	public void closeDBHelper() {
		dbHelper.close();
	}
	
}
