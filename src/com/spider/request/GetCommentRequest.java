package com.spider.request;

import java.util.ArrayList;
import java.util.List;

import com.spider.bean.Comment;
import com.spider.util.StringUtils;

public class GetCommentRequest extends BaseRequest implements Runnable {

	private String mTopicId;

	public GetCommentRequest(String url, String topicId) {
		super(url);
		mTopicId = topicId;
	}

	@Override
	public void onSuccess(String htmlString) {
		List<Comment> resultList = mRegular.getCommentFromHtmlString(htmlString, mTopicId);
		System.out.println("URL:" + mUrl);
		System.out.println("共找到匹配项" + resultList.size() + "个\n");
		for (Comment comment : resultList) {
			mSensitiveCommentListener.setSensitiveComment(comment);
			String string = "topicId：" + comment.getTopicId()+ "\r\n" + "ID：" + comment.getId() + "\r\n"
					+ "author：" + comment.getAuthor() + "\r\n" + "createTime：" + comment.getCreateDate() + "\r\n"
					+ "content：" + comment.getContent() + "\r\n";
			System.out.println(string);
			
			List sqlValues = new ArrayList<>();
			sqlValues.add(comment.getId());
			sqlValues.add(comment.getTopicId());
			sqlValues.add(comment.getAuthor());
			sqlValues.add(comment.getCreateDate());
			sqlValues.add(comment.getContent());
			mDBHelper.setSql(StringUtils.SQL_FOR_INSERT_COMMENT);
			mDBHelper.setSqlValues(sqlValues);
			mDBHelper.executeUpdate();
		}
	}

}
