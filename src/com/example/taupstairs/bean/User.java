package com.example.taupstairs.bean;

public class User {

	private Long id;
	private String userId;
	private String userCollege;
	private String userStudentId;
	private String userPassword;
	
	public static final String TABLE_NAME = "user";
	
	public static final String ID = "_id";
	public static final String USER_ID = "userId";
	public static final String USER_COLLEGE = "userCollege";
	public static final String USER_STUDENTID = "userStudentId";
	public static final String USER_PASSWORD = "userPassword";
	
	public User(String userCollege, String userStudentId, String userPassword) {
		this.userCollege = userCollege;
		this.userStudentId = userStudentId;
		this.userPassword = userPassword;
	}
	
	public User(String userId, String userCollege, String userStudentId, String userPassword) {
		this.userId = userId;
		this.userCollege = userCollege;
		this.userStudentId = userStudentId;
		this.userPassword = userPassword;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserCollege() {
		return userCollege;
	}
	public void setUserCollege(String userCollege) {
		this.userCollege = userCollege;
	}
	public String getUserStudentId() {
		return userStudentId;
	}
	public void setUserStudentId(String userStudentId) {
		this.userStudentId = userStudentId;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
}
