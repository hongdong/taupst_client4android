package com.example.taupstairs.imageCache;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.example.taupstairs.app.TaUpstairsApplication;

public class SimpleImageLoader {

	/*
	 * 前台UI要调这个更新UI，以显示图片
	 */
	public static void showImage(ImageView imageView, String url) {
		Drawable drawable = TaUpstairsApplication.lazyImageLoader.get(url, getCallback(imageView));
		/*这里drawable有可能为空。如果缓存里面没有的话，就会为空。
		 * 这个时候就显示一张默认图片。
		 * 它什么时候更新呢？他下载完了，处理完一系列事情后就会调用下面的callback了*/
		if (drawable != null) {
			imageView.setImageDrawable(drawable);
		} 
	}
	
	/*
	 * 生成一个回调函数，refresh里面更新imageView的图片显示
	 */
	private static ImageLoaderCallback getCallback(final ImageView imageView) {
		return new ImageLoaderCallback() {
			public void refresh(Drawable drawable) {
				imageView.setImageDrawable(drawable);
			}
		};
	}
}
