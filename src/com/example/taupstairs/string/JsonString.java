package com.example.taupstairs.string;

public class JsonString {

	public static class Login {
		public static final String IS_LOGINED = "isLogined";
		public static final String USERS_ID = "users_id";
		public static final String CAPTCHA = "mcode";
		public static final String IS_EXIST = "isExist";
		public static final String STATE = "state";
		public static final String STATE_OK = "1";
		public static final String STATE_NO = "2";
	}
	
	public static class Person {
		public static final String PERSON_ID = "users_id";
		
		public static final String PERSON_PHOTOURL = "photo";
		public static final String PERSON_NICKNAME = "username";
		public static final String PERSON_SIGNATRUE = "signature";
		public static final String PERSON_PRAISE = "total_praise";
		
		public static final String PERSON_QQ = "qq";
		public static final String PERSON_EMAIL = "email";
		public static final String PERSON_PHONE = "phone";
		
		public static final String PERSON_FACULTY = "department";
		public static final String PERSON_YEAR = "grade";
		public static final String PERSON_SPECIALTY = "special";
		public static final String PERSON_NAME = "realname";
		public static final String PERSON_SEX = "sex";
	}
	
	public static class Status {
		public static final String STATUS_ID = "task_id";
		public static final String STATUS_TITLE = "title";
		public static final String STATUS_CONTENT = "content";
		public static final String STATUS_RELEASETIME = "release_time";
		public static final String STATUS_ENDTIME = "end_of_time";
		public static final String STATUS_REWARDS = "rewards";
		public static final String STATUS_MESSAGECOUNT = "tm_cnt";
		public static final String STATUS_SIGNUPCOUNT = "sign_cnt";
		
		public static final String PERSON_ID = "users_id";
		public static final String PERSON_PHOTOURL = "photo";
		public static final String PERSON_NICKNAME = "username";
		public static final String PERSON_DEPARTMENT = "department";
		public static final String PERSON_GRADE = "grade";
		public static final String PERSON_SEX = "sex";
	}
	
	public static class Message {
		public static final String MESSAGE_ID = "message_id";
		public static final String PERSON_ID = "users_id";
		public static final String PERSON_SEX = "sex";
		public static final String PERSON_PHOTOURL = "photo";
		public static final String PERSON_NICKNAME = "username";
		public static final String MESSAGE_TIME = "message_time";
		public static final String MESSAGE_CONTENTS = "content";
	}
	
	public static class MessageContent {
		public static final String CONTENT_ID = "contentId";
		public static final String REPLY_ID = "reply_id";
		public static final String REPLY_NICKNAME = "reply";
		public static final String RECEIVE_ID = "replied_id";
		public static final String RECEIVE_NICKNAME = "replied";
		public static final String CONTENT = "message_content";
	}
	
	public static class Rank {
		public static final String MY_RANK = "mRank";
		public static final String RANK_LIST = "rankList";
		public static final String PERSON_ID = "users_id";
		public static final String PERSON_PHOTOURL = "photo";
		public static final String PERSON_NICKNAME = "username";
		public static final String PERSON_SEX = "sex";
		public static final String RANK_PRAISE = "total_praise";
		public static final String RANK_RANK = "pm";
	}
	
	public static class Info {
		public static final String INFO_ID = "news_id";
		public static final String PERSON_ID = "send";
		public static final String PERSON_PHOTOURL = "photo";
		public static final String PERSON_NICKNAME = "username";
		public static final String INFO_RELEASETIME = "time";
		public static final String INFO_CONTENT = "content";
		
		public static final String PERSON_DEPARTMENT = "department";
		public static final String PERSON_GRADE = "grade";
		public static final String PERSON_SEX = "sex";
		
		public static final String INFO_SOURCE = "source";
		public static final String INFO_TYPE = "type";
	}
	
	public static class InfoMessage {
		public static final String MESSAGE_ID = "message_id";
		public static final String CURRENT_CONTENT = "message_content";
		public static final String STATUS_ID = "task_id";
		public static final String STATUS_PERSONID = "t_users_id";
		public static final String STATUS_PERSONNICKNAME = "username";
		public static final String STATUS_TITLE = "title";
		public static final String MESSAGE_CONTENTS = "tmList";
	}
	
	public static class InfoSignUp {
		public static final String STATUS_ID = "task_id";
		public static final String STATUS_PERSONID = "users_id";
		public static final String STATUS_PERSONNICKNAME = "my_name";
		public static final String STATUS_TITLE = "title";
		public static final String SIGNUP_NICKNAME = "username";
		public static final String SIGNUP_STRING = "message";
		public static final String PERSON_CONTACK = "open_mes";
		public static final String PERSON_PHONE = "phone";
		public static final String PERSON_QQ = "qq";
		public static final String PERSON_EMAIL = "email";
	}
	
	public static class Return {
		public static final String STATE = "state";
		public static final String STATE_OK = "0";
		public static final String SUCCESS = "success";
		public static final String SUCCESS_STRING = "true";
		public static final String MSG = "msg";
	}
}
