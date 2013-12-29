package com.example.taupstairs.imageCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.drawable.Drawable;

public class CallbackManager {

	/*
	 * 一个url，要对应一个ImageLoaderCallback的链表，就是一群的callback，
	 * 为什么要这样？因为可能多个地方要用到这个图片，那么只要从网络下载一次图片，
	 * 然后需要这张图片的地方都更新一下，就ok了
	 */
	private ConcurrentHashMap<String, List<ImageLoaderCallback>> concurrentHashMap;
	
	public CallbackManager() {
		concurrentHashMap = new ConcurrentHashMap<String, List<ImageLoaderCallback>>();
	}
	
	/*
	 * 加入到ConcurrentHashMap，如果已经存在，只要加入到回调链表就行了
	 */
	public void put(String url, ImageLoaderCallback callback) {
		/*这里用contains方法是不行的，那个可能是测试value值的，不是测试key的*/
		if (!concurrentHashMap.containsKey(url)) {
			List<ImageLoaderCallback> callbacks = new ArrayList<ImageLoaderCallback>();
			callbacks.add(callback);
			concurrentHashMap.put(url, callbacks);
		} else {
			concurrentHashMap.get(url).add(callback);
		}
	}
	
	/*
	 * 找出url对应的callback的list链表，链表里面的callback全调一遍
	 */
	public void doCallback(String url, Drawable drawable) {
		List<ImageLoaderCallback> callbacks = concurrentHashMap.get(url);
		if (callbacks != null) {
			for (ImageLoaderCallback callback : callbacks) {
				if (callback != null) {
					callback.refresh(drawable);
				}
			}
			/*清理，避免内存溢出*/
			callbacks.clear();
			concurrentHashMap.remove(url);
		}
	}
}
