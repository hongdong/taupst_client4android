package com.example.taupstairs.bean;

public class Person {

	private Long id;
	private String personId;
	//头像，昵称，个性签名，qq，emali，phone，被赞数
	private String personPhotoUrl;
	private String personNickname;
	private String personSignature;
	private String personPraise;
	private String personQq;
	private String personEmail;
	private String personPhone;
	//院系，年级，专业，姓名，性别
	private String personFaculty;
	private String personYear;
	private String personSpecialty;
	private String personName;
	private String personSex;
	
	public static final String TB_NAME = "person";
	
	public static final String ID = "_id";
	public static final String PERSON_ID = "personId";
	public static final String PERSON_PHOTOURL = "personPhotoUrl";
	public static final String PERSON_NICKNAME = "personNickname";
	public static final String PERSON_SIGNATURE = "personSignature";
	public static final String PERSON_PRAISE = "personPraise";
	public static final String PERSON_QQ = "personQq";
	public static final String PERSON_EMAIL = "personEmail";
	public static final String PERSON_PHONE = "personPhone";
	
	public static final String PERSON_FACULTY = "personFaculty";
	public static final String PERSON_YEAR = "personYear";
	public static final String PERSON_SPECIALTY = "personSpecialty";
	public static final String PERSON_NAME = "personName";
	public static final String PERSON_SEX = "personSex";
	
	public static final String MALE = "男";
	public static final String MALE_NICKNAME = "某屌丝男";
	public static final String MALE_SIGNATRUE = "没签名，没妹子";
	public static final String FEMALE = "女";
	public static final String FEMALE_NICKNAME = "某女汉子";
	public static final String FEMALE_SIGNATRUE = "没签名，真汉子";
	
	public Person(){
		
	} 	
	
	public Person(String personQq, String personEmail, String personPhone) {
		this.personQq = personQq;
		this.personEmail = personEmail;
		this.personPhone = personPhone;
	}

	public Person(String personId, String personPhotoUrl,
			String personNickname, String personSignature, String personPraise,
			String personFaculty, String personYear, String personSpecialty,
			String personName, String personSex) {
		this.personId = personId;
		this.personPhotoUrl = personPhotoUrl;
		this.personNickname = personNickname;
		this.personSignature = personSignature;
		this.personPraise = personPraise;
		this.personFaculty = personFaculty;
		this.personYear = personYear;
		this.personSpecialty = personSpecialty;
		this.personName = personName;
		this.personSex = personSex;
	}
	
	public Person(String personId, String personPhotoUrl, String personNickname, 
			String personSignature, String personPraise, String personQq,
			String personEmail, String personPhone, String personFaculty,
			String personYear, String personSpecialty, String personName,
			String personSex) {
		this.personId = personId;
		this.personPhotoUrl = personPhotoUrl;
		this.personNickname = personNickname;
		this.personSignature = personSignature;
		this.personPraise = personPraise;
		this.personQq = personQq;
		this.personEmail = personEmail;
		this.personPhone = personPhone;
		this.personFaculty = personFaculty;
		this.personYear = personYear;
		this.personSpecialty = personSpecialty;
		this.personName = personName;
		this.personSex = personSex;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getPersonSignature() {
		return personSignature;
	}
	public void setPersonSignature(String personSignature) {
		this.personSignature = personSignature;
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
	public String getPersonPhone() {
		return personPhone;
	}
	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}
	public String getPersonPraise() {
		return personPraise;
	}
	public void setPersonPraise(String personPraise) {
		this.personPraise = personPraise;
	}
	public String getPersonFaculty() {
		return personFaculty;
	}
	public void setPersonFaculty(String personFaculty) {
		this.personFaculty = personFaculty;
	}
	public String getPersonYear() {
		return personYear;
	}
	public void setPersonYear(String personYear) {
		this.personYear = personYear;
	}
	public String getPersonSpecialty() {
		return personSpecialty;
	}
	public void setPersonSpecialty(String personSpecialty) {
		this.personSpecialty = personSpecialty;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPersonSex() {
		return personSex;
	}
	public void setPersonSex(String personSex) {
		this.personSex = personSex;
	}

	@Override
	public String toString() {
		return "Person [personId=" + personId + ", personPhotoUrl="
				+ personPhotoUrl + ", personNickname=" + personNickname
				+ ", personSignature=" + personSignature + ", personPraise="
				+ personPraise + ", personQq=" + personQq + ", personEmail="
				+ personEmail + ", personPhone=" + personPhone
				+ ", personFaculty=" + personFaculty + ", personYear="
				+ personYear + ", personSpecialty=" + personSpecialty
				+ ", personName=" + personName + ", personSex=" + personSex
				+ "]";
	}
	
}
