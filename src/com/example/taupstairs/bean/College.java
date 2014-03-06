package com.example.taupstairs.bean;

public class College {

	private Long id;
	private String collegeId;
	private String collegeName;
	
	public static final String ID = "_id";
	public static final String COLLEGE_ID = "collegeId";
	public static final String COLLEGE_NAME = "collegeName";
	
	public College() {
		
	}

	public College(String collegeId) {
		this.collegeId = collegeId;
	}

	public College(String collegeId, String collegeName) {
		this.collegeId = collegeId;
		this.collegeName = collegeName;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(String collegeId) {
		this.collegeId = collegeId;
	}

	public String getCollegeName() {
		return collegeName;
	}
	
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}
	
}
