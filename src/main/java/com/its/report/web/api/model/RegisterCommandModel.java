package com.its.report.web.api.model;

import java.io.Serializable;
import java.util.List;

public class RegisterCommandModel implements Serializable {
	private String commandName;
	private String transactionId;
	private String channelId;
	private String status;
	private List<ParameterModel> parameters;

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ParameterModel> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterModel> parameters) {
		this.parameters = parameters;
	}
}
