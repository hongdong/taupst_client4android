package com.example.taupstairs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.model.X_BS_ACL;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;

public class UploadToBCS {
  
	static String bucket = "taupst";
	
  	private String bcshost = "bcs.duapp.com";
	private String username = "A9MIroqTfseTHViNpijkXvV4";
	private String passwd = "2UEyEfQIMb32jnrUgs01rAyuCdqmcnsu";
  	
  	
  	public void putObjectByInputStream(File file,String fileName)
			throws FileNotFoundException {
  		BCSCredentials credentials = new BCSCredentials(username, passwd);
  		BaiduBCS baiduBCS = new BaiduBCS(credentials, bcshost);
		InputStream fileContent = new FileInputStream(file);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType("image/jpg");
		objectMetadata.setContentLength(file.length());
		PutObjectRequest request = new PutObjectRequest(bucket, "/photo"+fileName,
				fileContent, objectMetadata);
		request.setAcl(X_BS_ACL.PublicRead);  
		
		baiduBCS.putObject(request);
	}
}
