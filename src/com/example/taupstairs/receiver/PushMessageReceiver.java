package com.example.taupstairs.receiver;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.baidu.android.pushservice.PushConstants;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.MainService;

/**
 * Push消息处理receiver
 */
public class PushMessageReceiver extends BroadcastReceiver {
	/** TAG to Log */
	public static final String TAG = PushMessageReceiver.class.getSimpleName();

	AlertDialog.Builder builder;

	/**
	 * @param context
	 *            Context
	 * @param intent
	 *            接收的intent
	 */
	@Override
	public void onReceive(final Context context, Intent intent) {

		Log.d(TAG, ">>> Receive intent: \r\n" + intent);

		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			//获取消息内容
			String message = intent.getExtras().getString(
					PushConstants.EXTRA_PUSH_MESSAGE_STRING);

			//消息的用户自定义内容读取方式
			Log.i(TAG, "onMessage: " + message);
			
			//自定义内容的json串
        	Log.d(TAG, "EXTRA_EXTRA = " + intent.getStringExtra(PushConstants.EXTRA_EXTRA));
			
			//用户在此自定义处理消息,以下代码为demo界面展示用
        	
		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
			//处理绑定等方法的返回数据
			//PushManager.startWork()的返回值通过PushConstants.METHOD_BIND得到
			
			//获取方法
//			final String method = intent
//					.getStringExtra(PushConstants.EXTRA_METHOD);
			//方法返回错误码。若绑定返回错误（非0），则应用将不能正常接收消息。
			//绑定失败的原因有多种，如网络原因，或access token过期。
			//请不要在出错时进行简单的startWork调用，这有可能导致死循环。
			//可以通过限制重试次数，或者在其他时机重新调用来解决。
//			int errorCode = intent
//					.getIntExtra(PushConstants.EXTRA_ERROR_CODE,
//							PushConstants.ERROR_SUCCESS);
			String content = "";
			if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
				//返回内容
				content = new String(
					intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
				try {
					JSONObject jsonContent = new JSONObject(content);
					JSONObject params = jsonContent.getJSONObject("response_params");
//					String appid = params.getString("appid");
					String channelid = params.getString("channel_id");
					String userid = params.getString("user_id");
					HashMap<String, Object> taskParams = new HashMap<String, Object>(2);
					taskParams.put(Task.TA_PUSH_CHANNEL_ID, channelid);
					taskParams.put(Task.TA_PUSH_USER_ID, userid);
					Task task = new Task(Task.TA_PUSH, taskParams);
					MainService.addTask(task);
				} catch (JSONException e) {
					
				}
			}
			
			//用户在此自定义处理消息,以下代码为demo界面展示用	
		}
	}

}
