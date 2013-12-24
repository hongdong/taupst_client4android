package com.example.taupstairs.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.string.JsonString;

public class JsonUtil {

	public static Person getPerson(Context context, String jsonString) {
		Person person = new Person();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			person.setPersonId(jsonObject.getString(JsonString.Person.PERSON_ID));
			person.setPersonSex(jsonObject.getString(JsonString.Person.PERSON_SEX));	//先确认这人是男是女
			if (jsonObject.isNull(JsonString.Person.PERSON_DRAWABLE)) {		//如果没有头像，就用默认的
				if (person.getPersonSex().trim().equals(Person.MALE)) {
					Drawable drawable = context.getResources().getDrawable(R.drawable.default_drawable);
					person.setPersonDrawable(drawable);
				} else if (person.getPersonSex().trim().equals(Person.FEMALE)){
					Drawable drawable = context.getResources().getDrawable(R.drawable.default_drawable);
					person.setPersonDrawable(drawable);
				}
			} else {
				/*在这里，获取头像又是一件麻烦的事*/
				String pthto = jsonObject.getString(JsonString.Person.PERSON_DRAWABLE);
				Drawable drawable = HttpClientUtil.getPersonDrawable(HttpClientUtil.BASE_URL + pthto);
				person.setPersonDrawable(drawable);
			}
			if (jsonObject.isNull(JsonString.Person.PERSON_NICKNAME)) {		//如果没有昵称，就用默认的
				if (person.getPersonSex().trim().equals(Person.MALE)) {
					person.setPersonNickname(Person.MALE_NICKNAME);
				} else if (person.getPersonSex().trim().equals(Person.FEMALE)){
					person.setPersonNickname(Person.FEMALE_NICKNAME);
				}
			} else {
				person.setPersonNickname(jsonObject.getString(JsonString.Person.PERSON_NICKNAME));
			}
			if (jsonObject.isNull(JsonString.Person.PERSON_SIGNATRUE)) {	//如果没有个性签名，就用默认的
				if (person.getPersonSex().trim().equals(Person.MALE)) {
					person.setPersonSignatrue(Person.MALE_SIGNATRUE);
				} else if (person.getPersonSex().trim().equals(Person.FEMALE)){
					person.setPersonSignatrue(Person.FEMALE_SIGNATRUE);
				}
			} else {
				person.setPersonSignatrue(jsonObject.getString(JsonString.Person.PERSON_SIGNATRUE));
			}
			person.setPersonFaculty(jsonObject.getString(JsonString.Person.PERSON_FACULTY));
			person.setPersonYear(jsonObject.getString(JsonString.Person.PERSON_YEAR));
			person.setPersonSpecialty(jsonObject.getString(JsonString.Person.PERSON_SPECIALTY));
			person.setPersonName(jsonObject.getString(JsonString.Person.PERSON_NAME));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return person;
	}
}
