package com.example.taupstairs.bean;

public class Info {

	private Long id;
	
	private String infoId;
	private String personId;
	private String personPhotoUrl;
	private String personNickname;
	private String infoReleaseTime;
	private String infoContent;
	
	public static final String TB_NAME = "info";
	public static final int INFO_COUNT_PERPAGE = 20;
	
	public static final String ID = "_id";
	
	public static final String INFO_ID = "infoId";
	public static final String PERSON_ID = "personId";
	public static final String PERSON_PHOTOURL = "personPhotoUrl";
	public static final String PERSON_NICKNAME = "personNickname";
	public static final String INFO_RELEASETIME = "infoReleaseTime";
	public static final String INFO_CONTENT = "infoContent";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInfoId() {
		return infoId;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
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
	public String getInfoReleaseTime() {
		return infoReleaseTime;
	}
	public void setInfoReleaseTime(String infoReleaseTime) {
		this.infoReleaseTime = infoReleaseTime;
	}
	public String getInfoContent() {
		return infoContent;
	}
	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}
	
}
