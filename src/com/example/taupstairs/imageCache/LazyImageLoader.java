package com.example.taupstairs.imageCache;

import java.lang.Thread.State;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.taupstairs.logic.TaUpstairsApplication;

public class LazyImageLoader {

	private static final int MESSAGE_WHAT = 1;
	private static final String URL = "url";
	private static final String IMAGE = "image";
	
	private ImageManager imageManager = new ImageManager(TaUpstairsApplication.context);
	
	private BlockingQueue<String> urlQueue = new ArrayBlockingQueue<String>(50);
	
	private DownloadImageThread downloadImageThread = new DownloadImageThread();
	
	private CallbackManager callbackManager = new CallbackManager();
	
	/*
	 * 得到图片。如果缓存（先从map缓存中找，找不到再到文件中找）中有，就拿出来，
	 * 如果没有，就从网络上获取
	 */
	public Drawable get(String url, ImageLoaderCallback callback) {
		Drawable drawable = imageManager.getFromCache(url);
		if (null == drawable) {
			/*开启线程前要把回调函数放进去，不然Handle里面就没东西回调了
			 * 回调是和url对应的，Handle里面也是根据url找到回调链表再回调的*/
			callbackManager.put(url, callback);
			startDownloadThread(url);
		}
		return drawable;
	}
	
	/*
	 * 从网络下载图片。如果有新的可用线程，就用；如果没有，就new一个出来
	 */
	private void startDownloadThread(String url) {
		/*要记得把url放到队列里面去，线程就是从里面找出url去下载的*/
		putUrlToQueue(url);
		State state = downloadImageThread.getState();
		/*如果线程中止了才再开线程。只开一个线程去下载图片
		 * 用线程池去下载的话，到时候再看看去做吧
		 * http://developer.android.com/reference/java/lang/Thread.State.html*/
		if (State.NEW == state) {
			downloadImageThread.start();
		} else if (State.TERMINATED == state) {
			downloadImageThread = new DownloadImageThread();
			downloadImageThread.start();
		} /*else if 正在运行，就不start也不new了，就让它运行*/
	}
	
	/*
	 * 把url放到队列里面去
	 * 如果队列里面已经有url了，就不要放进去了。
	 * 这样一张图片多次使用的时候，只会下载一份，
	 * 到时候调用这个url对应的callback链表去更新，
	 * 把所有需要图片的地方都更新一下。这样提高了效率
	 */
	private void putUrlToQueue(String url) {
		if (!urlQueue.contains(url)) {
			try {
				urlQueue.put(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 线程异步执行完后要把下载到的东西传过来
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_WHAT:
				Bundle bundle = msg.getData();
				String url = bundle.getString(URL);
				Bitmap bitmap = bundle.getParcelable(IMAGE);
				/*http://blog.csdn.net/zhtsuc/article/details/5839913，第一个参数不加的api已经过时了*/
				Drawable drawable = new BitmapDrawable(TaUpstairsApplication.context.getResources(), bitmap);
				/*去回调显示图片，说白了就是更新前台UI了。只不过比平常麻烦，要转几次弯*/
				callbackManager.doCallback(url, drawable);
				break;

			default:
				break;
			}
		};
	};
	
	/*
	 * 下载线程
	 */
	private class DownloadImageThread extends Thread {
		
		private boolean isRun = true;
		
		@Override
		public void run() {
			try {
				while (isRun) {
					/* 这里的阻塞队列其实没什么用，跟不阻塞的一样。如果用take，就会阻塞。
					 * 用peek把头拿出来用，但不移除*/
					String url = urlQueue.peek();
					if (null == url) {
						break;
					}
					Drawable drawable = imageManager.downloadImage(url);
					/*bundle.putParcelable不能传Drawable，所以就转一下了*/
					if (drawable != null) {
						Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
						Message message = new Message();
						message.what = MESSAGE_WHAT;
						Bundle bundle = message.getData();
						bundle.putString(URL, url);
						bundle.putParcelable(IMAGE, bitmap);
						handler.sendMessage(message);
					}
					/*移除头，因为头url对应的下载已经完成了*/
					urlQueue.remove();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
