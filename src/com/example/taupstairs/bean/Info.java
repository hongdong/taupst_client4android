package com.example.taupstairs.bean;

public class Info {

	private Long id;
	
	private String infoId;
	private String personId;
	private String personPhotoUrl;
	private String personNickname;
	private String infoReleaseTime;
	private String infoContent;
	
	private String personDepartment;
	private String personGrade;
	private String personSex;
	
	private String infoSource;
	private String infoType;
	
	private InfoMessage infoMessage;
	private InfoExecTask infoExecTask;
	private InfoEndTask infoEndTask;
	private InfoSignUp infoSignUp;
	private InfoPrivateLetter infoPrivateLetter;
	
	public static final int INFO_COUNT_PERPAGE = 20;
	
	public static final String ID = "_id";
	
	public static final String INFO_ID = "infoId";
	public static final String PERSON_ID = "personId";
	public static final String PERSON_PHOTOURL = "personPhotoUrl";
	public static final String PERSON_NICKNAME = "personNickname";
	public static final String INFO_RELEASETIME = "infoReleaseTime";
	public static final String INFO_CONTENT = "infoContent";
	
	public static final String PERSON_DEPARTMENT = "personDepartment";
	public static final String PERSON_GRADE = "personGrade";
	public static final String PERSON_SEX = "personSex";
	
	public static final String INFO_SOURCE = "infoSource";
	public static final String INFO_TYPE = "infoType";
	
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
	
	public String getPersonDepartment() {
		return personDepartment;
	}
	public void setPersonDepartment(String personDepartment) {
		this.personDepartment = personDepartment;
	}
	public String getPersonGrade() {
		return personGrade;
	}
	public void setPersonGrade(String personGrade) {
		this.personGrade = personGrade;
	}
	public String getPersonSex() {
		return personSex;
	}
	public void setPersonSex(String personSex) {
		this.personSex = personSex;
	}
	
	public String getInfoSource() {
		return infoSource;
	}
	public void setInfoSource(String infoSource) {
		this.infoSource = infoSource;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	
	public InfoMessage getInfoMessage() {
		return infoMessage;
	}
	public void setInfoMessage(InfoMessage infoMessage) {
		this.infoMessage = infoMessage;
	}
	public InfoExecTask getInfoExecTask() {
		return infoExecTask;
	}
	public void setInfoExecTask(InfoExecTask infoExecTask) {
		this.infoExecTask = infoExecTask;
	}
	public InfoEndTask getInfoEndTask() {
		return infoEndTask;
	}
	public void setInfoEndTask(InfoEndTask infoEndTask) {
		this.infoEndTask = infoEndTask;
	}
	public InfoSignUp getInfoSignUp() {
		return infoSignUp;
	}
	public void setInfoSignUp(InfoSignUp infoSignUp) {
		this.infoSignUp = infoSignUp;
	}
	public InfoPrivateLetter getInfoPrivateLetter() {
		return infoPrivateLetter;
	}
	public void setInfoPrivateLetter(InfoPrivateLetter infoPrivateLetter) {
		this.infoPrivateLetter = infoPrivateLetter;
	}
	@Override
	public String toString() {
		return "Info [id=" + id + ", infoId=" + infoId + ", personId="
				+ personId + ", personPhotoUrl=" + personPhotoUrl
				+ ", personNickname=" + personNickname + ", infoReleaseTime="
				+ infoReleaseTime + ", infoContent=" + infoContent
				+ ", personDepartment=" + personDepartment + ", personGrade="
				+ personGrade + ", personSex=" + personSex + ", infoSource="
				+ infoSource + ", infoType=" + infoType + ", infoMessage="
				+ infoMessage + ", infoExecTask=" + infoExecTask
				+ ", infoEndTask=" + infoEndTask + ", infoSignUp=" + infoSignUp
				+ ", infoPrivateLetter=" + infoPrivateLetter + "]";
	}
	
}
