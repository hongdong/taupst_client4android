package com.example.taupstairs.bean;

public class InfoSignUp {

	private String signUpId;
	private String statusId;
	private String statusPersonId;
	private String statusPersonNickname;
	private String statusTitle;
	private String signUpNickname;
	private String signUpString;
	private String hasExec;
	private String personContact;
	private String personPhone;
	private String personQq;
	private String personEmail;
	
	public static final String SIGNUP_ID = "signUpId";
	public static final String SIGNUP_STRING = "signUpString";
	
	public String getSignUpId() {
		return signUpId;
	}
	public void setSignUpId(String signUpId) {
		this.signUpId = signUpId;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getStatusPersonId() {
		return statusPersonId;
	}
	public void setStatusPersonId(String statusPersonId) {
		this.statusPersonId = statusPersonId;
	}
	public String getStatusPersonNickname() {
		return statusPersonNickname;
	}
	public void setStatusPersonNickname(String statusPersonNickname) {
		this.statusPersonNickname = statusPersonNickname;
	}
	public String getStatusTitle() {
		return statusTitle;
	}
	public void setStatusTitle(String statusTitle) {
		this.statusTitle = statusTitle;
	}
	public String getSignUpNickname() {
		return signUpNickname;
	}
	public void setSignUpNickname(String signUpNickname) {
		this.signUpNickname = signUpNickname;
	}
	public String getSignUpString() {
		return signUpString;
	}
	public void setSignUpString(String signUpString) {
		this.signUpString = signUpString;
	}
	public String getHasExec() {
		return hasExec;
	}
	public void setHasExec(String hasExec) {
		this.hasExec = hasExec;
	}
	public String getPersonContact() {
		return personContact;
	}
	public void setPersonContact(String personContact) {
		this.personContact = personContact;
	}
	public String getPersonPhone() {
		return personPhone;
	}
	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}
	public String getPersonQq() {
		return personQq;
	}
	public void setPersonQq(String personQq) {
		this.personQq = personQq;
	}
	public String getPersonEmail() {
		return personEmail;
	}
	public void setPersonEmail(String personEmail) {
		this.personEmail = personEmail;
	}
	@Override
	public String toString() {
		return "InfoSignUp [signUpId=" + signUpId + ", statusId=" + statusId
				+ ", statusPersonId=" + statusPersonId
				+ ", statusPersonNickname=" + statusPersonNickname
				+ ", statusTitle=" + statusTitle + ", signUpNickname="
				+ signUpNickname + ", signUpString=" + signUpString
				+ ", personContact=" + personContact + ", personPhone="
				+ personPhone + ", personQq=" + personQq + ", personEmail="
				+ personEmail + "]";
	}
	
}
