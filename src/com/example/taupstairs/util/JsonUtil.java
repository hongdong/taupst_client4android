package com.example.taupstairs.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Status;
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
				Drawable drawable = HttpClientUtil.getPersonDrawable(HttpClientUtil.PHOTO_BASE_URL + pthto);
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
	
	public static List<Status> getListStatus(String jsonString) {
		List<Status> listStatus = new ArrayList<Status>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Status status = new Status();
				status.setStatusId(jsonObject.getString(JsonString.Status.STATUS_ID));
				status.setStatusTitle(jsonObject.getString(JsonString.Status.STATUS_TITLE));
				status.setStatusContent(jsonObject.getString(JsonString.Status.STATUS_CONTENT));
				status.setStatusReleaseTime(jsonObject.getString(JsonString.Status.STATUS_RELEASETIME));
				status.setStatusEndTime(jsonObject.getString(JsonString.Status.STATUS_ENDTIME));
				status.setStatusRewards(jsonObject.getString(JsonString.Status.STATUS_REWARDS));
				status.setStatusMessageCount(jsonObject.getString(JsonString.Status.STATUS_MESSAGECOUNT));
				status.setStatusSignUpCount(jsonObject.getString(JsonString.Status.STATUS_SIGNUPCOUNT));
				
				status.setPersonId(jsonObject.getString(JsonString.Status.PERSON_ID));
				
				/*任务里面的头像和昵称可能是为空的*/
				if (!jsonObject.isNull(JsonString.Status.PERSON_PHOTOURL)) {
					status.setPersonPhotoUrl(jsonObject.getString(JsonString.Status.PERSON_PHOTOURL));
				}
				
				if (!jsonObject.isNull(JsonString.Status.PERSON_NICKNAME)) {
					status.setPersonNickname(jsonObject.getString(JsonString.Status.PERSON_NICKNAME));
				}
				
				status.setPersonDepartment(jsonObject.getString(JsonString.Status.PERSON_DEPARTMENT));
				status.setPersonGrade(jsonObject.getString(JsonString.Status.PERSON_GRADE));
				status.setPersonSex(jsonObject.getString(JsonString.Status.PERSON_SEX));
				
				listStatus.add(status);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listStatus;
	}
	
}
