package com.spider.util;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.spider.callback.AddRequestListener;
import com.spider.callback.SetMessageListener;

public class RequestManager extends Thread implements AddRequestListener {
	private static final int MAX_REQUEST_NUM = 5;
	private static final long MAX_FREE_TIME = 30 * 1000;
	private static final long REQUEST_PAUSE_TIME = 1000;
	
	private static int currentNum = 0;
	
	private long freeTimeStart, freeTimeEnd;

	private Queue<Runnable> requsetQueue = new LinkedBlockingQueue<Runnable>();

	private SetMessageListener mSetMessageListener;
	
	public void setSetMessageListener(SetMessageListener setMessageListener) {
		mSetMessageListener = setMessageListener;
	}
	
	public void addRequest(Runnable request) {
		requsetQueue.add(request);
	}

	public void pause() {
		try {
			sleep(REQUEST_PAUSE_TIME);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showManagerMessage() {
		if (mSetMessageListener != null) {
			mSetMessageListener.setMessage("当前请求队列中有" + requsetQueue.size() + "个请求" + "\n" + "正在执行第" + currentNum + "个请求" + "\n");
		}
//		System.out.println("当前请求队列中有" + requsetQueue.size() + "个请求");
//		System.out.println("正在执行第" + currentNum + "个请求");
	}
	
	@Override
	public void add(Runnable request) {
		// TODO Auto-generated method stub
		addRequest(request);
	}

	@Override
	public void run() {
		super.run();
		freeTimeStart = System.currentTimeMillis();
		freeTimeEnd = System.currentTimeMillis();
		while (freeTimeEnd - freeTimeStart < MAX_FREE_TIME) {
			freeTimeEnd = System.currentTimeMillis();
			if (requsetQueue.size() > MAX_REQUEST_NUM) {
				for (int i = 0; i < 5; i ++) {
					requsetQueue.poll().run();
					currentNum++;
					showManagerMessage();
				}
				freeTimeStart = System.currentTimeMillis();
			} else if (requsetQueue.size() > 0) {
				for (int i = 0; i < requsetQueue.size(); i++) {
					requsetQueue.poll().run();
					currentNum++;
					showManagerMessage();
				}
				freeTimeStart = System.currentTimeMillis();
			}
			pause();
		}
	}
}
