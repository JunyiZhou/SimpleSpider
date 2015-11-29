package com.spider.request;

import java.util.ArrayList;
import java.util.List;

import com.spider.bean.Topic;
import com.spider.util.StringUtils;

public class GetTopicRequest extends BaseRequest implements Runnable {

	private String mDiscussionId;
	private String mBaseUrl;

	public GetTopicRequest(String url, String page, String discussionId) {
		super(url + page);
		mDiscussionId = discussionId;
		mBaseUrl = url;
	}

	@Override
	public void onSuccess(String htmlString) {
		List<Topic> resultList = mRegular.getTopicFromHtmlString(htmlString, mDiscussionId);
		System.out.println("URL:" + mUrl);
		System.out.println("共找到匹配项" + resultList.size() + "个\n");
		for (Topic topic : resultList) {
			String string = "discussionId：" + topic.getDiscussionId() + "\r\n" + "ID：" + topic.getId() + "\r\n"
					+ "author：" + topic.getAuthor() + "\r\n" + "title：" + topic.getTitle() + "\r\n" + "url："
					+ topic.getUrl() + "\r\n" + "createTime：" + topic.getCreateDate() + "\r\n" + "commentNum："
					+ topic.getCommentNum() + "\r\n";
			System.out.println(string);
			
			List sqlValues = new ArrayList<>();
			sqlValues.add(topic.getId());
			sqlValues.add(topic.getDiscussionId());
			sqlValues.add(topic.getTitle());
			sqlValues.add(topic.getUrl());
			sqlValues.add(topic.getAuthor());
			sqlValues.add(topic.getCreateDate());
			sqlValues.add(topic.getCommentNum());
			mDBHelper.setSql(StringUtils.SQL_FOR_INSERT_TOPIC);
			mDBHelper.setSqlValues(sqlValues);
			mDBHelper.executeUpdate();
			
			if (Integer.valueOf(topic.getCommentNum()) > 0) {
				GetCommentRequest getCommentRequest = new GetCommentRequest(String.format(StringUtils.URL_COMMENT, topic.getId()), topic.getId());
				if (mAddRequestListener != null) {
					mAddRequestListener.add(getCommentRequest);
				}
			}
		}

		String nextPage = mRegular.getStringFromHtmlString(htmlString, StringUtils.REGULAR_EXPRESSION_FOR_TOPIC_NEXT);
		if (!nextPage.isEmpty()) {
			GetTopicRequest getTopicRequest = new GetTopicRequest(mBaseUrl, nextPage, mDiscussionId);
			if (mAddRequestListener != null) {
				mAddRequestListener.add(getTopicRequest);
			}
		}
	}

}
