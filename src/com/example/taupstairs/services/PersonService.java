package com.example.taupstairs.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.db.DBHelper;
import com.example.taupstairs.db.DBInfo;

public class PersonService {

	private DBHelper dbHelper;

	public PersonService(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	/*添加Person*/
	public void insertPerson(Person person) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.execSQL("insert into " + DBInfo.Table.PERSON_TA_NAME + 
				" values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
				new Object[] {person.getPersonId(), person.getPersonPhotoUrl(), person.getPersonNickname(), 
				person.getPersonSignature(), person.getPersonQq(), person.getPersonEmail(), 
				person.getPersonPhone(), person.getPersonPraise(), person.getPersonFaculty(), 
				person.getPersonYear(), person.getPersonSpecialty(), person.getPersonName(), 
				person.getPersonSex(), person.getPermission(), });
	}
	
	/*
	 * 更新单列
	 */
	public void updataPersonInfo(String personId, String column, String updata) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(column, updata);
		db.update(DBInfo.Table.PERSON_TA_NAME, values, Person.PERSON_ID + " = ? ", new String[] {personId, });
	}
	
	/*根据personId删除指定行*/
	public void deletePerson(String personId) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.execSQL("delete from " + DBInfo.Table.PERSON_TA_NAME + " where " + Person.PERSON_ID + " = ? ", 
				new String[] {personId, });
	}
	
	public void emptyPersonDB() {
		dbHelper.getReadableDatabase().execSQL("delete from " + DBInfo.Table.PERSON_TA_NAME);
	}
	
	/*根据PersonId返回Person信息*/
	public Person getPersonById(String personId) {
		Person person = null;
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select * from " + DBInfo.Table.PERSON_TA_NAME + " where " + Person.PERSON_ID + " = ? ", 
				new String[] {personId, });
		if (null != cursor && cursor.getCount() > 0) {
			cursor.moveToFirst();		//这一句一定要加，总之我不加就出错了
			String personPhotoUrl = cursor.getString(cursor.getColumnIndex(Person.PERSON_PHOTOURL));
			String personNickname = cursor.getString(cursor.getColumnIndex(Person.PERSON_NICKNAME));
			String personSignature = cursor.getString(cursor.getColumnIndex(Person.PERSON_SIGNATURE));
			String personPraise = cursor.getString(cursor.getColumnIndex(Person.PERSON_PRAISE));
			String personFaculty = cursor.getString(cursor.getColumnIndex(Person.PERSON_FACULTY));
			String personYear = cursor.getString(cursor.getColumnIndex(Person.PERSON_YEAR));
			String personSpecialty = cursor.getString(cursor.getColumnIndex(Person.PERSON_SPECIALTY));
			String personName = cursor.getString(cursor.getColumnIndex(Person.PERSON_NAME));
			String personSex = cursor.getString(cursor.getColumnIndex(Person.PERSON_SEX));
			String permission = cursor.getString(cursor.getColumnIndex(Person.PERMISSION));
			person = new Person(personId, personPhotoUrl, personNickname, personSignature, personPraise, 
					personFaculty, personYear, personSpecialty, personName, personSex, permission);
		}
		cursor.close();
		return person;
	}
	
	/*
	 * 返回qq，email，phone
	 */
	public Person getPersonOptional(String personId) {
		Person person = null;
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select * from " + DBInfo.Table.PERSON_TA_NAME + " where " + Person.PERSON_ID + " = ? ", 
				new String[] {personId, });
		if (null != cursor && cursor.getCount() > 0) {
			cursor.moveToFirst();		//这一句一定要加，总之我不加就出错了
			String personQq = cursor.getString(cursor.getColumnIndex(Person.PERSON_QQ));
			String personEmail = cursor.getString(cursor.getColumnIndex(Person.PERSON_EMAIL));
			String personPhone = cursor.getString(cursor.getColumnIndex(Person.PERSON_PHONE));
			person = new Person(personQq, personEmail, personPhone);
		}
		return person;
	}
	
	/*关闭数据库缓存，一般在activity的onDestroy方法中调用*/
	public void closeDBHelper() {
		dbHelper.close();
	}
	
}
