package com.example.taupstairs.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.db.DBHelper;

public class PersonService {

	private DBHelper dbHelper;

	public PersonService(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	/*添加Person*/
	public void insertPerson(Person person) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		/*在这里用户头像添加比较麻烦，要把Drawable先转为Bitmap，
		 * 再把Bitmap输出到一个ByteArrayOutputStream流里面，
		 * 最后呢，ByteArrayOutputStream还要用toByteArray转为byte[]类型，
		 * 然后才能放到数据库中去，数据库中对应的为BLOB类型数据*/
		Bitmap bitmap = ((BitmapDrawable) person.getPersonDrawable()).getBitmap();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, os);
		db.execSQL("insert into " + Person.TB_NAME + " values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
				new Object[] {person.getPersonId(), os.toByteArray(), person.getPersonNickname(), 
				person.getPersonSignatrue(), person.getPersonFaculty(), person.getPersonYear(), 
				person.getPersonSpecialty(), person.getPersonName(), person.getPersonSex()});
	}
	
	/*根据personId删除指定行*/
	public void deletePerson(String personId) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.execSQL("delete from " + Person.TB_NAME + " where " + Person.PERSON_ID + " = ? ", 
				new String[] {personId, });
	}
	
	public void emptyPersonDB() {
		dbHelper.getReadableDatabase().execSQL("delete from " + Person.TB_NAME);
	}
	
	/*根据PersonId返回学校信息*/
	public Person getPersonById(String personId) {
		Person person = null;
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select * from " + Person.TB_NAME + " where " + Person.PERSON_ID + " = ? ", 
				new String[] {personId, });
		if (null != cursor && cursor.getCount() > 0) {
			cursor.moveToFirst();		//这一句一定要加，总之我不加就出错了
			/*在这里用户头像从数据库中读取也是一件麻烦的事
			 * 首先要将数据库中的BLOB类型数据读出来，存到byte[]里面去，
			 * 然后用byte[]建一个输入流ByteArrayInputStream，
			 * 最后Drawable可以调用createFromStream根据输入流生成Drawable对象*/
			byte[] drawableByte = cursor.getBlob(cursor.getColumnIndex(Person.PERSON_DRAWABLE));
			ByteArrayInputStream is = new ByteArrayInputStream(drawableByte);
			Drawable personDrawable = Drawable.createFromStream(is, Person.PERSON_DRAWABLE);
			String personNickname = cursor.getString(cursor.getColumnIndex(Person.PERSON_NICKNAME));
			String personSignatrue = cursor.getString(cursor.getColumnIndex(Person.PERSON_SIGNATRUE));
			String personFaculty = cursor.getString(cursor.getColumnIndex(Person.PERSON_FACULTY));
			String personYear = cursor.getString(cursor.getColumnIndex(Person.PERSON_YEAR));
			String personSpecialty = cursor.getString(cursor.getColumnIndex(Person.PERSON_SPECIALTY));
			String personName = cursor.getString(cursor.getColumnIndex(Person.PERSON_NAME));
			String personSex = cursor.getString(cursor.getColumnIndex(Person.PERSON_SEX));
			person = new Person(personDrawable, personNickname, personSignatrue, 
					personFaculty, personYear, personSpecialty, personName, personSex);
		}
		return person;
	}
	
	/*关闭数据库缓存，一般在activity的onDestroy方法中调用*/
	public void closeDBHelper() {
		dbHelper.close();
	}
	
}
