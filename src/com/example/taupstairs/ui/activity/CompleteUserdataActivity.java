package com.example.taupstairs.ui.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.bean.Task;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.MainService;
import com.example.taupstairs.services.RankService;
import com.example.taupstairs.services.StatusService;
import com.example.taupstairs.string.IntentString;
import com.example.taupstairs.string.JsonString;
import com.example.taupstairs.util.SdCardUtil;
import com.example.taupstairs.util.SharedPreferencesUtil;
import com.example.taupstairs.util.UploadToBCS;

public class CompleteUserdataActivity extends Activity implements ItaActivity {

	private Button btn_back, btn_ok;
	private ImageView img_userphoto;
	private EditText edit_nickname, edit_signatrue, edit_qq, edit_email, edit_phone;
	private String userId, photo, nickname, signatrue, qq, email, phone;
	private String[] items = new String[] { "选择本地图片", "拍照" };
	private static final int MSG_WHAT_OK = 0x123;
	private static final int MSG_WHAT_NO = 0x456;
	private static final String IMAGE_FILE_NAME = "userPhoto.jpg";
	private boolean flag_img;
	private Bitmap userPhoto;
	private ProgressDialog progressDialog;
	private Intent intent;
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
		intent = getIntent();
		userId = intent.getStringExtra(Person.PERSON_ID);
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
			public void onClick(View v) {
				updataUserData();
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updataUserData();
			}
		});
		img_userphoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog();
			}
		});
	}
	
	/*
	 * 头像上传完后通知主线程
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (MSG_WHAT_OK == msg.what) {
				photo = (String) msg.obj;
				doUpdataUserDataTask();
			} else if (MSG_WHAT_NO == msg.what) {
				progressDialog.dismiss();
				Toast.makeText(CompleteUserdataActivity.this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	/*
	 * 更新用户数据。先上传头像，ok后再把头像地址和其它一起上传
	 */
	private void updataUserData() {
		getEditString();
		progressDialog.setCancelable(false);
		progressDialog.setMessage("    稍等片刻...");
		progressDialog.show();
		if (flag_img) {			
			updataUserPhoto();
		} else {			
			doUpdataUserDataTask();
		}
	}
	
	/*
	 * 网络任务
	 */
	private void doUpdataUserDataTask() {
		String url = "users_id=" + userId;
		if (photo != null) {
			url += "&photo=" + photo;
		}
		if (!nickname.equals("")) {
			url += "&username=" + nickname;
		} else {
			String personNickname = intent.getStringExtra(Person.PERSON_NICKNAME);
			url += "&username=" + personNickname;
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
	
	private void getEditString() {
		nickname = edit_nickname.getText().toString().trim();
		signatrue = edit_signatrue.getText().toString().trim(); 
		qq = edit_qq.getText().toString().trim();
		email = edit_email.getText().toString().trim();
		phone = edit_phone.getText().toString().trim();
	}
	
	/*
	 * 上传照片
	 */
	private void updataUserPhoto() {
		try {	/*先把图片写到cache里面，再读出来以流的方式上传*/
			final File cache = getCacheDir();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			userPhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			final String fileName = System.currentTimeMillis() + ".jpeg";
			File imageFile = new File(cache, fileName);
			FileOutputStream fstream = new FileOutputStream(imageFile);
			BufferedOutputStream bStream = new BufferedOutputStream(fstream);
			bStream.write(byteArray);
			if (bStream != null) {
				bStream.close();
			}

			new Thread(new Runnable() {
				public void run() {
					UploadToBCS ub = new UploadToBCS();
					Message msg = new Message();
					try {
						File f = new File(cache.getAbsolutePath() + "/" + fileName);
						ub.putObjectByInputStream(f, "/" + fileName);
						msg.what = MSG_WHAT_OK;
						msg.obj = "/" + fileName;
					} catch (Exception e) {
						e.printStackTrace();
						msg.what = MSG_WHAT_NO;
					}							
					handler.sendMessage(msg);
				}
			}).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 显示选择对话框
	 */
	private void showDialog() {
		new AlertDialog.Builder(CompleteUserdataActivity.this)
				.setTitle("设置头像")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery, IntentString.RequestCode.IMAGE_REQUEST_CODE);
							break;
						case 1:
							Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (SdCardUtil.hasSdcard()) {
								intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment.getExternalStorageDirectory(), 
												IMAGE_FILE_NAME)));
							}
							startActivityForResult(intentFromCapture, IntentString.RequestCode.CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
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
							Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(CompleteUserdataActivity.this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}
				break;
			case IntentString.RequestCode.PHOTO_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
					flag_img = true;
				}
				break;
			}
		}
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
	
	/*
	 * 显示裁剪后的图片
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			userPhoto = extras.getParcelable("data");
			img_userphoto.setImageBitmap(userPhoto);
		}
	}

	@Override
	public void refresh(Object... params) {
		int taskId = (Integer) params[0];
		switch (taskId) {
		case Task.TA_UPDATAUSERDATA:
			progressDialog.dismiss();
			String result = (String) params[1];
			if (null == result) {
				Toast.makeText(CompleteUserdataActivity.this, "未连接网络", Toast.LENGTH_SHORT).show();
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					String state = jsonObject.getString(JsonString.Return.STATE).trim();
					if (state.equals(JsonString.Return.STATE_OK)) {
						SharedPreferencesUtil.savaLastestStatusId(CompleteUserdataActivity.this, null);
						StatusService statusService = new StatusService(CompleteUserdataActivity.this);
						statusService.emptyStatusDb();
						statusService.closeDBHelper();
						RankService rankService = new RankService(CompleteUserdataActivity.this);
						rankService.emptyRankDb();
						rankService.closeDBHelper();
						Intent intent = new Intent(CompleteUserdataActivity.this, HomePageActivity.class);
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(CompleteUserdataActivity.this, "网络竟然出错了", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}	
			break;
			
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(CompleteUserdataActivity.this);
	}

}
