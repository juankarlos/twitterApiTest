package com.twitter.hbc.example;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import emoji4j.EmojiUtils;
import twitter4j.JSONArray;

public class Statistics {
	private List<Feed> feed = new ArrayList<Feed>();
	private static final CharSequence URL = "\"url\"";
	private static final CharSequence EMOJIS = "";
	private int msgWithUrl = 0;
	private int msgWithEmojis = 0;
	private int msgWithPhoto = 0;

	private Map<String, Integer> topEmojis = new HashMap<String, Integer>();
	private Map<String, Integer> topHashTags = new HashMap<String, Integer>();
	private int deleted;

	Statistics() {
	}

	public int getTwitsCount() {
		return feed.size();
	}

	public String toString() {
		return "Total number of tweets: " + feed.size() + "\nPercentage of tweets with url: " + urlPercentage()
				+ "\nPercentage of tweets with emoji: " + emojiPercentage() + "\nTop emojis: " + topEmojis()
				+ "\nTop hashtags: " + topHashTags() + "\nTop emojis: " + topEmojis();
	}

	private String topHashTags() {
		ValueComparator bvc = new ValueComparator(topHashTags);
		TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);

		sorted_map.putAll(topHashTags);

		return sorted_map.toString();
	}

	private String topEmojis() {

		ValueComparator bvc = new ValueComparator(topEmojis);
		TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);

		// System.out.println("unsorted map: " + topEmojis);
		sorted_map.putAll(topEmojis);
		// System.out.println("results: " + sorted_map);

		return sorted_map.toString();
	}

	private String emojiPercentage() {
		return String.valueOf(msgWithEmojis > 0 && feed.size() > 0 ? msgWithEmojis * 100 / feed.size() : 0) + "%";
	}

	private String urlPercentage() {
		return String.valueOf(msgWithUrl > 0 && feed.size() > 0 ? msgWithUrl * 100 / feed.size() : 0) + "%";
	}

	/**
	 * Receives RAW string and parses it.
	 * 
	 * @param msg
	 */
	public void processMsg(String msg) {
		LocalTime localTime = LocalTime.now();

		try {
			// Gson gson = new Gson();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(msg);
			String text = "";
			if (object.get("delete") != null) {
				deleted++;
			} else {
				if (object.get("created_at") != null) {
					// created = object.get("created_at").toString();
					// TODO Maybe to store create date is useful.
				}

				// Detect emojis.
				if (object.get("text") != null) {
					text = object.get("text").toString();
					if (EmojiUtils.countEmojis(text) > 1) {
						msgWithEmojis++;
						Utils.splitByUnicodeSetEmojis(text, topEmojis);
						// TODO Split multiple emojis in the same text.
					}
				}

				if (object.get("entities") != null) {
					JsonObject entities = object.getAsJsonObject("entities");
					JsonArray hashtags = entities.getAsJsonArray("hashtags");
					if (hashtags != null) {
						// TODO Split hashtags
						Utils.setHashTags(hashtags, topHashTags);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error parsing feed: " + e);
		}

		// Storage msg in a Arralist list of Feeds
		feed.add(new Feed(msg, localTime));
		if (msg.contains(URL)) {
			msgWithUrl++;
		}

	}

}
