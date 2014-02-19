package com.example.taupstairs.bean;

public class SignUp {

	private String signUpId;
	private String personId;
	private String personPhotoUrl;
	private String personNickname;
	private String signUpTime;
	private String isExe;
	private String signUpPraise;
	private String signUpMessage;
	
	public static final String SIGNUP_ID = "signUpId";
	public static final String PERSON_ID = "personId";
	public static final String SIGNUP_PRAISE = "signUpPraise";
	public static final String SIGNUP_MESSAGE = "signUpMessage";
	
	public String getSignUpId() {
		return signUpId;
	}
	public void setSignUpId(String signUpId) {
		this.signUpId = signUpId;
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
	public String getSignUpTime() {
		return signUpTime;
	}
	public void setSignUpTime(String signUpTime) {
		this.signUpTime = signUpTime;
	}
	public String getIsExe() {
		return isExe;
	}
	public void setIsExe(String isExe) {
		this.isExe = isExe;
	}
	public String getSignUpPraise() {
		return signUpPraise;
	}
	public void setSignUpPraise(String signUpPraise) {
		this.signUpPraise = signUpPraise;
	}
	public String getSignUpMessage() {
		return signUpMessage;
	}
	public void setSignUpMessage(String signUpMessage) {
		this.signUpMessage = signUpMessage;
	}
	@Override
	public String toString() {
		return "SignUp [signUpId=" + signUpId + ", personId=" + personId
				+ ", personPhotoUrl=" + personPhotoUrl + ", personNickname="
				+ personNickname + ", signUpTime=" + signUpTime + ", isExe="
				+ isExe + ", signUpPraise=" + signUpPraise + ", signUpMessage="
				+ signUpMessage + "]";
	}
	
}
