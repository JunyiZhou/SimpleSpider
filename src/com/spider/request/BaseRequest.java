package com.spider.request;

import java.io.IOException;

import com.spider.callback.AddRequestListener;
import com.spider.callback.SetSensitiveCommentListener;
import com.spider.util.DBHelper;
import com.spider.util.Regular;
import com.spider.util.Spider;

public abstract class BaseRequest implements Runnable {
	protected static AddRequestListener mAddRequestListener;
	protected static SetSensitiveCommentListener mSensitiveCommentListener;
	
	protected String mUrl;
	protected Spider mSpider;
	protected Regular mRegular;
	protected DBHelper mDBHelper;
	
	public BaseRequest(String url) {
		mUrl = url;
		mSpider = new Spider();
		mRegular = new Regular();
		mDBHelper = new DBHelper();
	}
	
	public static void setAddRequestListener(AddRequestListener addRequestListener) {
		mAddRequestListener = addRequestListener;
	}
	
	public static void setSetSensitiveCommentListener(SetSensitiveCommentListener setSensitiveCommentListener) {
		mSensitiveCommentListener = setSensitiveCommentListener;
	}
	
	@Override
	public void run() {
		try {
			String htmlString = mSpider.getHtmlStringFromWeb(mUrl);
			if (!htmlString.isEmpty()) {
				onSuccess(htmlString);
			}
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public abstract void onSuccess(String htmlString);
}
