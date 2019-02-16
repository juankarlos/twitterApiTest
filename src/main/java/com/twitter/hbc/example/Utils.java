package com.twitter.hbc.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import twitter4j.JSONObject;

public class Utils {

	public static void splitByUnicodeSetEmojis(String msg, Map<String, Integer> topEmojis) throws Exception {
		// System.out.println("Original String:"+msg);
		String regexPattern = "[\uD83C-\uDBFF\uDC00-\uDFFF]+";
		byte[] utf8 = msg.getBytes("UTF-8");

		String string1 = new String(utf8, "UTF-8");

		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(string1);
		List<String> matchList = new ArrayList<String>();

		while (matcher.find()) {
			matchList.add(matcher.group());
		}

		for (int i = 0; i < matchList.size(); i++) {
			String keyEmoji = matchList.get(i);
			if (!topEmojis.containsKey(keyEmoji)) {
				topEmojis.put(keyEmoji, 1);
			} else {
				int count = topEmojis.get(keyEmoji);
				topEmojis.put(keyEmoji, ++count);
			}
		}
	}

	public static void setHashTags(JsonArray hashtags, Map<String, Integer> topHashTags) {
		if (hashtags != null) {
			
			try {
				for (int i = 0; i < hashtags.size(); i++) {
		             JsonObject jb = hashtags.get(i).getAsJsonObject();
		             String text = jb.get("text").toString();
		             if (!topHashTags.containsKey(text)) {
		            	topHashTags.put(text, 1);
		 			} else {
		 				int count = topHashTags.get(text);
		 				topHashTags.put(text, ++count);
		 			}
		        }
			}
			catch (Exception e) {
				System.out.println("Error gettings hashtags: " + e);
			}
		}
	}
	
	
}