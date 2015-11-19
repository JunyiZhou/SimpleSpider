package com.spider.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Spider {

	public Spider() {}
	
	public static void sendReply(String username, String password, String topicId, String content) throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpPost httppostForLogin = new HttpPost(StringUtils.URL_LOGIN);
        List<NameValuePair> paramsForLogin = new ArrayList<NameValuePair>();
        paramsForLogin.add(new BasicNameValuePair("account", username));
        paramsForLogin.add(new BasicNameValuePair("password", new RSA().encrypt(password)));
        paramsForLogin.add(new BasicNameValuePair("remember", "on"));
		httppostForLogin.setEntity(new UrlEncodedFormEntity(paramsForLogin));
        HttpResponse responseForLogin = httpclient.execute(httppostForLogin);
        HttpEntity entityForLogin = responseForLogin.getEntity();
        String htmlStringForLogin = "";
        InputStream htmlInputStreamForLogin = null;      
        if(entityForLogin != null){
            System.out.println("获取html成功");
            htmlInputStreamForLogin = entityForLogin.getContent();
            htmlStringForLogin = StringUtils.InputStreamToString(htmlInputStreamForLogin, "UTF-8");
            System.out.println(htmlStringForLogin);
        }
        
        HttpPost httppostForReply = new HttpPost(String.format(StringUtils.URL_REPLY, topicId));
        List<NameValuePair> paramsForReply = new ArrayList<NameValuePair>();
        paramsForReply.add(new BasicNameValuePair("post_id", topicId));
        paramsForReply.add(new BasicNameValuePair("content", content));
		httppostForReply.setEntity(new UrlEncodedFormEntity(paramsForReply));
        HttpResponse responseForReply = httpclient.execute(httppostForReply);
        HttpEntity entityForReply = responseForReply.getEntity();
        String htmlStringForReply = "";
        InputStream htmlInputStreamForReply = null;
        if(entityForReply != null){
            System.out.println("获取html成功");
            htmlInputStreamForReply = entityForReply.getContent();
            htmlStringForReply = StringUtils.InputStreamToString(htmlInputStreamForReply, "UTF-8");
            System.out.println(htmlStringForReply);
        }
	}
	
	public String getHtmlStringFromWeb(String url) throws IllegalStateException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpHost proxy = new HttpHost("60.15.56.67", 3128);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        String htmlString = "";
        
        InputStream htmlInputStream = null;
        
        if(httpEntity != null){
//            System.out.println("获取html成功");
            htmlInputStream = httpEntity.getContent();
            
            htmlString = StringUtils.InputStreamToString(htmlInputStream, "UTF-8");
//            System.out.println(htmlString);
        }
        
        return htmlString;
	}
	
	public String getHtmlStringFromFile(String filePath, String charSet) throws IllegalStateException, IOException {
		FileInputStream fileInputStream = new FileInputStream(filePath);
		String htmlString = StringUtils.InputStreamToString(fileInputStream, charSet);
		
        return htmlString;
	}
}
