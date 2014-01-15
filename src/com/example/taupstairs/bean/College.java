package com.example.taupstairs.bean;

public class College {

	private Long id;
	private String collegeId;
	private String collegeName;
	private String collegeCaptchaUrl;
	private String collegeWeb;
	
	public static final String TB_NAME = "college";
	public static final String VIEWSTATE = "input[name=__VIEWSTATE]";
	
	public static final String ID = "_id";
	public static final String COLLEGE_ID = "collegeId";
	public static final String COLLEGE_NAME = "collegeName";
	public static final String COLLEGE_CAPTCHAURL = "collegeCaptchaUrl";
	public static final String COLLEGE_WEB = "collegeWeb";
	
	public College() {
		
	}

	public College(String collegeId) {
		this.collegeId = collegeId;
	}

	public College(String collegeId, String collegeName) {
		this.collegeId = collegeId;
		this.collegeName = collegeName;
	}

	public College(String collegeId, String collegeName, String collegeCaptchaUrl) {
		this.collegeId = collegeId;
		this.collegeName = collegeName;
		this.collegeCaptchaUrl = collegeCaptchaUrl;
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

	public String getCollegeCaptchaUrl() {
		return collegeCaptchaUrl;
	}

	public void setCollegeCaptchaUrl(String collegeCaptchaUrl) {
		this.collegeCaptchaUrl = collegeCaptchaUrl;
	}

	public String getCollegeWeb() {
		return collegeWeb;
	}
	
	public void setCollegeWeb(String collegeWeb) {
		this.collegeWeb = collegeWeb;
	}
	
}
