package com.twitter.hbc.example;

import java.time.LocalTime;

public class Feed {
	String value;
	LocalTime processedTime;
	
	Feed(String value, LocalTime processedTime) {
		this.value = value;
		this.processedTime = processedTime;
	}

}
