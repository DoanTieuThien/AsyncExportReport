package com.its.report.web.api.thread;

import java.util.concurrent.LinkedBlockingDeque;

/*
 * tao khung thread base 
 */
public abstract class ThreadAbstractBase {
	protected int miThreadState = 1;
	private long threadId;
	private String threadName;

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public void start() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				runningThread();
			}
		};
		Thread t = new Thread(r);
		t.start();
		this.threadId = t.getId();
	}

	public void stop() {
		this.miThreadState = 0;
	}

	public long getThreadId() {
		return this.threadId;
	}

	// abstract function
	public abstract void init() throws Exception;

	public abstract void processing() throws Exception;

	public abstract void end() throws Exception;

	/*
	 * private running thread
	 */
	private void runningThread() {
		this.miThreadState = 1;
		while (this.miThreadState != 0) {
			try {
				init();
				processing();
			} catch (Exception exp) {
				exp.printStackTrace();
			} finally {
				try {
					end();
				} catch (Exception exp) {
					exp.printStackTrace();
				} finally {

				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
