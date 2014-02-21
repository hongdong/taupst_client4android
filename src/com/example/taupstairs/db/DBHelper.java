package com.example.taupstairs.db;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.College;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private Context context;
	
	public DBHelper(Context context) {
		super(context, DBInfo.DB.DB_NAME, null, DBInfo.DB.DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		initCollegeDB(db);
		initPersonDB(db);
		initInfoDB(db);
		initStatusDB(db);
		initRankDB(db);
	}
	
	private void initCollegeDB(SQLiteDatabase db) {
		String[] collegeIds = context.getResources().getStringArray(R.array.college_id);
		String[] collegeNames = context.getResources().getStringArray(R.array.college_name);
		db.execSQL(DBInfo.Table.COLLEGE_CREATE_STRING);
		for (int i = 0; i < collegeIds.length; i++) {
			db.execSQL("insert into " + College.TB_NAME + " values(null, ?, ?)", 
					new String[] {collegeIds[i], collegeNames[i]});
		}
	}
	
	private void initPersonDB(SQLiteDatabase db) {
		db.execSQL(DBInfo.Table.PERSON_CREATE_STRING);
	}
	
	private void initInfoDB(SQLiteDatabase db) {
		db.execSQL(DBInfo.Table.INFO_CREATE_STRING);
	}
	
	private void initStatusDB(SQLiteDatabase db) {
		db.execSQL(DBInfo.Table.STATUS_CREATE_STRING);
		db.execSQL(DBInfo.Table.MY_RELEASE_STATUS_CREATE_STRING);
		db.execSQL(DBInfo.Table.MY_SIGNUP_STATUS_CREATE_STRING);
	}
	
	private void initRankDB(SQLiteDatabase db) {
		db.execSQL(DBInfo.Table.RANK_CREATE_STRING);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
