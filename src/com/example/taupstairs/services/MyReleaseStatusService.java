package com.example.taupstairs.services;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.db.DBHelper;
import com.example.taupstairs.db.DBInfo;

public class MyReleaseStatusService {

	private DBHelper dbHelper;
	
	public MyReleaseStatusService(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	/*
	 * 拿出来用于刚打开页面时显示
	 */
	public List<Status> getListStatus() {
		List<Status> listStatus = new ArrayList<Status>(Status.STATUS_COUNT_PERPAGE);
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select * from " + DBInfo.Table.MY_RELEASE_STATUS_TB_NAME, null);
		if (null == cursor || cursor.getCount() <= 0) {
			return null;
		}
		while (cursor.moveToNext()) {
			Status status = new Status();
			
			status.setStatusId(cursor.getString(cursor.getColumnIndex(Status.STATUS_ID)));
			status.setStatusTitle(cursor.getString(cursor.getColumnIndex(Status.STATUS_TITLE)));
			status.setStatusContent(cursor.getString(cursor.getColumnIndex(Status.STATUS_CONTENT)));
			status.setStatusReleaseTime(cursor.getString(cursor.getColumnIndex(Status.STATUS_RELEASETIME)));
			status.setStatusEndTime(cursor.getString(cursor.getColumnIndex(Status.STATUS_ENDTIME)));
			status.setStatusRewards(cursor.getString(cursor.getColumnIndex(Status.STATUS_REWARDS)));
			status.setStatusMessageCount(cursor.getString(cursor.getColumnIndex(Status.STATUS_MESSAGECOUNT)));
			status.setStatusSignUpCount(cursor.getString(cursor.getColumnIndex(Status.STATUS_SIGNUPCOUNT)));
			
			status.setPersonId(cursor.getString(cursor.getColumnIndex(Status.PERSON_ID)));
			status.setPersonPhotoUrl(cursor.getString(cursor.getColumnIndex(Status.PERSON_PHOTOURL)));
			status.setPersonNickname(cursor.getString(cursor.getColumnIndex(Status.PERSON_NICKNAME)));
			status.setPersonDepartment(cursor.getString(cursor.getColumnIndex(Status.PERSON_DEPARTMENT)));
			status.setPersonGrade(cursor.getString(cursor.getColumnIndex(Status.PERSON_GRADE)));
			status.setPersonSex(cursor.getString(cursor.getColumnIndex(Status.PERSON_SEX)));
			
			listStatus.add(status);
		}
		return listStatus;
	}
	
	/*
	 * 清空表
	 */
	public void emptyStatusDb() {
		dbHelper.getReadableDatabase().execSQL("delete from " + DBInfo.Table.MY_RELEASE_STATUS_TB_NAME);
	}
	
	/*
	 * 插入数据，只能插入一页的内容    i < Status.STATUS_COUNT_PERPAGE
	 */
	public void insertListStatus(List<Status> listStatus) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		for (int i = 0; i < listStatus.size() && i < Status.STATUS_COUNT_PERPAGE; i++) {
			Status status = listStatus.get(i);
			db.execSQL("insert into " + DBInfo.Table.MY_RELEASE_STATUS_TB_NAME + 
					" values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
					new String[] {status.getStatusId(), status.getStatusTitle(), 
					status.getStatusContent(), status.getStatusReleaseTime(), status.getStatusEndTime(), 
					status.getStatusRewards(), status.getStatusMessageCount(), status.getStatusSignUpCount(), 
					status.getPersonId(), status.getPersonPhotoUrl(), status.getPersonNickname(), 
					status.getPersonDepartment(), status.getPersonGrade(), status.getPersonSex()});
		}
	}
	
	/*关闭数据库缓存，一般在activity的onDestroy方法中调用*/
	public void closeDBHelper() {
		dbHelper.close();
	}
	
}
