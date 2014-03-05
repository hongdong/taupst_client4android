package com.example.taupstairs.bean;

public class Status {

	private Long id;
	
	/*是否报名，状态，ID，标题，内容，发布时间，截止时间，报酬，留言数，报名数*/
	
	private String isSign;
	private String statusState;
	
	private String statusId;
	private String statusTitle;
	private String statusContent;
	private String statusReleaseTime;
	private String statusEndTime;
	private String statusRewards;
	private String statusMessageCount;
	private String statusSignUpCount;
	/*发布人ID，头像，昵称，院系，年级，性别*/
	private String personId;
	private String personPhotoUrl;
	private String personNickname;
	private String personDepartment;
	private String personGrade;
	private String personSex;
	
	public static final int STATUS_COUNT_PERPAGE = 10;
	
	public static final String ID = "_id";
	
	public static final String STATUS_STATE = "statusState";
	public static final String STATUS_ID = "statusId";
	public static final String STATUS_TITLE = "statusTitle";
	public static final String STATUS_CONTENT = "statusContent";
	public static final String STATUS_RELEASETIME = "statusReleaseTime";
	public static final String STATUS_ENDTIME = "statusEndTime";
	public static final String STATUS_REWARDS = "statusRewards";
	public static final String STATUS_MESSAGECOUNT = "statusMessageCount";
	public static final String STATUS_SIGNUPCOUNT = "statusSignUpCount";
	
	public static final String PERSON_ID = "personId";
	public static final String PERSON_PHOTOURL = "personPhotoUrl";
	public static final String PERSON_NICKNAME = "personNickname";
	public static final String PERSON_DEPARTMENT = "personDepartment";
	public static final String PERSON_GRADE = "personGrade";
	public static final String PERSON_SEX = "personSex";
	
	public Status() {
		
	}
	
	public Status(String statusId, String statusTitle, String statusContent,
			String statusReleaseTime, String statusEndTime,
			String statusRewards, String statusMessageCount,
			String statusSignUpCount, String personId, String personPhotoUrl, 
			String personNickname, String personDepartment, String personGrade,
			String personSex) {
		this.statusId = statusId;
		this.statusTitle = statusTitle;
		this.statusContent = statusContent;
		this.statusReleaseTime = statusReleaseTime;
		this.statusEndTime = statusEndTime;
		this.statusRewards = statusRewards;
		this.statusMessageCount = statusMessageCount;
		this.statusSignUpCount = statusSignUpCount;
		this.personId = personId;
		this.personPhotoUrl = personPhotoUrl;
		this.personNickname = personNickname;
		this.personDepartment = personDepartment;
		this.personGrade = personGrade;
		this.personSex = personSex;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIsSign() {
		return isSign;
	}
	public void setIsSign(String isSign) {
		this.isSign = isSign;
	}
	public String getStatusState() {
		return statusState;
	}
	public void setStatusState(String statusState) {
		this.statusState = statusState;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getStatusTitle() {
		return statusTitle;
	}
	public void setStatusTitle(String statusTitle) {
		this.statusTitle = statusTitle;
	}
	public String getStatusContent() {
		return statusContent;
	}
	public void setStatusContent(String statusContent) {
		this.statusContent = statusContent;
	}
	public String getStatusReleaseTime() {
		return statusReleaseTime;
	}
	public void setStatusReleaseTime(String statusReleaseTime) {
		this.statusReleaseTime = statusReleaseTime;
	}
	public String getStatusEndTime() {
		return statusEndTime;
	}
	public void setStatusEndTime(String statusEndTime) {
		this.statusEndTime = statusEndTime;
	}
	public String getStatusRewards() {
		return statusRewards;
	}
	public void setStatusRewards(String statusRewards) {
		this.statusRewards = statusRewards;
	}
	public String getStatusSignUpCount() {
		return statusSignUpCount;
	}
	public void setStatusSignUpCount(String statusSignUpCount) {
		this.statusSignUpCount = statusSignUpCount;
	}
	public String getStatusMessageCount() {
		return statusMessageCount;
	}
	public void setStatusMessageCount(String statusMessageCount) {
		this.statusMessageCount = statusMessageCount;
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

	@Override
	public String toString() {
		return "Status [statusId=" + statusId + ", statusTitle=" + statusTitle
				+ ", statusContent=" + statusContent + ", statusReleaseTime="
				+ statusReleaseTime + ", statusEndTime=" + statusEndTime
				+ ", statusRewards=" + statusRewards + ", statusMessageCount="
				+ statusMessageCount + ", statusSignUpCount="
				+ statusSignUpCount + ", personId=" + personId
				+ ", personPhotoUrl=" + personPhotoUrl + ", personNickname="
				+ personNickname + ", personDepartment=" + personDepartment
				+ ", personGrade=" + personGrade + ", personSex=" + personSex
				+ "]";
	}
	
}
