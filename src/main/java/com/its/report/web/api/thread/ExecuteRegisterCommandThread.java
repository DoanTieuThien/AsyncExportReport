package com.its.report.web.api.thread;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.its.report.web.api.model.RegisterCommandModel;
import com.its.report.web.api.model.TextMessageModel;

public class ExecuteRegisterCommandThread extends ThreadAbstractBase {
	/*
	 * log4j
	 */
	private static final Logger log = LoggerFactory.getLogger(ExecuteRegisterCommandThread.class);

	@Autowired
	@Qualifier("queueRegisterCommand")
	public LinkedBlockingDeque<RegisterCommandModel> queueRegisterCommand = null;
	@Autowired
	@Qualifier("queueTransferEvenetData")
	public LinkedBlockingDeque<TextMessageModel> queueTransferEvenetData = null;
	@Autowired
	@Qualifier("commandManagerMap")
	public ConcurrentHashMap<String, RegisterCommandModel> commandManagerMap = null;

	@Override
	public void init() throws Exception {
		log.warn("Thread " + this.getThreadId() + ": is started");
	}

	@Override
	public void processing() throws Exception {
		while (this.miThreadState != 0) {
			try {
				RegisterCommandModel command = this.queueRegisterCommand == null || this.queueRegisterCommand.isEmpty()
						? null
						: this.queueRegisterCommand.poll();

				if (command != null) {
					command.setStatus("DEQUEUED");
					this.commandManagerMap.put(command.getTransactionId(), command);
					Thread.sleep(10000);
					TextMessageModel textMessageModel = new TextMessageModel();
					textMessageModel.setChannelId(command.getChannelId());
					textMessageModel.setPayload(command);
					textMessageModel.setCommandName(command.getCommandName());
					this.queueTransferEvenetData.push(textMessageModel);
					
					Thread.sleep(10000);
					command.setStatus("EXPORTING");
					this.commandManagerMap.put(command.getTransactionId(), command);
					textMessageModel = new TextMessageModel();
					textMessageModel.setChannelId(command.getChannelId());
					textMessageModel.setPayload(command);
					textMessageModel.setCommandName(command.getCommandName());
					this.queueTransferEvenetData.push(textMessageModel);
					
					Thread.sleep(10000);
					command.setStatus("EXPORT-FINISEHED");
					this.commandManagerMap.put(command.getTransactionId(), command);
					textMessageModel = new TextMessageModel();
					textMessageModel.setChannelId(command.getChannelId());
					textMessageModel.setPayload(command);
					textMessageModel.setCommandName(command.getCommandName());
					this.queueTransferEvenetData.push(textMessageModel);
					Thread.sleep(10);
					continue;
				}
			} catch (Exception exp) {
				log.error("Thread " + this.getThreadId() + " process error", exp);
			}
			Thread.sleep(100);
		}
	}

	@Override
	public void end() throws Exception {
		log.warn("Thread " + this.getThreadId() + ": is stopped");
	}

}
