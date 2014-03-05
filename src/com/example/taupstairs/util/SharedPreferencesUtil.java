package com.example.taupstairs.util;

import com.example.taupstairs.bean.Info;
import com.example.taupstairs.bean.User;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
	
	private static final String DEFAULT_USER = "default_user";
	private static final String LASTEST_ID = "lastest_id";
	public static final int LASTEST_INFOID = 1;

	/*存储默认账户（如果先前存储过默认账户，则会覆盖）*/
	public static void saveDefaultUser(Context context, User user) {
		SharedPreferences sp = context.getSharedPreferences(DEFAULT_USER, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(User.USER_ID, user.getUserId());
		editor.putString(User.USER_COLLEGEID, user.getUserCollegeId());
		editor.putString(User.USER_STUDENTID, user.getUserStudentId());
		editor.putString(User.USER_PASSWORD, user.getUserPassword());
		editor.commit();
	}
	
	/*从存储器里面找出默认账户（用于登录）*/
	public static User getDefaultUser(Context context) {
		SharedPreferences sp = context.getSharedPreferences(DEFAULT_USER, Context.MODE_PRIVATE);
		String userId = sp.getString(User.USER_ID, null);
		if (null == userId) {
			return null;		//如果先前没有存储默认账户的话，就返回null
		}
		String userCollegeId = sp.getString(User.USER_COLLEGEID, null);
		String userStudentId = sp.getString(User.USER_STUDENTID, null);
		String userPassword = sp.getString(User.USER_PASSWORD, null);
		return new User(userId, userCollegeId, userStudentId, userPassword);
	}
	
	/*
	 * 储存最新ID
	 */
	public static void savaLastestId(Context context, int mode, String lastestId) {
		SharedPreferences sp = context.getSharedPreferences(LASTEST_ID, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		switch (mode) {
		case LASTEST_INFOID:
			editor.putString(Info.INFO_ID, lastestId);
			break;

		default:
			break;
		}
		
		editor.commit();
	}
	
	/*
	 * 读取最新ID
	 */
	public static String getLastestId(Context context, int mode) {
		SharedPreferences sp = context.getSharedPreferences(LASTEST_ID, Context.MODE_PRIVATE);
		String id = null;
		switch (mode) {
		case LASTEST_INFOID:
			id = sp.getString(Info.INFO_ID, null);
			break;
			
		default:
			break;
		}
		return id;
	}
	
	public static void emptyLastestId(Context context) {
		SharedPreferences sp = context.getSharedPreferences(LASTEST_ID, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(Info.INFO_ID, null);
		editor.commit();
	}
	
}
