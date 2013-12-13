package com.example.taupstairs.util;

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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

	public static HttpClient httpClient = new DefaultHttpClient();
	public static final String BASE_URL = "http://hongdong.duapp.com/";
	
	/*用GET命令发送请求的URL，返回服务器相应的字符串*/
	public static String getRequest(final String url) throws Exception {
		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
			public String call() throws Exception {
				HttpGet get = new HttpGet(url);							//创建HttpGet对象用于GET请求
				HttpResponse response = httpClient.execute(get);		//发送GET请求
				if (200 == response.getStatusLine().getStatusCode()) {	//code为200表示成功返回
					String result = EntityUtils.toString(response.getEntity());
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
				post.setEntity(new UrlEncodedFormEntity(params, "taupstairs"));
				HttpResponse response = httpClient.execute(post);
				if (200 == response.getStatusLine().getStatusCode()) {
					String result = EntityUtils.toString(response.getEntity());
					return result;
				}
				return null;
			}
		});
		new Thread(task).start();
		return task.get();
	}
}
