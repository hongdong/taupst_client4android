package com.example.taupstairs.services;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.taupstairs.bean.Info;
import com.example.taupstairs.db.DBHelper;
import com.example.taupstairs.db.DBInfo;

public class InfoService {

	private DBHelper dbHelper;
	
	public InfoService(Context context) {
		this.dbHelper = new DBHelper(context);
	}
	
	public List<Info> getInfos() {
		List<Info> infos = new ArrayList<Info>(Info.INFO_COUNT_PERPAGE);
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from " + DBInfo.Table.INFO_TB_NAME, null);
		if (null == cursor || cursor.getCount() <= 0) {
			return null;
		}
		while (cursor.moveToNext()) {
			Info info = new Info();
			info.setInfoId(cursor.getString(cursor.getColumnIndex(Info.INFO_ID)));
			info.setPersonId(cursor.getString(cursor.getColumnIndex(Info.PERSON_ID)));
			info.setPersonPhotoUrl(cursor.getString(cursor.getColumnIndex(Info.PERSON_PHOTOURL)));
			info.setPersonNickname(cursor.getString(cursor.getColumnIndex(Info.PERSON_NICKNAME)));
			info.setPersonSex(cursor.getString(cursor.getColumnIndex(Info.PERSON_SEX)));
			info.setPersonDepartment(cursor.getString(cursor.getColumnIndex(Info.PERSON_DEPARTMENT)));
			info.setPersonGrade(cursor.getString(cursor.getColumnIndex(Info.PERSON_GRADE)));
			info.setInfoReleaseTime(cursor.getString(cursor.getColumnIndex(Info.INFO_RELEASETIME)));
			info.setInfoContent(cursor.getString(cursor.getColumnIndex(Info.INFO_CONTENT)));
			info.setInfoSource(cursor.getString(cursor.getColumnIndex(Info.INFO_SOURCE)));
			info.setInfoType(cursor.getString(cursor.getColumnIndex(Info.INFO_TYPE)));
			infos.add(info);
		}
		cursor.close();
		return infos;
	}
	
	public void insertInfos(List<Info> infos) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		for (int i = 0; i < infos.size() && i < Info.INFO_COUNT_PERPAGE; i++) {
			Info info = infos.get(i);
			db.execSQL("insert into " + DBInfo.Table.INFO_TB_NAME + 
					" values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
					new String[] {info.getInfoId(), info.getPersonId(), info.getPersonPhotoUrl(), 
					info.getPersonNickname(), info.getPersonSex(), info.getPersonDepartment(), 
					info.getPersonGrade(), info.getInfoReleaseTime(), info.getInfoContent(), 
					info.getInfoSource(), info.getInfoType(), });
		}
	}
	
	public void emptyInfoDb() {
		dbHelper.getReadableDatabase().execSQL("delete from " + DBInfo.Table.INFO_TB_NAME);
	}
	
	public void closeDBHelper() {
		dbHelper.close();
	}
	
}
