package com.example.taupstairs.util;

import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.drawable.Drawable;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.string.JsonString;

public class JsonUtil {

	public static Person getPerson(String jsonString) {
		Person person = new Person();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			person.setPersonId(jsonObject.getString(JsonString.Person.PERSON_ID));
			if (!jsonObject.isNull(JsonString.Person.PERSON_DRAWABLE)) {
				/*在这里，获取头像又是一件麻烦的事*/
				String pthto = jsonObject.getString(JsonString.Person.PERSON_DRAWABLE);
				Drawable drawable = HttpClientUtil.getPersonDrawable(HttpClientUtil.BASE_URL + pthto);
				person.setPersonDrawable(drawable);
			}
			if (!jsonObject.isNull(JsonString.Person.PERSON_NICKNAME)) {
				person.setPersonNickname(jsonObject.getString(JsonString.Person.PERSON_NICKNAME));
			}
			if (!jsonObject.isNull(JsonString.Person.PERSON_SIGNATRUE)) {
				person.setPersonSignatrue(jsonObject.getString(JsonString.Person.PERSON_SIGNATRUE));
			}
			person.setPersonFaculty(jsonObject.getString(JsonString.Person.PERSON_FACULTY));
			person.setPersonYear(jsonObject.getString(JsonString.Person.PERSON_YEAR));
			person.setPersonSpecialty(jsonObject.getString(JsonString.Person.PERSON_SPECIALTY));
			person.setPersonName(jsonObject.getString(JsonString.Person.PERSON_NAME));
			person.setPersonSex(jsonObject.getString(JsonString.Person.PERSON_SEX));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return person;
	}
}
