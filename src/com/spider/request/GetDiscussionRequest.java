package com.spider.request;

import java.util.ArrayList;
import java.util.List;

import com.spider.bean.Discussion;
import com.spider.util.StringUtils;

public class GetDiscussionRequest extends BaseRequest implements Runnable {
	
	public GetDiscussionRequest(String url) {
		super(url);
	}

	@Override
	public void onSuccess(String htmlString) {
		List<Discussion> resultList = mRegular.getDiscussionFromHtmlString(htmlString);

		System.out.println("共找到匹配项" + resultList.size() + "个\n");
		for (Discussion discussion : resultList) {
			String string = "ID：" + discussion.getId() + "\r\n" + "title：" + discussion.getTitle() + "\r\n" + "url："
					+ discussion.getUrl() + "\r\n";
			System.out.println(string);
			
			List sqlValues = new ArrayList<>();
			sqlValues.add(discussion.getId());
			sqlValues.add(discussion.getTitle());
			sqlValues.add(discussion.getUrl());
			mDBHelper.setSql(StringUtils.SQL_FOR_INSERT_DISCUSSION);
			mDBHelper.setSqlValues(sqlValues);
			mDBHelper.executeUpdate();
			GetTopicRequest getTopicRequest = new GetTopicRequest(discussion.getUrl(), "", discussion.getId());
			if (mAddRequestListener != null) {
				mAddRequestListener.add(getTopicRequest);
			}
		}
		mDBHelper.close();
	}
	
}
