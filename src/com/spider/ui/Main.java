package com.spider.ui;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.spider.util.RSA;
import com.spider.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

public class Main {

	public static void main(String[] args) throws IllegalStateException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpPost httppostForLogin = new HttpPost(StringUtils.URL_LOGIN);
        List<NameValuePair> paramsForLogin = new ArrayList<NameValuePair>();
        paramsForLogin.add(new BasicNameValuePair("account", "JunyiZhou"));
        paramsForLogin.add(new BasicNameValuePair("password", new RSA().encrypt("iambest")));
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
        
        HttpPost httppostForReply = new HttpPost(String.format(StringUtils.URL_REPLY, "1003544"));
        List<NameValuePair> paramsForReply = new ArrayList<NameValuePair>();
        paramsForReply.add(new BasicNameValuePair("post_id", "1003544"));
        paramsForReply.add(new BasicNameValuePair("site_id", "710"));
        paramsForReply.add(new BasicNameValuePair("content", "赞一个"));
		httppostForReply.setEntity(new UrlEncodedFormEntity(paramsForReply));
        HttpResponse responseForReply = httpclient.execute(httppostForReply);
        HttpEntity entityForReply = responseForReply.getEntity();
        // 在这里可以用Jsoup之类的工具对返回结果进行分析，以判断登录是否成功
        String htmlStringForReply = "";
        InputStream htmlInputStreamForReply = null;
        if(entityForReply != null){
            System.out.println("获取html成功");
            htmlInputStreamForReply = entityForReply.getContent();
            htmlStringForReply = StringUtils.InputStreamToString(htmlInputStreamForReply, "UTF-8");
            System.out.println(htmlStringForReply);
        }
	}

}
