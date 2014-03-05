package com.example.taupstairs.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.taupstairs.bean.Info;
import com.example.taupstairs.bean.InfoEndTask;
import com.example.taupstairs.bean.InfoExecTask;
import com.example.taupstairs.bean.InfoMessage;
import com.example.taupstairs.bean.InfoSignUp;
import com.example.taupstairs.bean.Message;
import com.example.taupstairs.bean.MessageContent;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Rank;
import com.example.taupstairs.bean.SignUp;
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
	
	public static Status getStatus(String jsonString) {
		Status status = new Status();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			
			status.setIsSign(jsonObject.getString(JsonString.Status.IS_SIGN));
			status.setStatusState(jsonObject.getString(JsonString.Status.STATUS_STATE));
			
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
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		return status;
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
	
	public static List<Info> getInfos(String jsonString) {
		List<Info> infos = new ArrayList<Info>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Info info = new Info();
				info.setInfoId(jsonObject.getString(JsonString.Info.INFO_ID));
				info.setPersonId(jsonObject.getString(JsonString.Info.PERSON_ID));
				info.setPersonPhotoUrl(jsonObject.getString(JsonString.Info.PERSON_PHOTOURL));
				info.setPersonNickname(jsonObject.getString(JsonString.Info.PERSON_NICKNAME));
				info.setInfoReleaseTime(jsonObject.getString(JsonString.Info.INFO_RELEASETIME));
				info.setInfoContent(jsonObject.getString(JsonString.Info.INFO_CONTENT));
				
				info.setPersonDepartment(jsonObject.getString(JsonString.Info.PERSON_DEPARTMENT));
				info.setPersonGrade(jsonObject.getString(JsonString.Info.PERSON_GRADE));
				info.setPersonSex(jsonObject.getString(JsonString.Info.PERSON_SEX));
				
				info.setInfoSource(jsonObject.getString(JsonString.Info.INFO_SOURCE));
				info.setInfoType(jsonObject.getString(JsonString.Info.INFO_TYPE));
								
				infos.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return infos;
	}
	
	public static Object getInfoDetail(int type, String jsonString) {
		Object object = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);	
			switch (type) {
			case 1:
				InfoMessage infoMessage = new InfoMessage();		
				String root_id = jsonObject.getString(JsonString.InfoMessage.ROOT_ID);
				if (root_id.trim().equals("-1")) {
					infoMessage.setMessageId(jsonObject.getString(JsonString.InfoMessage.MESSAGE_ID));
				} else {
					infoMessage.setMessageId(jsonObject.getString(JsonString.InfoMessage.ROOT_ID));
				}
				infoMessage.setCurrentMessage(jsonObject.getString(JsonString.InfoMessage.CURRENT_CONTENT));
				infoMessage.setStatusId(jsonObject.getString(JsonString.InfoMessage.STATUS_ID));
				infoMessage.setStatusPersonId(jsonObject.getString(JsonString.InfoMessage.STATUS_PERSONID));
				infoMessage.setStatusPersonNickname(jsonObject.getString(JsonString.InfoMessage.STATUS_PERSONNICKNAME));
				infoMessage.setStatusTitle(jsonObject.getString(JsonString.InfoMessage.STATUS_TITLE));
				String content = jsonObject.getString(JsonString.InfoMessage.MESSAGE_CONTENTS);
				List<MessageContent> contents = getInfoMessageContents(content);
				infoMessage.setContents(contents);
				object = infoMessage;
				break;
				
			case 2:
				InfoExecTask infoExecTask = new InfoExecTask();
				infoExecTask.setSignUpStringReply(jsonObject.getString(JsonString.InfoExecTask.SIGNUP_STRING_REPLY));
				infoExecTask.setStatusId(jsonObject.getString(JsonString.InfoExecTask.STATUS_ID));
				infoExecTask.setStatusPersonId(jsonObject.getString(JsonString.InfoExecTask.STATUS_PERSONID));
				infoExecTask.setStatusPersonNickname(
						jsonObject.getString(JsonString.InfoExecTask.STATUS_PERSONNICKNAME));
				infoExecTask.setStatusTitle(jsonObject.getString(JsonString.InfoExecTask.STATUS_TITLE));
				infoExecTask.setSignUpStringNickname(
						jsonObject.getString(JsonString.InfoExecTask.SIGNUP_STRING_NICKNAME));
				infoExecTask.setSignUpString(jsonObject.getString(JsonString.InfoExecTask.SIGNUP_STRING));
				infoExecTask.setPersonContact(jsonObject.getString(JsonString.InfoExecTask.PERSON_CONTACK));
				if (!jsonObject.isNull(JsonString.InfoExecTask.PERSON_PHONE)) {
					infoExecTask.setPersonPhone(jsonObject.getString(JsonString.InfoExecTask.PERSON_PHONE));
				}
				if (!jsonObject.isNull(JsonString.InfoExecTask.PERSON_QQ)) {
					infoExecTask.setPersonQq(jsonObject.getString(JsonString.InfoExecTask.PERSON_QQ));
				}
				if (!jsonObject.isNull(JsonString.InfoExecTask.PERSON_EMAIL)) {
					infoExecTask.setPersonEmail(jsonObject.getString(JsonString.InfoExecTask.PERSON_EMAIL));
				}
				object = infoExecTask;
				break;
				
			case 3:
				InfoSignUp infoSignUp = new InfoSignUp();
				infoSignUp.setSignUpId(jsonObject.getString(JsonString.InfoSignUp.SIGNUP_ID));
				infoSignUp.setStatusId(jsonObject.getString(JsonString.InfoSignUp.STATUS_ID));
				infoSignUp.setStatusPersonId(jsonObject.getString(JsonString.InfoSignUp.STATUS_PERSONID));
				infoSignUp.setStatusPersonNickname(jsonObject.getString(JsonString.InfoSignUp.STATUS_PERSONNICKNAME));
				infoSignUp.setStatusTitle(jsonObject.getString(JsonString.InfoSignUp.STATUS_TITLE));
				infoSignUp.setStatusEndTime(jsonObject.getString(JsonString.InfoSignUp.STATUS_ENDTIME));
				infoSignUp.setStatusState(jsonObject.getString(JsonString.InfoSignUp.STATUS_STATE));
				infoSignUp.setSignUpNickname(jsonObject.getString(JsonString.InfoSignUp.SIGNUP_NICKNAME));
				infoSignUp.setSignUpString(jsonObject.getString(JsonString.InfoSignUp.SIGNUP_STRING));
				infoSignUp.setHasExec(jsonObject.getString(JsonString.InfoSignUp.HAS_EXEC));
				infoSignUp.setPersonContact(jsonObject.getString(JsonString.InfoSignUp.PERSON_CONTACK));
				if (!jsonObject.isNull(JsonString.InfoSignUp.PERSON_PHONE)) {
					infoSignUp.setPersonPhone(jsonObject.getString(JsonString.InfoSignUp.PERSON_PHONE));
				}
				if (!jsonObject.isNull(JsonString.InfoSignUp.PERSON_QQ)) {
					infoSignUp.setPersonQq(jsonObject.getString(JsonString.InfoSignUp.PERSON_QQ));
				}
				if (!jsonObject.isNull(JsonString.InfoSignUp.PERSON_EMAIL)) {
					infoSignUp.setPersonEmail(jsonObject.getString(JsonString.InfoSignUp.PERSON_EMAIL));
				}
				object = infoSignUp;
				break;
				
			case 4:
				InfoEndTask infoEndTask = new InfoEndTask();
				infoEndTask.setEndTaskString(jsonObject.getString(JsonString.InfoEndTask.ENDTASK_STRING));
				infoEndTask.setStatusId(jsonObject.getString(JsonString.InfoEndTask.STATUS_ID));
				infoEndTask.setStatusPersonId(jsonObject.getString(JsonString.InfoEndTask.STATUS_PERSONID));
				infoEndTask.setStatusPersonNickname(jsonObject.getString(JsonString.InfoEndTask.STATUS_PERSONNICKNAME));
				infoEndTask.setStatusTitle(jsonObject.getString(JsonString.InfoEndTask.STATUS_TITLE));
				infoEndTask.setEndTaskPraise(jsonObject.getString(JsonString.InfoEndTask.ENDTASK_PRAISE));
				object = infoEndTask;
				break;
	
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
	
	public static List<MessageContent> getInfoMessageContents(String jsonString) {
		List<MessageContent> contents = new ArrayList<MessageContent>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				MessageContent content = new MessageContent();
				content.setReplyId(jsonObject.getString(JsonString.MessageContent.REPLY_ID));
				content.setReplyNickname(jsonObject.getString(JsonString.MessageContent.REPLY_NICKNAME));
				content.setReceiveId(jsonObject.getString(JsonString.MessageContent.RECEIVE_ID));
				content.setReceiveNickname(jsonObject.getString(JsonString.MessageContent.RECEIVE_NICKNAME));
				content.setContent(jsonObject.getString(JsonString.MessageContent.CONTENT));
				contents.add(content);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return contents;
	}
	
	public static List<SignUp> getSignUps(String jsonString) {
		List<SignUp> signUps = new ArrayList<SignUp>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				SignUp signUp = new SignUp();
				signUp.setSignUpId(jsonObject.getString(JsonString.SignUp.SIGNUP_ID));
				signUp.setPersonId(jsonObject.getString(JsonString.SignUp.PERSON_ID));
				signUp.setPersonPhotoUrl(jsonObject.getString(JsonString.SignUp.PERSON_PHOTOURL));
				signUp.setPersonNickname(jsonObject.getString(JsonString.SignUp.PERSON_NICKNAME));
				signUp.setSignUpTime(jsonObject.getString(JsonString.SignUp.SIGNUP_TIME));
				signUp.setIsExe(jsonObject.getString(JsonString.SignUp.IS_EXE));
				signUp.setSignUpPraise(jsonObject.getString(JsonString.SignUp.SIGNUP_PRAISE));
				signUp.setSignUpMessage(jsonObject.getString(JsonString.SignUp.SIGNUP_MESSAGE));
				signUps.add(signUp);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return signUps;
	}
	
}
