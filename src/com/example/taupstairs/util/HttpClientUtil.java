package com.example.taupstairs.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.graphics.drawable.Drawable;

public class HttpClientUtil {

	private static HttpClient httpClient;
	private static final String SCHEME_NAME = "http";
	public static final String BASE_URL = "http://taupst.duapp.com/";
	public static final String PHOTO_BASE_URL = "http://bcs.duapp.com/taupst/photo/";
	private static final String DEFAULT_CHARSET = HTTP.UTF_8;// httpclient读取内容时使用的字符集，为了读汉字
	public static String cookieString;
	
	/*
	 * 私有的，空的，构造函数。因为用httpclient有很多好处。
	 * 其实我这里是工具类了，其它地方根本不会去new，所以加不加其实无所谓
	 */
	private HttpClientUtil() {
		
	}
	
	/*
	 * 有时其它地方需要直接用到httpClient这个对象，
	 * 而不是下面的静态方法
	 */
	public static synchronized HttpClient getHttpClient() {
		if (null == httpClient) {
			final HttpParams httpParams = new BasicHttpParams();    
	        SchemeRegistry schemeRegistry = new SchemeRegistry();    
	        schemeRegistry.register(new Scheme(SCHEME_NAME, PlainSocketFactory.getSocketFactory(), 80));    
	        ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);    
	        httpClient = new DefaultHttpClient(manager, httpParams);  
		}
		return httpClient;
	}
	
	/*用GET命令发送请求的URL，返回服务器相应的字符串*/
	public static String getRequest(final String url) throws Exception {
		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
			public String call() throws Exception {
				HttpGet get = new HttpGet(url);							//创建HttpGet对象用于GET请求
				HttpClient httpClient = getHttpClient();
				HttpResponse response = httpClient.execute(get);		//发送GET请求
				if (200 == response.getStatusLine().getStatusCode()) {	//code为200表示成功返回
					String result = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
					return result;										//response.getEntity()可获取服务器返回的字符串
				}
				return null;
			}
		});
		new Thread(task).start();										//FutureTask是一个异步线程
		return task.get();
	}
	
	/*用POST命令发送请求的URL，外加一个Map参数返回服务器相应的字符串*/
	public static String postRequest(final String url, final Map<String, String>rawParams) throws Exception {
		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
			public String call() throws Exception {
				HttpPost post = new HttpPost(url);						//创建HttpPost对象用于POST请求
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for (String key : rawParams.keySet()) {					//参数比较多的话可以封装起来
					params.add(new BasicNameValuePair(key, rawParams.get(key)));
				}
				post.setEntity(new UrlEncodedFormEntity(params, DEFAULT_CHARSET));
				HttpClient httpClient = getHttpClient();
				HttpResponse response = httpClient.execute(post);
				if (200 == response.getStatusLine().getStatusCode()) {
					String result = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
					return result;
				}
				return null;
			}
		});
		new Thread(task).start();
		return task.get();
	}
	
	public static Drawable getCollegeCaptcha(String collegeCaptchaUrl) {
		Drawable drawable = null;
		HttpGet get = new HttpGet(collegeCaptchaUrl);
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(get);
			cookieString = response.getFirstHeader("set-Cookie").getValue();
			InputStream is = response.getEntity().getContent();
			drawable = Drawable.createFromStream(is, collegeCaptchaUrl);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return drawable;
	}
	
}
