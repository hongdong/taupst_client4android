package com.example.taupstairs.imageCache;

import android.graphics.drawable.Drawable;

/*
 * 前台UI获取图片时要异步，异步里面弄了这个接口
 */
public interface ImageLoaderCallback {

	void refresh(Drawable drawable);
}
