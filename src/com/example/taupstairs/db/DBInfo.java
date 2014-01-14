package com.example.taupstairs.db;

public class DBInfo {

	public static class DB {
		public static final String DB_NAME = "taupstairs";
		public static final int DB_VERSION = 1;
	}
	
	public static class Table {
		public static final String COLLEGE_TB_NAME = "college";
		public static final String COLLEGE_CREATE_STRING = "create table if not exists " + COLLEGE_TB_NAME + 
				" (_id integer primary key autoincrement, collegeId, collegeName, collegeCaptchaUrl)";
		public static final String COLLEGE_DROP_STRING = "drop table " + COLLEGE_TB_NAME;
		
		public static final String PERSON_TB_NAME = "person";
		public static final String PERSON_CREATE_STRING = "create table if not exists " + PERSON_TB_NAME + 
				" (_id integer primary key autoincrement, personId, personPhotoUrl, personNickname, " +
				" personSignature, personQq, personEmail, personPhone, personFaculty, personYear, " +
				" personSpecialty, personName, personSex)";
		public static final String PERSON_DROP_STRING = "drop table " + PERSON_TB_NAME;
		
		public static final String STATUS_TB_NAME = "status";
		public static final String STATUS_CREATE_STRING = "create table if not exists " + STATUS_TB_NAME + 
				" (_id integer primary key autoincrement, statusId, statusTitle, statusContent, " + 
				" statusReleaseTime, statusEndTime, statusRewards, statusMessageCount, statusSignUpCount, " +
				" personId, personPhotoUrl, personNickname, personDepartment, personGrade, personSex)";
		public static final String STATUS_DROP_STRING = "drop table " + STATUS_TB_NAME;
		
		public static final String RANK_TB_NAME = "rank";
		public static final String RANK_CREATE_STRING = "create table if not exists " + RANK_TB_NAME + 
				" (_id integer primary key autoincrement, rankId, personId, personPhotoUrl, " + 
				" personNickname, personSex, rankPraise, rankRank) ";
		public static final String RANK_DROP_STRING = "drop table " + RANK_TB_NAME;
	}
}
