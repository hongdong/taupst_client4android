package com.example.taupstairs.services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.taupstairs.bean.College;
import com.example.taupstairs.db.DBHelper;

public class CollegeService {

	private DBHelper dbHelper;
	
	public CollegeService(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	/*添加学校名*/
	public void insertCollege(College college) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.execSQL("insert into " + College.TB_NAME + " values(null, ?)", new String[] {college.getCollegeName(), });
	}
	
	/*根据学校名返回学校信息*/
	public College getCollegeByName(String collegeName) {
		College college = null;
		return college;
	}
	
	/*根据关键字在数据库中查找，返回Cursor对象*/
	public Cursor getCursorByKeyword(String keyword) {
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select * from " + College.TB_NAME + " where " + College.COLLEGE_NAME + " like ?", 
				new String[] {"%" + keyword + "%", });
		return cursor;
	}
	
	public void closeDBHelper() {
		dbHelper.close();
	}
}
