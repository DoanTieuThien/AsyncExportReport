package com.its.report.web.api.thread;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.its.report.web.api.model.ChannelModel;
import com.its.report.web.api.model.TextMessageModel;

public class TransferDataThread extends ThreadAbstractBase {

	private static final Logger log = LoggerFactory.getLogger(TransferDataThread.class);
	@Autowired
	@Qualifier("sessionManagerMap")
	public ConcurrentHashMap<String, ChannelModel> sessionManagerMap = null;
	@Autowired
	@Qualifier("queueTransferEvenetData")
	public LinkedBlockingDeque<TextMessageModel> queueTransferEvenetData = null;
	private ObjectMapper objectMapper = null;

	@Override
	public void init() throws Exception {
		objectMapper = new ObjectMapper();
	}

	@Override
	public void processing() throws Exception {
		while (this.miThreadState != 0) {
			try {
				TextMessageModel textMessageModel = this.queueTransferEvenetData == null
						|| this.queueTransferEvenetData.isEmpty() ? null : this.queueTransferEvenetData.poll();

				if (textMessageModel != null) {
					String commandName = textMessageModel.getCommandName() == null ? ""
							: textMessageModel.getCommandName().trim();

					if (commandName.equals("PUBLIC-NEW-CONNECTION") || commandName.equals("EXPORT-REPORT")) {
						// public ban tin den tat ca cac kenh
						this.sessionManagerMap.forEach((channelId, channelModel) -> {
							if (channelModel.isOpen()) {
								try {
									channelModel.sendData(objectMapper.writeValueAsString(textMessageModel));
								} catch (JsonProcessingException e) {
									e.printStackTrace();
								}
							} else {
								channelModel.close();
							}
						});
					} else {
						// public for only one channel
						ChannelModel channelModel = this.sessionManagerMap.get(textMessageModel.getChannelId());

						if (channelModel == null) {
							log.info("Thread " + this.getThreadId() + ": channel not found");
						} else {
							try {
								channelModel.sendData(objectMapper.writeValueAsString(textMessageModel));
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
						}
					}
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
		objectMapper = null;
	}

}
