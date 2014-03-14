package com.example.taupstairs.ui.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.taupstairs.R;
import com.example.taupstairs.bean.Person;
import com.example.taupstairs.imageCache.SimpleImageLoader;
import com.example.taupstairs.logic.ItaActivity;
import com.example.taupstairs.logic.TaUpstairsApplication;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.SdCardUtil;

public class LargePhotoActivity extends Activity implements ItaActivity {

	private String photoUrl;
	private ImageView imageView;
	private Drawable drawable;
	private ProgressBar progressBar;
	private final int DOWNLOAD_SUCCESS = 1;
	private final int DOWNLOAD_ERROR = 2;
	private final int SAVE_PHOTO = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.large_photo);
		init();
	}
	
	@Override
	public void init() {
		initData();
		initView();
	}
	
	private void initData() {
		photoUrl = getIntent().getStringExtra(Person.PERSON_PHOTOURL);
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DOWNLOAD_SUCCESS:
				progressBar.setVisibility(View.GONE);
				imageView.setImageDrawable(drawable);
				registerForContextMenu(imageView);
				break;
			case DOWNLOAD_ERROR:
				progressBar.setVisibility(View.GONE);
				AlertDialog.Builder builder = new AlertDialog.Builder(LargePhotoActivity.this);  
				builder.setTitle("提示");  
                builder.setMessage("靠！下载原图失败了");  
                builder.setPositiveButton("确定", new OnClickListener() {   
					public void onClick(DialogInterface dialog, int which) {  
                        dialog.dismiss();  
                    }  
                });
                builder.create().show();
                registerForContextMenu(imageView);
				break;
			}
		};
	};
	
	private void initView() {
		imageView = (ImageView) findViewById(R.id.img_large_photo);
		progressBar = (ProgressBar) findViewById(R.id.pb_large_photo);
		
		SimpleImageLoader.showImage(imageView, HttpClientUtil.PHOTO_BASE_URL + photoUrl);
		imageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();	//单击退出（如果注册了上下文菜单，刚上下文菜单会优先。为什么我也不去管，反正是这样）
			}
		});
		
		boolean cache = false;
		TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
		Map<String, SoftReference<Drawable>> map = app.getMap();
		if (map != null) {
			SoftReference<Drawable> reference = null;
			synchronized (this) {
				reference = map.get(photoUrl);
			}
			if (reference != null) {
				drawable = reference.get();
				if (drawable != null) {
					cache = true;
					progressBar.setVisibility(View.GONE);
					imageView.setImageDrawable(drawable);
					registerForContextMenu(imageView);
				}
			}
		}
		if (!cache) {
			new Thread() {
				public void run() {
					try {
						HttpGet get = new HttpGet(HttpClientUtil.PHOTO_BASE_URL + "l" + photoUrl);
						HttpClient httpClient = new DefaultHttpClient();
						HttpResponse response = httpClient.execute(get);
						if (200 == response.getStatusLine().getStatusCode()) {
							InputStream is = response.getEntity().getContent();
							drawable = Drawable.createFromStream(is, null);
							
							Map<String, SoftReference<Drawable>> map = 
									new HashMap<String, SoftReference<Drawable>>();
							map.put(photoUrl, new SoftReference<Drawable>(drawable));
							TaUpstairsApplication app = (TaUpstairsApplication) getApplication();
							app.setMap(map);
							
							handler.sendEmptyMessage(DOWNLOAD_SUCCESS);
						} else {
							handler.sendEmptyMessage(DOWNLOAD_ERROR);
						}
					} catch (Exception e) {
						handler.sendEmptyMessage(DOWNLOAD_ERROR);
					}
				}
			}.start();
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, SAVE_PHOTO, 0, "保存到SD卡");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SAVE_PHOTO:
			if (!SdCardUtil.hasSdcard()) {  
                //如果没有SD卡  
            	AlertDialog.Builder builder = new AlertDialog.Builder(LargePhotoActivity.this);  
                builder.setTitle("提示");  
                builder.setMessage("当前设备无SD卡，数据无法保存");  
                builder.setPositiveButton("确定", new OnClickListener() {   
					public void onClick(DialogInterface dialog, int which) {  
                        dialog.dismiss();  
                    }  
                });
                builder.create().show();  
            } else {  
            	Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            	File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() 
                		+ "/taupstairs/");  
                if(!file.exists()){  
                    file.mkdir();  
                }  
                String filename = Environment.getExternalStorageDirectory().getAbsolutePath() 
                		+ "/taupstairs/" + photoUrl;
                FileOutputStream out = null;
                try {
	                out = new FileOutputStream(filename);
	                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (Exception e) {
                	e.printStackTrace();
                } finally {
	                try {
	                	out.flush();
	                } catch (Exception e) {
	                	e.printStackTrace();
	                }
	                try {
	                	out.close();
	                } catch (Exception e) {
	                	e.printStackTrace();
	                }
	                out=null;
                }
            }
			break;

		default:
			break;
		}
		return true;
	}
	
	@Override
	public void refresh(Object... params) {
		
	}

}
