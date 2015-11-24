package com.spider.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.spider.bean.Comment;
import com.spider.bean.Discussion;
import com.spider.bean.Topic;

public class Regular {

	public Regular() {
	}

	public String getStringFromHtmlString(String htmlString, String regularExpression) {
		if (regularExpression.isEmpty()) {
			return null;
		}

		Pattern p = Pattern.compile(regularExpression);
		Matcher m = p.matcher(htmlString);

		while (m.find()) {
			return m.group(1);
		}
		return "";
	}

	public List<Discussion> getDiscussionFromHtmlString(String htmlString) {
		List<Discussion> result = new ArrayList<Discussion>();
		Pattern p = Pattern.compile(StringUtils.REGULAR_EXPRESSION_FOR_DISCUSSION);
		Matcher m = p.matcher(htmlString);
		while (m.find()) {
			Discussion discussion = new Discussion();
			discussion.setTitle(m.group(1));
			discussion.setUrl(m.group(2));
			discussion.setId(m.group(3));
			result.add(discussion);
		}
		return result;
	}

	public List<Topic> getTopicFromHtmlString(String htmlString, String discussionId) {
		List<Topic> result = new ArrayList<Topic>();
		Pattern p = Pattern.compile(StringUtils.REGULAR_EXPRESSION_FOR_TOPIC);
		Matcher m = p.matcher(htmlString);

		while (m.find()) {
			Topic topic = new Topic();
			topic.setDiscussionId(discussionId);
			topic.setUrl(m.group(1));
			topic.setId(getStringFromHtmlString(topic.getUrl(), StringUtils.REGULAR_EXPRESSION_FOR_TOPIC_ID));
			topic.setTitle(m.group(2));
			topic.setAuthor(m.group(3));
			topic.setCreateDate(m.group(4));
			topic.setCommentNum(m.group(5));

			result.add(topic);
		}
		return result;
	}

	public List<Comment> getCommentFromHtmlString(String htmlString, String topicId) {
		List<Comment> result = new ArrayList<Comment>();
		Pattern p = Pattern.compile(StringUtils.REGULAR_EXPRESSION_FOR_COMMENT);
		Matcher m = p.matcher(htmlString);

		while (m.find()) {
			Comment comment = new Comment();
			comment.setTopicId(topicId);
			comment.setId(m.group(1));
			comment.setContent(StringUtils.filter(StringUtils.convert(m.group(2)),
					StringUtils.REGULAR_EXPRESSION_FOR_CONTENT_FILTER, StringUtils.TARGET_NEWLINE));
			comment.setCreateDate(m.group(3));
			comment.setAuthor(StringUtils.convert(m.group(4)));

			result.add(comment);
		}
		return result;
	}

	public static boolean checkKeyInComment(String key, String comment) {
		Pattern p = Pattern.compile(key);
		Matcher m = p.matcher(comment);
		while (m.find()) {
			return true;
		}
		return false;
	}
}
