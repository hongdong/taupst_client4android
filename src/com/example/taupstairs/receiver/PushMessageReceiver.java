package com.example.taupstairs.receiver;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.baidu.android.pushservice.PushConstants;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.ui.activity.HomePageActivity;

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
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
        	//目前不发送这种类型的推送
		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
			//处理绑定等方法的返回数据
			if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
				//返回内容
				String content = new String(intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
				try {
					JSONObject jsonContent = new JSONObject(content);
					JSONObject params = jsonContent.getJSONObject("response_params");
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
		} else if (intent.getAction().equals(	//点击事件
				PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
			//目前我们的服务器只发送这类推送
			Intent displayInfoIntent = new Intent(context, HomePageActivity.class);
			displayInfoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(displayInfoIntent);
		}
	}

}
