package com.its.report.web.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.its.report.web.api.model.ChannelModel;
import com.its.report.web.api.model.RegisterCommandModel;
import com.its.report.web.api.model.TextMessageModel;
import com.its.report.web.api.thread.ExecuteRegisterCommandThread;
import com.its.report.web.api.thread.ManagerConnectionThread;
import com.its.report.web.api.thread.ThreadAbstractBase;
import com.its.report.web.api.thread.TransferDataThread;

@Configuration
public class AppConfiguration {

	@Bean("threadManager")
	public ConcurrentHashMap<Long, ThreadAbstractBase> threadManager() {
		ConcurrentHashMap<Long, ThreadAbstractBase> sessionManager = new ConcurrentHashMap<Long, ThreadAbstractBase>();
		
		ThreadAbstractBase thread1 = managerConnectioThread();
		thread1.setThreadName("managerConnectioThread");
		sessionManager.put(thread1.getThreadId(), thread1);
		ThreadAbstractBase thread2 = transferDataThread();
		thread1.setThreadName("transferDataThread");
		sessionManager.put(thread2.getThreadId(), thread1);
		
		ThreadAbstractBase thread3 = executeCommandThread();
		thread1.setThreadName("executeCommandThread");
		sessionManager.put(thread3.getThreadId(), thread3);
		return sessionManager;
	}
	
	@Bean("sessionManagerMap")
	public ConcurrentHashMap<String, ChannelModel> sessionManagerMap() {
		return new ConcurrentHashMap<String, ChannelModel>();
	}
	
	@Bean("commandManagerMap")
	public ConcurrentHashMap<String, RegisterCommandModel> commandManagerMap() {
		return new ConcurrentHashMap<String, RegisterCommandModel>();
	}

	@Bean("managerConnectioThread")
	public ThreadAbstractBase managerConnectioThread() {
		ManagerConnectionThread managerConnectionThread = new ManagerConnectionThread();

		managerConnectionThread.start();
		return managerConnectionThread;
	}
	
	@Bean("transferDataThread")
	public ThreadAbstractBase transferDataThread() {
		TransferDataThread transferDataThread = new TransferDataThread();

		transferDataThread.start();
		return transferDataThread;
	}
	
	@Bean("executeCommandThread")
	public ThreadAbstractBase executeCommandThread() {
		ExecuteRegisterCommandThread exe = new ExecuteRegisterCommandThread();
		exe.start();
		return exe;
	}

	@Bean("queueNewConnection")
	public LinkedBlockingDeque<ChannelModel> queueNewConnection() {
		return new LinkedBlockingDeque<ChannelModel>();
	}
	
	@Bean("queueTransferEvenetData")
	public LinkedBlockingDeque<TextMessageModel> queueTransferEvenetData() {
		return new LinkedBlockingDeque<TextMessageModel>();
	}
	
	@Bean("queueRegisterCommand")
	public LinkedBlockingDeque<RegisterCommandModel> queueRegisterCommand() {
		return new LinkedBlockingDeque<RegisterCommandModel>();
	}
}
