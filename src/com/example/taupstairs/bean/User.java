package com.example.taupstairs.bean;

public class User {

	private Long id;
	private String userId;
	private String userCollegeId;
	private String userCollegeName;
	private String userStudentId;
	private String userPassword;
	
	public static final String TABLE_NAME = "user";
	
	public static final String ID = "_id";
	public static final String USER_ID = "userId";
	public static final String USER_COLLEGEID = "userCollegeId";
	public static final String USER_COLLEGENAME = "userCollegeName";
	public static final String USER_STUDENTID = "userStudentId";
	public static final String USER_PASSWORD = "userPassword";

	public User() {
		
	}

	public User(String userCollegeId, String userStudentId, String userPassword) {
		this.userCollegeId = userCollegeId;
		this.userStudentId = userStudentId;
		this.userPassword = userPassword;
	}
	
	public User(String userId, String userCollegeId, String userCollegeName, 
			String userStudentId, String userPassword) {
		this.userId = userId;
		this.userCollegeId = userCollegeId;
		this.userCollegeName = userCollegeName;
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
	public String getUserCollegeId() {
		return userCollegeId;
	}
	public void setUserCollegeId(String userCollegeId) {
		this.userCollegeId = userCollegeId;
	}
	public String getUserCollegeName() {
		return userCollegeName;
	}
	public void setUserCollegeName(String userCollegeName) {
		this.userCollegeName = userCollegeName;
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

	public String toString() {
		return "User [userCollegeId=" + userCollegeId + ", userStudentId="
				+ userStudentId + ", userPassword=" + userPassword + "]";
	}
	
}
