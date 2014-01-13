package com.example.taupstairs.db;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.College;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private String[] collegeIds;
	private String[] collegeNames;
	
	public DBHelper(Context context) {
		super(context, DBInfo.DB.DB_NAME, null, DBInfo.DB.DB_VERSION);
		collegeIds = context.getResources().getStringArray(R.array.college_id);
		collegeNames = context.getResources().getStringArray(R.array.college_name);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		initCollegeDB(db);
		initPersonDB(db);
		initStatusDB(db);
		initRankDB(db);
	}
	
	private void initCollegeDB(SQLiteDatabase db) {
		db.execSQL(DBInfo.Table.COLLEGE_CREATE_STRING);
		for (int i = 0; i < collegeIds.length; i++) {
			db.execSQL("insert into " + College.TB_NAME + " values(null, ?, ?)", 
					new String[] {collegeIds[i], collegeNames[i]});
		}
	}
	
	private void initPersonDB(SQLiteDatabase db) {
		db.execSQL(DBInfo.Table.PERSON_CREATE_STRING);
	}
	
	private void initStatusDB(SQLiteDatabase db) {
		db.execSQL(DBInfo.Table.STATUS_CREATE_STRING);
	}
	
	private void initRankDB(SQLiteDatabase db) {
		db.execSQL(DBInfo.Table.RANK_CREATE_STRING);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
