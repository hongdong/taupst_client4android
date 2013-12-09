package com.example.taupstairs.db;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.College;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private String[] allCollegeNames;
	
	public DBHelper(Context context) {
		super(context, DBInfo.DB.DB_NAME, null, DBInfo.DB.DB_VERSION);
		allCollegeNames = context.getResources().getStringArray(R.array.college_name);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		initCollegeDB(db);
	}
	
	private void initCollegeDB(SQLiteDatabase db) {
		db.execSQL(DBInfo.Table.COLLEGE_CREATE_STRING);
		for (int i = 0; i < allCollegeNames.length; i++) {
			db.execSQL("insert into " + College.TB_NAME + " values(null, ?)", new String[] {allCollegeNames[i], });
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
