package com.example.taupstairs.db;

public class DBInfo {

	public static class DB {
		public static final String DB_NAME = "taupstairs";
		public static final int DB_VERSION = 1;
	}
	
	public static class Table {
		public static final String COLLEGE_TB_NAME = "college";
		public static final String COLLEGE_CREATE_STRING = "create table " + COLLEGE_TB_NAME + 
				" (_id integer primary key autoincrement, collegeName)";
		public static final String COLLEGE_DROP_STRING = "drop table " + COLLEGE_TB_NAME;
	}
}
