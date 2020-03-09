package com.its.report.web.api.handler;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.its.report.web.api.model.ChannelModel;


public class SnakeWebSocketHandler extends TextWebSocketHandler {

	@Autowired
	@Qualifier("queueNewConnection")
	public LinkedBlockingDeque<ChannelModel> queueNewConnection = null;

	/*
	 * Sau khi web giao dien ket noi se co 1 connection moi o day
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		ChannelModel channelModel = new ChannelModel();

		channelModel.setChannelId(session.getId());
		channelModel.setStartDateTime(new Date());
		channelModel.setSession(session);
		this.queueNewConnection.push(channelModel);
	}

	/*
	 * Sau khi web giao dien bi ngat ket noi se co 1 event duoc day vao day
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		ChannelModel channelModel = new ChannelModel();

		channelModel.setChannelId(session.getId());
		channelModel.setStartDateTime(new Date());
		channelModel.setSession(session);
		channelModel.setCloseStatus(status);
		this.queueNewConnection.push(channelModel);
	}
}
