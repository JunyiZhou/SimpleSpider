package com.spider.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtils {
	public static final String REGULAR_EXPRESSION_FOR_TOPIC_ID = "/t/(.*?)/";
	public static final String REGULAR_EXPRESSION_FOR_TOPIC_NEXT = "<a rel=\"next\" href=\"(.*?)\"";
	public static final String REGULAR_EXPRESSION_FOR_TOPIC = "<td class=\"title\">[\\s\\S]*?<a href=\"(.*?)\">(.*?)</a>[\\s\\S]*?<a class=\"site-anchor\"[\\s\\S]*?>(.*?)</a>[\\s\\S]*?<time pubdate[\\s\\S]*?>(.*?)</time>[\\s\\S]*?<td class=\"comment\">(.*?)</td>";
	public static final String REGULAR_EXPRESSION_FOR_DISCUSSION = "<a title=\"(.*?)\" href=\"(.*?)\" data-site-id=\"(.*?)\"";
	public static final String REGULAR_EXPRESSION_FOR_COMMENT = "\"note_id\":\"(.*?)\".*?\"content\":\"(.*?)\".*?\"created_at\":\"(.*?)\".*?\"author\".*?\"name\":\"(.*?)\"";

	public static final String REGULAR_EXPRESSION_FOR_CONTENT_FILTER = "<br \\\\/>\\\\r\\\\n";
	public static final String REGULAR_EXPRESSION_FOR_FILENAME_FILTER = "[/:*\\?\"<>|\\\\]";

	public static final String TARGET_EMPTY = "";
	public static final String TARGET_NEWLINE = "\r\n";

	public static final String URL_DISCUSSION = "http://tuchong.com/discussion/hot/";
	public static final String URL_COMMENT = "http://tuchong.com/rest/posts/%1$s/comments?type=comment";
	public static final String URL_LOGIN = "http://tuchong.com/rest/accounts/login";
	public static final String URL_REPLY = "http://tuchong.com/rest/posts/%1$s/comments";

	public static final String SQL_FOR_INSERT_DISCUSSION = "insert into discussion(id, title, url) values(?, ?, ?)";
	public static final String SQL_FOR_INSERT_TOPIC = "insert into topic(id, discussion_id, title, url, author, create_date, comment_num) values(?, ?, ?, ?, ?, ?, ?)";
	public static final String SQL_FOR_INSERT_COMMENT = "insert into comment(id, topic_id, author, create_date, content) values(?, ?, ?, ?, ?)";

	public static final String PUBLIC_MODULE = "D8CC0180AFCC72C9F5981BDB90A27928672F1D6EA8A57AF44EFFA7DAF6EFB17DAD9F643B9F9F7A1F05ACC2FEA8DE19F023200EFEE9224104627F1E680CE8F025AF44824A45EA4DDC321672D2DEAA91DB27418CFDD776848F27A76E747D53966683EFB00F7485F3ECF68365F5C10C69969AE3D665162D2EE3A5BA109D7DF6C7A5";
	public static final String PUBLIC_EXPONENT = "10001";

	public static void saveStringToFile(String filepath, String string) {
		try {
			OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath, true), "UTF-8");
			outs.write(string);
			outs.close();
		} catch (IOException e) {
			System.out.println("Error at save string...");
			e.printStackTrace();
		}
	}

	public static String InputStreamToString(InputStream in_st, String charset) throws IOException {
		BufferedReader buff = new BufferedReader(new InputStreamReader(in_st, charset));
		StringBuffer res = new StringBuffer();
		String line = "";
		while ((line = buff.readLine()) != null) {
			res.append(line);
		}
		return res.toString();
	}

	public static String convert(String utfString) {
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while ((i = utfString.indexOf("\\u", pos)) != -1) {
			sb.append(utfString.substring(pos, i));
			if (i + 5 < utfString.length()) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
			}
		}
		return sb.toString();
	}

	public static String gbEncoding(String gbString) {
		char[] utfBytes = gbString.toCharArray();
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]);
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		System.out.println("unicodeBytes is: " + unicodeBytes);
		return unicodeBytes;
	}

	public static String filter(String string, String regularString, String targetString)
			throws PatternSyntaxException {
		Pattern p = Pattern.compile(regularString);
		Matcher m = p.matcher(string);
		return m.replaceAll(targetString).trim();
	}

	// 汉字-->16进制
	public static String encode(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	// 16进制--〉汉字
	public static String decode(String hexStr) {
		if (null == hexStr || "".equals(hexStr) || (hexStr.length()) % 2 != 0) {
			return null;
		}
		int byteLength = hexStr.length() / 2;
		byte[] bytes = new byte[byteLength];
		int temp = 0;
		for (int i = 0; i < byteLength; i++) {
			temp = hex2Dec(hexStr.charAt(2 * i)) * 16 + hex2Dec(hexStr.charAt(2 * i + 1));
			bytes[i] = (byte) (temp < 128 ? temp : temp - 256);
		}
		return new String(bytes);
	}

	private static String hexString = "0123456789ABCDEF";

	private static int hex2Dec(char ch) {
		if (ch == '0')
			return 0;
		if (ch == '1')
			return 1;
		if (ch == '2')
			return 2;
		if (ch == '3')
			return 3;
		if (ch == '4')
			return 4;
		if (ch == '5')
			return 5;
		if (ch == '6')
			return 6;
		if (ch == '7')
			return 7;
		if (ch == '8')
			return 8;
		if (ch == '9')
			return 9;
		if (ch == 'a')
			return 10;
		if (ch == 'A')
			return 10;
		if (ch == 'B')
			return 11;
		if (ch == 'b')
			return 11;
		if (ch == 'C')
			return 12;
		if (ch == 'c')
			return 12;
		if (ch == 'D')
			return 13;
		if (ch == 'd')
			return 13;
		if (ch == 'E')
			return 14;
		if (ch == 'e')
			return 14;
		if (ch == 'F')
			return 15;
		if (ch == 'f')
			return 15;
		else
			return -1;
	}
}
