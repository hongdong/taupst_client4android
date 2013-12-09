package com.example.taupstairs.bean;

public class College {

	private Long id;
	private String collegeName;
	public static final String TB_NAME = "college";
	public static final String ID = "_id";
	public static final String COLLEGE_NAME = "collegeName";
	
	public College() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public College(String collegeName) {
		this.collegeName = collegeName;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCollegeName() {
		return collegeName;
	}
	
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}
	
}
