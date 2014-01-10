package com.example.taupstairs.bean;

import java.util.List;

public class Message {

	private Long id;
	/*基本信息*/
	private String messageId;
	private String personId;
	private String personSex;
	private String personPhotoUrl;
	private String personNickname;
	private String messageTime;
	/*内容*/
	private List<MessageContent> messageContents;
	
	public static final String TB_NAME = "message";
	public static final String ID = "_id";
	
	public static final String MESSAGE_ID = "messageId";
	public static final String PERSON_ID = "personId";
	public static final String PERSON_SEX = "personSex";
	public static final String PERSON_PHOTOURL = "personPhotoUrl";
	public static final String PERSON_NICKNAME = "personNickname";
	public static final String MESSAGE_TIME = "messageTime";
	public static final String MESSAGE_CONTENTS = "messageContents";
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonSex() {
		return personSex;
	}

	public void setPersonSex(String personSex) {
		this.personSex = personSex;
	}

	public String getPersonPhotoUrl() {
		return personPhotoUrl;
	}

	public void setPersonPhotoUrl(String personPhotoUrl) {
		this.personPhotoUrl = personPhotoUrl;
	}

	public String getPersonNickname() {
		return personNickname;
	}

	public void setPersonNickname(String personNickname) {
		this.personNickname = personNickname;
	}

	public String getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}

	public List<MessageContent> getMessageContents() {
		return messageContents;
	}

	public void setMessageContents(List<MessageContent> messageContents) {
		this.messageContents = messageContents;
	}

	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", personId=" + personId
				+ ", personPhotoUrl=" + personPhotoUrl + ", personNickname="
				+ personNickname + ", messageTime=" + messageTime
				+ ", messageContents=" + messageContents + "]";
	}
	
}
