/**
 * Copyright 2013 Twitter, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package com.twitter.hbc.example;

import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterAPIExample {

	private Statistics stats;
	private String consumerKey;
	private String consumerSecret;
	private String token;
	private String secret;
	private int maxCount;
	
	public TwitterAPIExample(int maxCount) {
		consumerKey = "mBBlX0ER3Bc4YEW4BB5X3dJJs";
	    consumerSecret = "pacpt55fz8DVieQXf49vaEcTfQM89ewREQQa04MzSGt6DL7sTb";
	    token = "49780226-YR0W1vtDSNGsvD3TTKrpVCmg8COdgbwp0I4R2TsrW";
	    secret = "RfPMRzIwiIdU0JJ81SZ0qgorMkKDOnA5XR6oePH2G02ca";
	    stats = new Statistics();
	    this.maxCount = maxCount;
	}

	public void run()
			throws InterruptedException {
		// Create an appropriately sized blocking queue
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);

		// Define our endpoint: By default, delimited=length is set (we need this for
		// our processor)
		// and stall warnings are on.
		StatusesSampleEndpoint endpoint = new StatusesSampleEndpoint();
		endpoint.stallWarnings(false);

		Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);
		// Authentication auth = new com.twitter.hbc.httpclient.auth.BasicAuth(username,
		// password);

		// Create a new BasicClient. By default gzip is enabled.
		BasicClient client = new ClientBuilder().name("sampleExampleClient").hosts(Constants.STREAM_HOST)
				.endpoint(endpoint).authentication(auth).processor(new StringDelimitedProcessor(queue)).build();

		// Establish a connection
		client.connect();

		
		// Do whatever needs to be done with messages
		System.out.println("Processing msg:");
		int count = 0;
	
		for (int msgRead = 0; msgRead <= maxCount; msgRead++) {
			if (client.isDone()) {
				System.out.println("Client connection closed unexpectedly: " + client.getExitEvent().getMessage());
				break;
			}

			String msg = queue.poll(1000, TimeUnit.SECONDS);
			if (msg == null) {
				System.out.println("Did not receive a message in 5 seconds");
			} else {				
				System.out.println(++count + " of " + maxCount);
				stats.processMsg(msg);
			}
		}
		client.stop();
	}

	public static void main(String[] args) {
		try {
			System.out.println("The feeds will be reading until you press ESCAPE.");
			TwitterAPIExample test = new TwitterAPIExample(1000);			
			test.run();
			test.getStatistics();
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}

	private void getStatistics() {
		System.out.println("Statistics:");
		System.out.println("***********");
		System.out.println(this.stats);
	}
}
