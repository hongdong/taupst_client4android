package com.example.taupstairs.logic;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.example.taupstairs.bean.College;
import com.example.taupstairs.bean.Info;
import com.example.taupstairs.bean.InfoSignUp;
import com.example.taupstairs.bean.MessageContent;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Rank;
import com.example.taupstairs.bean.SignUp;
import com.example.taupstairs.bean.Status;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.bean.User;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.JsonUtil;
import com.example.taupstairs.util.StringUtil;
import com.example.taupstairs.util.UploadToBCS;

public class DoTaskService {
	
	private Context context;
	
	public DoTaskService(Context context) {
		this.context = context;
	}

	public String doCheckUserTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String collegeId = (String) taskParams.get(College.COLLEGE_ID);
		String studentId = (String) taskParams.get(User.USER_STUDENTID);
		String check_user_url = HttpClientUtil.BASE_URL + "data/user/issysn?school=" +
				collegeId + "&student_id=" + studentId;
		try {
			check_user_url = StringUtil.replaceBlank(check_user_url);
			result = HttpClientUtil.getRequest(check_user_url);
		} catch (Exception e) {
			e.printStackTrace();	//如果没有连接网络，就会抛出异常，result就会为初值TA_NO：no
		}
		return result;
	}
	
	public Drawable doGetCaptchaTask(Task task) {
		Drawable drawable = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String captchaUrl = (String) taskParams.get(Task.TA_GETCAPTCHA_CAPTCHAURL);
		String get_captcha_url = HttpClientUtil.BASE_URL + "image/code/" + captchaUrl;
		try {
			get_captcha_url = StringUtil.replaceBlank(get_captcha_url);
			drawable = HttpClientUtil.getCaptcha(get_captcha_url);
		} catch (Exception e) {
			e.printStackTrace();	//如果没有连接网络，就会抛出异常，result就会为初值TA_NO：no
		}
		return drawable;
	}
	
	/*登录任务*/
	public String doLoginTask(Task task) {
		String result = Task.TA_NO;
		Map<String, Object> taskParams = task.getTaskParams();
		String collegeId = (String) taskParams.get(User.USER_COLLEGEID);
		String studentId = (String) taskParams.get(User.USER_STUDENTID);
		String password = (String) taskParams.get(User.USER_PASSWORD);
		String isExist = (String) taskParams.get(JsonString.Login.IS_EXIST);
		String captcha = (String) taskParams.get(Task.TA_LOGIN_CAPTCHA);
		String login_url = HttpClientUtil.BASE_URL + "data/user/login?student_id=" + studentId 
				+ "&pwd=" + password + "&school=" + collegeId + "&issysn=" + isExist;
		if (captcha != null) {
			login_url += "&code=" + captcha;
		}
		try {
			login_url = StringUtil.replaceBlank(login_url);
			result = HttpClientUtil.getRequest(login_url);
		} catch (Exception e) {
			e.printStackTrace();	//如果没有连接网络，就会抛出异常，result就会为初值TA_NO：no
		}
		return result;
	}
	
	public void doPushTask(Task task) {
		Map<String, Object> taskParams = task.getTaskParams();
		String channelid = (String) taskParams.get(Task.TA_PUSH_CHANNEL_ID);
		String userid = (String) taskParams.get(Task.TA_PUSH_USER_ID);
		String push_url = HttpClientUtil.BASE_URL + "data/pull/save?user_id=" + userid +
				"&channel_id=" + channelid;
		try {
			HttpClientUtil.getRequest(push_url);
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}
	
	/*获取Person信息*/
	public Person doGetUserDataTask(Task task) {
		Person person = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String personId = (String) taskParams.get(Task.TA_GETUSERDATA_TASKPARAMS);
		String getuserdata_url = HttpClientUtil.BASE_URL + "data/user/userInfo?users_id=" + personId;
		try {
			String jsonString = HttpClientUtil.getRequest(getuserdata_url);
			person = JsonUtil.getPerson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return person;		//此处如果未连接网络的话，返回的是null
	}
	
	/*
	 * 获取List<Status>（任务）
	 * 分为三种，分别是第一次加载，下拉刷新，上拉加载更多
	 */
	public List<Status> doGetStatusTask(Task task) {
		List<Status> listStatus = null;
		String getstatus_url = null;
		Map<String, Object> taskParams = task.getTaskParams();
		int type = (Integer) taskParams.get(Task.TA_GETSTATUS_TYPE);
		int mode = (Integer) taskParams.get(Task.TA_GETSTATUS_MODE);
		String statusId = null, personId = null;
		switch (mode) {
		case Task.TA_GETSTATUS_MODE_FIRSTTIME:
			getstatus_url = HttpClientUtil.BASE_URL + "data/task/taskList2Down";
			break;
			
		case Task.TA_GETSTATUS_MODE_PULLREFRESH:
			statusId = (String) taskParams.get(Status.STATUS_ID);
			getstatus_url = HttpClientUtil.BASE_URL + "data/task/taskList2Down?task_id=" + statusId;
			break;
			
		case Task.TA_GETSTATUS_MODE_LOADMORE:
			statusId = (String) taskParams.get(Status.STATUS_ID);
			getstatus_url = HttpClientUtil.BASE_URL + "data/task/taskList2Up?task_id=" + statusId;
			break;

		default:
			break;
		}
		
		switch (type) {
		case Task.TA_GETSTATUS_TYPE_ALL:
			
			break;
			
		case Task.TA_GETSTATUS_TYPE_MY_RELEASE:
			personId = (String) taskParams.get(Person.PERSON_ID);
			if (Task.TA_GETSTATUS_MODE_FIRSTTIME == mode) {
				getstatus_url += "?my=" + personId;
			} else {
				getstatus_url += "&my=" + personId;
			}
			break;
			
		case Task.TA_GETSTATUS_TYPE_MY_SIGNUP:
			personId = (String) taskParams.get(Person.PERSON_ID);
			if (Task.TA_GETSTATUS_MODE_FIRSTTIME == mode) {
				getstatus_url += "?sign=" + personId;
			} else {
				getstatus_url += "&sign=" + personId;
			}
			break;	

		default:
			break;
		}
		
		try {
			String jsonString = HttpClientUtil.getRequest(getstatus_url);
			/*如果数组长度为0，则链表长度为0，但他不为空，因为在里面已经new了，
			 * 所以有联网状态下，没有更新与没有更多的时候，0 == listStatus.size() 成立，但listStatus不为空*/
			listStatus = JsonUtil.getListStatus(jsonString);
		} catch (Exception e) {
			/*没网络，会返回null*/
			e.printStackTrace();
		}
		return listStatus;
	}
	
	/*
	 * 发布任务
	 */
	public String doReleaseTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String title = (String) taskParams.get(Status.STATUS_TITLE);
		String content = (String) taskParams.get(Status.STATUS_CONTENT);
		String rewards = (String) taskParams.get(Status.STATUS_REWARDS);
		String endtime = (String) taskParams.get(Status.STATUS_ENDTIME);	
		String release_url = HttpClientUtil.BASE_URL + "data/task/save?title=" + title
				+ "&content=" + content + "&rewards=" + rewards + "&end_of_time=" + endtime + "&task_level=1";
		try {
			release_url = StringUtil.replaceBlank(release_url);
			result = HttpClientUtil.getRequest(release_url);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return result;

	}
	
	/*
	 * 更新用户数据
	 */
	public String doUpdataUserdata(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String url = (String) taskParams.get(Task.TA_UPDATAUSERDATA_URL);
		String updata_userdata_url = HttpClientUtil.BASE_URL + "data/user/update?" + url;
		updata_userdata_url = StringUtil.replaceBlank(updata_userdata_url);
		try {
			result = HttpClientUtil.getRequest(updata_userdata_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	 * 检测任务状态
	 */
	public String doCheckStatusTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String statusId = (String) taskParams.get(Status.STATUS_ID);
		String check_status_url = HttpClientUtil.BASE_URL + "data/task/checktask?task_id=" + statusId;
		try {
			result = HttpClientUtil.getRequest(check_status_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} 
	
	/*
	 * 获取留言
	 */
	public List<com.example.taupstairs.bean.Message> doGetMessageTask(Task task) {
		List<com.example.taupstairs.bean.Message> messages = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String statusId = (String) taskParams.get(Status.STATUS_ID);
		String get_message_url = HttpClientUtil.BASE_URL + "data/taskmsg/taskMsgList2Down?task_id=" + statusId;
		try {
			get_message_url = StringUtil.replaceBlank(get_message_url);
			String jsonString = HttpClientUtil.getRequest(get_message_url);
			messages = JsonUtil.getMessages(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messages;
	}
	
	/*
	 * 留言
	 */
	public String doMessageTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String statusId = (String) taskParams.get(Status.STATUS_ID);
		String replyId = (String) taskParams.get(MessageContent.REPLY_ID);
		String content = (String) taskParams.get(MessageContent.CONTENT);
		String message_url = HttpClientUtil.BASE_URL + "data/taskmsg/save?task_id=" + statusId 
				+ "&message_content=" + content + "&to_user=" + replyId;
		String mode = (String) taskParams.get(Task.TA_MESSAGE_MODE);
		if (mode.equals(Task.TA_MESSAGE_MODE_ROOT)) {
			
		} else if (mode.equals(Task.TA_MESSAGE_MODE_CHILD)) {
			String messageId = (String) taskParams.get(com.example.taupstairs.bean.Message.MESSAGE_ID);
			message_url += "&root_id=" + messageId;
		} 
		try {
			message_url = StringUtil.replaceBlank(message_url);
			result= HttpClientUtil.getRequest(message_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} 
	
	/*
	 * 报名
	 */
	public String doSignupTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String statusId = (String) taskParams.get(Status.STATUS_ID);
		String personId = (String) taskParams.get(Status.PERSON_ID);
		String contact = (String) taskParams.get(Task.TA_SIGNUP_CONTACT);
		String message = (String) taskParams.get(Task.TA_SIGNUP_MESSAGE);
		String signup_url = HttpClientUtil.BASE_URL + "data/sign/save?task_id=" + statusId + 
				"&task_user=" + personId + "&open_mes=" + contact + "&message=" + message;
		try {
			signup_url = StringUtil.replaceBlank(signup_url);
			result = HttpClientUtil.getRequest(signup_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Rank> doGetRankTask(Task task) {
		List<Rank> ranks = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String mode = (String) taskParams.get(Task.TA_GETRANK_MODE);
		String get_rank_url = HttpClientUtil.BASE_URL + "data/ranking/list?type=" + mode;
		try {
			String jsonString = HttpClientUtil.getRequest(get_rank_url);
			ranks = JsonUtil.getRanks(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ranks;
	}
	
	/*
	 * 上传照片
	 */
	public String doUploadPhotoTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		Bitmap bitmap = (Bitmap) taskParams.get(Task.TA_UPLOADPHOTO_BITMAP);
		try {	/*先把图片写到cache里面，再读出来以流的方式上传*/
			File file = context.getFilesDir();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			final String fileName = System.currentTimeMillis() + ".jpeg";
			File imageFile = new File(file, fileName);
			FileOutputStream fstream = new FileOutputStream(imageFile);
			BufferedOutputStream bStream = new BufferedOutputStream(fstream);
			bStream.write(byteArray);
			if (bStream != null) {
				bStream.close();
			}
			UploadToBCS ub = new UploadToBCS();
			File f = new File(file.getAbsolutePath() + "/" + fileName);
			ub.putObjectByInputStream(f, "/" + fileName);
			result = fileName;						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Info> doGetInfoTask(Task task) {
		List<Info> infos = null;
		String getinfo_url = null;
		Map<String, Object> taskParams = task.getTaskParams();
		int mode = (Integer) taskParams.get(Task.TA_GETINFO_MODE);
		String infoId = null;
		switch (mode) {
		case Task.TA_GETINFO_MODE_FIRSTTIME:
			getinfo_url = HttpClientUtil.BASE_URL + "data/news/newsList2Down";
			break;
			
		case Task.TA_GETINFO_MODE_PULLREFRESH:
			infoId = (String) taskParams.get(Task.TA_GETINFO_INFOID);
			getinfo_url = HttpClientUtil.BASE_URL + "data/news/newsList2Down?news_id=" + infoId;
			break;
			
		case Task.TA_GETINFO_MODE_LOADMORE:
			infoId = (String) taskParams.get(Task.TA_GETINFO_INFOID);
			getinfo_url = HttpClientUtil.BASE_URL + "data/news/newsList2Up?news_id=" + infoId;
			break;

		default:
			break;
		}
		try {
			String jsonString = HttpClientUtil.getRequest(getinfo_url);
			infos = JsonUtil.getInfos(jsonString);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}
	
	public Object doGetInfoDetailTask(Task task) {
		Object result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String infoSource = (String) taskParams.get(Info.INFO_SOURCE);
		String infoType = (String) taskParams.get(Info.INFO_TYPE);
		String getinfo_detail_url = HttpClientUtil.BASE_URL + 
				"data/news/detail?source=" + infoSource + "&type=" + infoType;
		try {
			String jsonString = HttpClientUtil.getRequest(getinfo_detail_url);
			int type = Integer.parseInt(infoType);
			result = JsonUtil.getInfoDetail(type, jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String doExecTaskTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String signUpId = (String) taskParams.get(InfoSignUp.SIGNUP_ID);
		String signUpString = (String) taskParams.get(InfoSignUp.SIGNUP_STRING);
		String exec_task_url = HttpClientUtil.BASE_URL + 
				"data/sign/ce?sign_id=" + signUpId + "&reply=" + signUpString;
		try {
			exec_task_url = StringUtil.replaceBlank(exec_task_url);
			result = HttpClientUtil.getRequest(exec_task_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Status doGetTaskDetail(Task task) {
		Status status = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String statusId = (String) taskParams.get(Status.STATUS_ID);
		String get_task_url = HttpClientUtil.BASE_URL + "data/task/info?task_id=" + statusId;
		try {
			String jsonString = HttpClientUtil.getRequest(get_task_url);
			status = JsonUtil.getStatus(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	public String doEndTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String statusId = (String) taskParams.get(Status.STATUS_ID);
		String end_task_url = HttpClientUtil.BASE_URL + "data/task/finish?task_id=" + statusId;
		try {
			result = HttpClientUtil.getRequest(end_task_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<SignUp> doGetSignUpListTask(Task task) {
		List<SignUp> signUps = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String statusId = (String) taskParams.get(Status.STATUS_ID);
		String get_signup_list_url = HttpClientUtil.BASE_URL + "data/sign/signlist?task_id=" + statusId;
		try {
			String jsonString = HttpClientUtil.getRequest(get_signup_list_url);
			signUps = JsonUtil.getSignUps(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signUps;
	}
	
	public String doEvaluateTask(Task task) {
		String result = null;
		Map<String, Object> taskParams = task.getTaskParams();
		String statusId = (String) taskParams.get(Status.STATUS_ID);
		String signupId = (String) taskParams.get(SignUp.SIGNUP_ID);
		String personId = (String) taskParams.get(SignUp.PERSON_ID);
		String signupPraise = (String) taskParams.get(SignUp.SIGNUP_PRAISE);
		String signupMessage = (String) taskParams.get(SignUp.SIGNUP_MESSAGE);
		String evaluate_url = HttpClientUtil.BASE_URL + "data/task/apprise?task_id=" + statusId
				+ "&sign_id=" + signupId + "&users_id=" + personId 
				+ "&prise=" + signupPraise + "&msg=" + signupMessage;
		try {
			evaluate_url = StringUtil.replaceBlank(evaluate_url);
			result = HttpClientUtil.getRequest(evaluate_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} 
	
	/*
	 * 用户注销
	 */
	public void doUserExit(Task task) {
		Map<String, Object> taskParams = task.getTaskParams();
		int type = (Integer) taskParams.get(Task.TA_USEREXIT_TYPE);
		String userexit_url = HttpClientUtil.BASE_URL + "data/user/exit";
		switch (type) {
		case Task.TA_USEREXIT_TYPE_NORMAL:
			userexit_url += "?type=1";
			break;
			
		case Task.TA_USEREXIT_TYPE_CHANGE:
			
			break;

		default:
			break;
		}
		try {
			HttpClientUtil.getRequest(userexit_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
