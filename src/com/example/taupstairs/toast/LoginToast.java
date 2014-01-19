package com.example.taupstairs.toast;

import android.content.Context;
import android.widget.Toast;

public class LoginToast {

	public static void testState(Context context, int state) {
		switch (state) {
		case 1:Toast.makeText(context, "请登录教务系统完成教师评价后在登录", Toast.LENGTH_SHORT).show();break;
		case 2:Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();break;		
		case 3:Toast.makeText(context, "验证码不正确", Toast.LENGTH_SHORT).show();break;		
		case 4:Toast.makeText(context, "用户名不存在或未按照要求参加教学活动", Toast.LENGTH_SHORT).show();break;		
		case 5:Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();break;		
		case 6:Toast.makeText(context, "教务系统奔溃!!", Toast.LENGTH_SHORT).show();break;
		default:Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show();break;
		}
	}
}
