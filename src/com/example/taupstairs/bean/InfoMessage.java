package com.example.taupstairs.bean;

import java.util.List;

public class InfoMessage {

	private String messageId;
	private String currentMessage;
	private String currentReplyId;
	private String currentReplyNickname;
	private String statusId;
	private String statusPersonId;
	private String statusPersonNickname;
	private String statusTitle;
	private List<MessageContent> contents;
	
	public static final String MESSAGE_ID = "messageId";
	public static final String CURRENT_MESSAGE = "currentMessage";
	public static final String STATUS_ID = "statusId";
	public static final String STATUS_PERSONID = "statusPersonId";
	public static final String STATUS_PERSONNICKNAME = "statusPersonNickname";
	public static final String STATUS_TITLE = "statusTitle";
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getCurrentMessage() {
		return currentMessage;
	}
	public void setCurrentMessage(String currentMessage) {
		this.currentMessage = currentMessage;
	}
	public String getCurrentReplyId() {
		return currentReplyId;
	}
	public void setCurrentReplyId(String currentReplyId) {
		this.currentReplyId = currentReplyId;
	}
	public String getCurrentReplyNickname() {
		return currentReplyNickname;
	}
	public void setCurrentReplyNickname(String currentReplyNickname) {
		this.currentReplyNickname = currentReplyNickname;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getStatusPersonId() {
		return statusPersonId;
	}
	public void setStatusPersonId(String statusPersonId) {
		this.statusPersonId = statusPersonId;
	}
	public String getStatusPersonNickname() {
		return statusPersonNickname;
	}
	public void setStatusPersonNickname(String statusPersonNickname) {
		this.statusPersonNickname = statusPersonNickname;
	}
	public String getStatusTitle() {
		return statusTitle;
	}
	public void setStatusTitle(String statusTitle) {
		this.statusTitle = statusTitle;
	}
	public List<MessageContent> getContents() {
		return contents;
	}
	public void setContents(List<MessageContent> contents) {
		this.contents = contents;
	}
	@Override
	public String toString() {
		return "InfoMessage [messageId=" + messageId + ", currentMessage="
				+ currentMessage + ", currentReplyId=" + currentReplyId
				+ ", currentReplyNickname=" + currentReplyNickname
				+ ", statusId=" + statusId + ", statusPersonId="
				+ statusPersonId + ", statusPersonNickname="
				+ statusPersonNickname + ", statusTitle=" + statusTitle
				+ ", contents=" + contents + "]";
	}
	
}
