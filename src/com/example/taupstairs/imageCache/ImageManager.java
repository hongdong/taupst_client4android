package com.example.taupstairs.imageCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import com.example.taupstairs.util.HttpClientUtil;
import com.example.taupstairs.util.MD5Util;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class ImageManager {

	private Context context;
	private Map<String, SoftReference<Drawable>> imageCache;
	
	public ImageManager(Context context) {
		this.context = context;
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}
	
	/*
	 * 判断缓存中是否含有这个url对应的图片
	 */
	public boolean contains(String url) {
		return imageCache.containsKey(url);
	}
	
	/*
	 * 获取图片，如果缓存中没有的话，就到存储器里面去获取
	 */
	public Drawable getFromCache(String url) {
		Drawable drawable = null;
		drawable = getFromMapCache(url);
		if (null == drawable) {				//如果map缓存中没有的话，就从文件中找找看
			drawable = getFromFile(url);
		}
		return drawable;
	}
	
	/*
	 * 从Map缓存中获取图片
	 */
	public Drawable getFromMapCache(String url) {
		Drawable drawable = null;
		SoftReference<Drawable> reference = null;
		synchronized (this) {
			reference = imageCache.get(url);
		}
		if (reference != null) {
			drawable = reference.get();
		}
		return drawable;
	}
	
	/*
	 * 从存储器上面获取图片
	 */
	public Drawable getFromFile(String url) {
		Drawable drawable = null;
		FileInputStream is = null;
		String fileName = getMd5(url);
		try {
			is = context.openFileInput(fileName);
			drawable = Drawable.createFromStream(is, fileName);
		} catch (Exception e) {
			/*这里经常会找不到，所以就不要打印了，直接返回null*/
			return null;
		}
		
		finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return drawable;
	}
	
	/*
	 * 从网络上下载图片，保存到本地，然后再从本地读出图片（也将他保存到内存中）返回
	 */
	public Drawable downloadImage(String url) {
		Drawable drawable = null;
		HttpGet get = new HttpGet(url);
		try {
			HttpClient httpClient = HttpClientUtil.getHttpClient();
			HttpResponse response = httpClient.execute(get);
			InputStream is = response.getEntity().getContent();
			/*把图片加到内存的缓存中*/
			imageCache.put(url, new SoftReference<Drawable>(drawable));
			/*先保存，再读出。此处保存一定要将url转为可用的文件名，不然会异常*/
			String fileName = getMd5(url);
			writeImageToFile(fileName, is);
			is = context.openFileInput(fileName);
			drawable = Drawable.createFromStream(is, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return drawable;
	}
	
	/*
	 * 将图片写到文件里面去
	 * 以后可能改成保存到sdcard中去，毕竟手机本身存储器容量有限
	 */
	private void writeImageToFile(String fileName, InputStream is) {
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = null;
		try {
			OutputStream os = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			bos = new BufferedOutputStream(os);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*最后的清理工作*/
		finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.flush();
					bos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 因为文件名不能用：这样的符号，网址不能直接当文件名，所以要转换
	 */
	private String getMd5(String url) {
		return MD5Util.getMD5String(url);
	}
	
}
