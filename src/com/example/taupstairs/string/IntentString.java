package com.example.taupstairs.string;

public class IntentString {

	public static class RequestCode {
		public static final int LOGIN_SELECTCOLLEGE = 1;
		public static final int HOMEPAGE_WRITE = 2;
		public static final int TASKFRAGMENT_TASKDETAIL = 3;
		public static final int WRITE_SELECTENDTIME = 4;
		public static final int IMAGE_REQUEST_CODE = 5;
		public static final int CAMERA_REQUEST_CODE = 6;
		public static final int PHOTO_REQUEST_CODE = 7;
		public static final int MEFRAGMENT_UPDATAUSERDATABASE = 8;
		public static final int TASKDETAIL_SIGNUP = 10;
	}
	
	public static class ResultCode {
		public static final int SELECTCOLLEGE_LOGIN = 1;
		public static final int WRITE_HOMEPAGE = 2;
		public static final int TASKDETAIL_TASKFRAGMENT = 3;
		public static final int SELECTENDTIME_WRITE = 4;
		public static final int UPDATAUSERDATABASE_MEFRAGMENT_NICKNAME = 8;
		public static final int UPDATAUSERDATABASE_MEFRAGMENT_SIGNATURE = 9;
		public static final int SIGNUP_TASKDETAIL = 10;
	}
	
	public static class Extra {
		public static final String TYPE = "type";
		public static final String CONTENT = "content";
	}
}
