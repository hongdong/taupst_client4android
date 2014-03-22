package com.example.taupstairs.string;

public class IntentString {

	public static class RequestCode {
		public static final int LOGIN_SELECTCOLLEGE = 1;
		public static final int TASKACTIVITY_WRITE = 2;
		public static final int TASKACTIVITY_TASKDETAIL = 3;
		public static final int WRITE_SELECTENDTIME = 4;
		public static final int IMAGE_REQUEST_CODE = 5;
		public static final int CAMERA_REQUEST_CODE = 6;
		public static final int PHOTO_REQUEST_CODE = 7;
		public static final int MEACTIVITY_UPDATAUSERDATABASE = 8;
		public static final int TASK_SIGNUP = 10;
		public static final int INFOSIGNUP_INFOSIGNUPEXEC = 11;
		public static final int SIGNUPLIST_EVALUATE = 13;
		public static final int MYRELEASESTATUS_TASKDETAIL = 14;
		public static final int MYSIGNUPSTATUS_TASKDETAIL = 15;
		public static final int INFOSIGNUP_TASKBYID = 16;
		public static final int END_SIGNUPLIST = 17;
		public static final int SIGNUPLIST_INFODETAIL = 18;
	}
	
	public static class ResultCode {
		public static final int SELECTCOLLEGE_LOGIN = 1;
		public static final int WRITE_TASKACTIVITY = 2;
		public static final int TASKDETAIL_TASKACTIVITY = 3;
		public static final int SELECTENDTIME_WRITE = 4;
		public static final int UPDATAUSERDATABASE_MEACTIVITY_NICKNAME = 8;
		public static final int UPDATAUSERDATABASE_MEACTIVITY_SIGNATURE = 9;
		public static final int SIGNUP_TASK = 10;
		public static final int INFOSIGNUPEXEC_INFOSIGNUP = 11;
		public static final int EVALUATE_SIGNUPLIST = 13;
		public static final int TASKDETAIL_MYRELEASESTATUS = 14;
		public static final int TASKDETAIL_MYSIGNUPSTATUS = 15;
		public static final int TASKBYID_INFOSIGNUP = 16;
		public static final int SIGNUPLIST_END = 17;
		public static final int INFODETAIL_SIGNUPLIST = 18;
	}
	
	public static class Extra {
		public static final String TYPE = "type";
		public static final String CONTENT = "content";
	}
	
}
