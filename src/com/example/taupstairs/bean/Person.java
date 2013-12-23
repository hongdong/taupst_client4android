package com.example.taupstairs.bean;

import android.graphics.drawable.Drawable;

public class Person {

	private Long id;
	private String personId;
	//头像，昵称，个性签名
	private Drawable personDrawable;
	private String personNickname;
	private String personSignatrue;
	//院系，年级，专业，姓名，性别
	private String personFaculty;
	private String personYear;
	private String personSpecialty;
	private String personName;
	private String personSex;
	
	public static final String TB_NAME = "person";
	
	public static final String ID = "_id";
	public static final String PERSON_ID = "personId";
	public static final String PERSON_DRAWABLE = "personDrawable";
	public static final String PERSON_NICKNAME = "personNickname";
	public static final String PERSON_SIGNATRUE = "personSignatrue";
	public static final String PERSON_FACULTY = "personFaculty";
	public static final String PERSON_YEAR = "personYear";
	public static final String PERSON_SPECIALTY = "personSpecialty";
	public static final String PERSON_NAME = "personName";
	public static final String PERSON_SEX = "personSex";
	
	public Person(){
		
	} 

	public Person(Drawable personDrawable, String personNickname, String personSignatrue,
			String personFaculty, String personYear, String personSpecialty,
			String personName, String personSex) {
		this.personDrawable = personDrawable;
		this.personNickname = personNickname;
		this.personSignatrue = personSignatrue;
		this.personFaculty = personFaculty;
		this.personYear = personYear;
		this.personSpecialty = personSpecialty;
		this.personName = personName;
		this.personSex = personSex;
	}
	
	public Person(String personId, Drawable personDrawable,
			String personNickname, String personSignatrue,
			String personFaculty, String personYear, String personSpecialty,
			String personName, String personSex) {
		this.personId = personId;
		this.personDrawable = personDrawable;
		this.personNickname = personNickname;
		this.personSignatrue = personSignatrue;
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
	public Drawable getPersonDrawable() {
		return personDrawable;
	}
	public void setPersonDrawable(Drawable personDrawable) {
		this.personDrawable = personDrawable;
	}
	public String getPersonNickname() {
		return personNickname;
	}
	public void setPersonNickname(String personNickname) {
		this.personNickname = personNickname;
	}
	public String getPersonSignatrue() {
		return personSignatrue;
	}
	public void setPersonSignatrue(String personSignatrue) {
		this.personSignatrue = personSignatrue;
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
		return "Person [personId=" + personId + ", personNickname="
				+ personNickname + ", personSignatrue=" + personSignatrue
				+ ", personFaculty=" + personFaculty + ", personYear="
				+ personYear + ", personSpecialty=" + personSpecialty
				+ ", personName=" + personName + ", personSex=" + personSex
				+ "]";
	}
	
}
