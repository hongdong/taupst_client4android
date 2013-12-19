package com.example.taupstairs.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;

import com.example.taupstairs.ui.HomePageActivity;

public class DialogUtil {

	public static void showDialog(Context context, View view) {
		new AlertDialog.Builder(context)
			.setView(view)
			.setCancelable(false)
			.setPositiveButton("确定", null)
			.create()
			.show();
	}
	
	public static void showDialog(final Context context, String msg, boolean goToHomePage) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(msg).setCancelable(false);
		if (goToHomePage) {
			builder.setPositiveButton("确定", new OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, HomePageActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent);
				}
			});
		} else {
			builder.setPositiveButton("确定", null);
		}
		builder.create().show();
	}
}
