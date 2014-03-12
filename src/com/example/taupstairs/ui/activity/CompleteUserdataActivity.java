package com.example.taupstairs.ui.activity;

import java.io.File;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.SdCardUtil;
import com.example.taupstairs.util.SharedPreferencesUtil;

public class CompleteUserdataActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_ok;
	private ImageView img_userphoto;
	private EditText edit_nickname, edit_signatrue, edit_qq, edit_email, edit_phone;
	private String userId, photo, nickname, signatrue, qq, email, phone;
	private static final String IMAGE_FILE_NAME = "userPhoto.png";
	private static String[] items = new String[] { "选择本地图片", "拍照" };
	private boolean flag_img;
	private Bitmap userPhoto;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complete_userdata);
		MainService.addActivity(CompleteUserdataActivity.this);
		init();
	}
	
	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		flag_img = false;
		userId = SharedPreferencesUtil.getDefaultUser(this).getUserId();
	}
	
	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back_complete_userdata);
		btn_ok = (Button) findViewById(R.id.btn_ok_complete_userdata);
		img_userphoto = (ImageView) findViewById(R.id.img_userphoto);
		edit_nickname = (EditText) findViewById(R.id.edit_complete_userdata_nickname);
		edit_signatrue = (EditText) findViewById(R.id.edit_complete_userdata_signatrue);
		edit_qq = (EditText) findViewById(R.id.edit_complete_userdata_qq);
		edit_email = (EditText) findViewById(R.id.edit_complete_userdata_email);
		edit_phone = (EditText) findViewById(R.id.edit_complete_userdata_phone);
		progressDialog = new ProgressDialog(this);
		
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpToHomePage();
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updataUserData();
			}
		});
		img_userphoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
	}
	
	private void showDialog() {
		new AlertDialog.Builder(CompleteUserdataActivity.this)
		.setTitle("设置头像")
		.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					Intent intentFromGallery = new Intent();
					intentFromGallery.setType("image/*"); // 设置文件类型
					intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intentFromGallery, IntentString.RequestCode.IMAGE_REQUEST_CODE);
					break;
				case 1:
					// 判断存储卡是否可以用，可用进行存储
					if (SdCardUtil.hasSdcard()) {
						File fileName = new File(
		                		Environment.getExternalStorageDirectory().getAbsolutePath(), IMAGE_FILE_NAME);
						Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, fileName);
						startActivityForResult(intentFromCapture, IntentString.RequestCode.CAMERA_REQUEST_CODE);
					} else {
						Toast.makeText(CompleteUserdataActivity.this, "未找到存储卡，无法存储照片！",
								Toast.LENGTH_LONG).show();
					}
					break;
				}
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}
	
	/*
	 * 调用系统的裁剪图片的Intent
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, IntentString.RequestCode.PHOTO_REQUEST_CODE);
	}
	
	private void getEditString() {
		nickname = edit_nickname.getText().toString().trim();
		signatrue = edit_signatrue.getText().toString().trim(); 
		qq = edit_qq.getText().toString().trim();
		email = edit_email.getText().toString().trim();
		phone = edit_phone.getText().toString().trim();
	}
	
	private void showProgressDialog() {
		progressDialog.setCancelable(false);
		progressDialog.setMessage("    稍等片刻...");
		progressDialog.show();
	}
	
	private void dismissProgressDialog() {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	
	/*
	 * 更新用户数据。先上传头像，ok后再把头像地址和其它一起上传
	 */
	private void updataUserData() {
		getEditString();
		if (flag_img) {			
			doUpdataUserPhotoTask();
		} else {			
			doUpdataUserDataTask();
		}
	}
	
	/*
	 * 网络任务
	 */
	private void doUpdataUserDataTask() {
		showProgressDialog();
		String url = "users_id=" + userId;
		if (photo != null) {
			url += "&photo=" + photo;
		}
		if (!nickname.equals("")) {
			url += "&username=" + nickname;
		} 
		if (!signatrue.equals("")) {
			url += "&signature=" + signatrue;
		}
		if (!qq.equals("")) {
			url += "&qq=" + qq;
		}
		if (!email.equals("")) {
			url += "&email=" + email;
		}
		if (!phone.equals("")) {
			url += "&phone=" + phone;
		}
		HashMap<String, Object> taskParams = new HashMap<String, Object>(2);
		taskParams.put(Task.TA_UPDATAUSERDATA_ACTIVITY, Task.TA_UPDATAUSERDATA_ACTIVITY_COMPLETE);
		taskParams.put(Task.TA_UPDATAUSERDATA_URL, url);
		Task task = new Task(Task.TA_UPDATAUSERDATA, taskParams);
		MainService.addTask(task);
	}
	
	/*
	 * 上传照片
	 */
	private void doUpdataUserPhotoTask() {
		showProgressDialog();
		HashMap<String, Object> taskParams = new HashMap<String, Object>(2);
		taskParams.put(Task.TA_UPLOADPHOTO_ACTIVITY, Task.TA_UPLOADPHOTO_ACTIVITY_COMPLETE);
		taskParams.put(Task.TA_UPLOADPHOTO_BITMAP, userPhoto);
		Task task = new Task(Task.TA_UPLOADPHOTO, taskParams);
		MainService.addTask(task);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 结果码不等于取消时候
		if (resultCode != Activity.RESULT_CANCELED) {
			switch (requestCode) {
			case IntentString.RequestCode.IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case IntentString.RequestCode.CAMERA_REQUEST_CODE:
				if (SdCardUtil.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory().getAbsolutePath(), IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				}
				break;
			case IntentString.RequestCode.PHOTO_REQUEST_CODE:
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						userPhoto = extras.getParcelable("data");
						img_userphoto.setImageBitmap(userPhoto);
					}
					flag_img = true;
				}
				break;
			}
		}
	}

	@Override
	public void refresh(Object... params) {
		dismissProgressDialog();
		String result = (String) params[1];
		if (result != null) {
			int taskId = (Integer) params[0];
			switch (taskId) {
			case Task.TA_UPDATAUSERDATA:
				try {
					JSONObject jsonObject = new JSONObject(result);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						jumpToHomePage();
					} else {
						Toast.makeText(CompleteUserdataActivity.this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
				
			case Task.TA_UPLOADPHOTO:
				photo = result;
				doUpdataUserDataTask();
				break;
				
			default:
				break;
			}
		} else {
			Toast.makeText(CompleteUserdataActivity.this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void jumpToHomePage() {
		Intent intent = new Intent(CompleteUserdataActivity.this, HomePageActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(CompleteUserdataActivity.this);
	}

}
