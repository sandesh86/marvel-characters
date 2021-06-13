package com.yapily.characters.utils;

import java.util.concurrent.CountDownLatch;

import com.yapily.characters.service.DataLoader;

/**
 * Worker class to make parallel calls
 * @author acer
 *
 */
public class CallAPI implements Runnable {

	private CountDownLatch latch;
	private int offset;
	private String url;
	private DataLoader loader;	
	
	public CallAPI(CountDownLatch latch, int offset, String url, DataLoader loader) {
		this.latch = latch;
		this.offset = offset;
		this.url = url;
		this.loader = loader;
	}
	@Override
	public void run() {
		loader.loadCharacters(offset, url);
		latch.countDown();
	}

}
