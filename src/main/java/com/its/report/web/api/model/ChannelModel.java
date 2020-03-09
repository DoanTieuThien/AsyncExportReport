package com.its.report.web.api.model;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Date;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class ChannelModel implements Serializable {
	private String channelId;
	private Date startDateTime;
	private WebSocketSession session;
	private CloseStatus closeStatus;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public CloseStatus getCloseStatus() {
		return closeStatus;
	}

	public void setCloseStatus(CloseStatus closeStatus) {
		this.closeStatus = closeStatus;
	}

	public void close() {
		if (this.session == null) {
			return;
		}
		try {
			this.session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.session = null;
	}

	public boolean isOpen() {
		return this.session != null && this.session.isOpen();
	}

	public void sendData(String data) {
		try {
			if (this.session == null) {
				return;
			}
			TextMessage textMessage = new TextMessage(data.getBytes(Charset.forName("utf-8")));
			this.session.sendMessage(textMessage);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	public void sendData(TextMessage data) {
		try {
			if (this.session == null) {
				return;
			}
			this.session.sendMessage(data);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
