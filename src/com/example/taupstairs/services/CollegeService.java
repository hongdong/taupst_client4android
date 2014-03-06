package com.example.taupstairs.services;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.taupstairs.bean.College;
import com.example.taupstairs.db.DBHelper;
import com.example.taupstairs.db.DBInfo;

public class CollegeService {

	private DBHelper dbHelper;
	
	public CollegeService(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	/*添加学校*/
	public void insertCollege(College college) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.execSQL("insert into " + DBInfo.Table.COLLEGE_TB_NAME + " values(null, ?, ?)", 
				new String[] {college.getCollegeId(), college.getCollegeName()});
	}
	
	/*根据学校ID返回学校信息（还没去实现）*/
	public College getCollegeById(String collegeId) {
		College college = null;
		return college;
	}
	
	public List<String> getCollegeNames() {
		List<String> collegeNames = new ArrayList<String>();
		Cursor cursor = dbHelper.getReadableDatabase()
				.rawQuery("select " + College.COLLEGE_NAME + " from " + DBInfo.Table.COLLEGE_TB_NAME, null);
		if (null != cursor && cursor.getCount() > 0) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				String collegeName = cursor.getString(cursor.getColumnIndex(College.COLLEGE_NAME));
				collegeNames.add(collegeName);
			}
		}
		cursor.close();
		return collegeNames;
	}
	
	/*根据学校名返回学校信息*/
	public College getCollegeByName(String collegeName) {
		College college = null;
		Cursor cursor = dbHelper.getReadableDatabase()
				.rawQuery("select * from " + DBInfo.Table.COLLEGE_TB_NAME + " where " + College.COLLEGE_NAME + " = ? ", 
				new String[] {collegeName, });
//		Cursor cursor = dbHelper.getReadableDatabase().query(College.TB_NAME, 	//用这个函数也能找得出来
//				new String[] {College.ID, College.COLLEGE_ID, College.COLLEGE_NAME}, 	//不过还是用sql语句吧
//				College.COLLEGE_NAME + "=?", new String[] {collegeName}, null, null, null);
		if (null != cursor && cursor.getCount() > 0) {
			cursor.moveToFirst();		//这一句一定要加，总之我不加就出错了
			String collegeId = cursor.getString(cursor.getColumnIndex(College.COLLEGE_ID));
			college = new College(collegeId, collegeName);
		}
		cursor.close();
		return college;
	}
	
	/*根据关键字（学校名）在数据库中查找，返回Cursor对象*/
	public Cursor getCursorByKeyword(String keyword) {
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select * from " + DBInfo.Table.COLLEGE_TB_NAME + " where " + College.COLLEGE_NAME + " like ? ", 
				new String[] {"%" + keyword + "%", });
		return cursor;
	}
	
	/*关闭数据库缓存，一般在activity的onDestroy方法中调用*/
	public void closeDBHelper() {
		dbHelper.close();
	}
}
