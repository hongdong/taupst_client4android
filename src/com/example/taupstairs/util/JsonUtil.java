package com.example.taupstairs.util;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.taupstairs.bean.Message;
import com.example.taupstairs.bean.MessageContent;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Rank;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.string.JsonString;

public class JsonUtil {

	public static Person getPerson(String jsonString) {
		Person person = new Person();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			person.setPersonId(jsonObject.getString(JsonString.Person.PERSON_ID));			
			person.setPersonPhotoUrl(jsonObject.getString(JsonString.Person.PERSON_PHOTOURL));
			person.setPersonNickname(jsonObject.getString(JsonString.Person.PERSON_NICKNAME));
			person.setPersonSignature(jsonObject.getString(JsonString.Person.PERSON_SIGNATRUE));
			/*这三个很可能是空的*/
			if (!jsonObject.isNull(JsonString.Person.PERSON_QQ)) {
				person.setPersonQq(jsonObject.getString(JsonString.Person.PERSON_QQ));
			} 
			if (!jsonObject.isNull(JsonString.Person.PERSON_EMAIL)) {
				person.setPersonEmail(jsonObject.getString(JsonString.Person.PERSON_EMAIL));
			}
			if (!jsonObject.isNull(JsonString.Person.PERSON_PHONE)) {
				person.setPersonPhone(jsonObject.getString(JsonString.Person.PERSON_PHONE));
			}
			person.setPersonPraise(jsonObject.getString(JsonString.Person.PERSON_PRAISE));
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
				status.setPersonPhotoUrl(jsonObject.getString(JsonString.Status.PERSON_PHOTOURL));
				status.setPersonNickname(jsonObject.getString(JsonString.Status.PERSON_NICKNAME));
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
	
	public static List<Message> getMessages(String jsonString) {
		List<Message> messages = new ArrayList<Message>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Message message = new Message();
				message.setMessageId(jsonObject.getString(JsonString.Message.MESSAGE_ID));
				message.setPersonId(jsonObject.getString(JsonString.Message.PERSON_ID));
				message.setPersonSex(jsonObject.getString(JsonString.Message.PERSON_SEX));
				message.setPersonPhotoUrl(jsonObject.getString(JsonString.Message.PERSON_PHOTOURL));
				message.setPersonNickname(jsonObject.getString(JsonString.Message.PERSON_NICKNAME));
				message.setMessageTime(jsonObject.getString(JsonString.Message.MESSAGE_TIME));
				String content = jsonObject.getString(JsonString.Message.MESSAGE_CONTENTS);
				List<MessageContent> contents = getMessageContents(content);
				message.setMessageContents(contents);
				messages.add(message);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return messages;
	}
	
	public static List<MessageContent> getMessageContents(String jsonString) {
		List<MessageContent> contents = new ArrayList<MessageContent>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				MessageContent content = new MessageContent();
				if (i > 0) {
					content.setReplyId(jsonObject.getString(JsonString.MessageContent.REPLY_ID));
					content.setReplyNickname(jsonObject.getString(JsonString.MessageContent.REPLY_NICKNAME));
					content.setReceiveId(jsonObject.getString(JsonString.MessageContent.RECEIVE_ID));
					content.setReceiveNickname(jsonObject.getString(JsonString.MessageContent.RECEIVE_NICKNAME));
					content.setContent(jsonObject.getString(JsonString.MessageContent.CONTENT));
				} else {
					content.setContent(jsonObject.getString(JsonString.MessageContent.CONTENT));
				}
				
				contents.add(content);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return contents;
	}
	
	public static List<Rank> getRanks(String jsonString) {
		List<Rank> ranks = new ArrayList<Rank>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);	
			String myString = jsonObject.getString(JsonString.Rank.MY_RANK);
			JSONObject myObject = new JSONObject(myString);
			String rankList = jsonObject.getString(JsonString.Rank.RANK_LIST);
			JSONArray jsonArray = new JSONArray(rankList);
			jsonArray.put(myObject);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject rankObject = jsonArray.getJSONObject(i);
				Rank rank = new Rank();
				rank.setPersonId(rankObject.getString(JsonString.Rank.PERSON_ID));
				rank.setPersonPhotoUrl(rankObject.getString(JsonString.Rank.PERSON_PHOTOURL));
				rank.setPersonNickname(rankObject.getString(JsonString.Rank.PERSON_NICKNAME));
				rank.setPersonSex(rankObject.getString(JsonString.Rank.PERSON_SEX));
				rank.setRankPraise(rankObject.getString(JsonString.Rank.RANK_PRAISE));
				rank.setRankRank(rankObject.getString(JsonString.Rank.RANK_RANK));
				ranks.add(rank);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ranks;
	}
	
}
