package com.example.taupstairs.bean;

public class MessageContent {

	private Long id;
	
	private String contentId;
	private String replyId;
	private String replyNickname;
	private String receiveId;
	private String receiveNickname;
	private String content;
	
	public static final String TB_NAME = "message_content";
	public static final String ID = "_id";
	
	public static final String CONTENT_ID = "contentId";
	public static final String REPLY_ID = "replyId";
	public static final String REPLY_NICKNAME = "replyNickname";
	public static final String RECEIVE_ID = "receiveId";
	public static final String RECEIVE_NICKNAME = "receiveNickname";
	public static final String CONTENT = "content";
	
	public static final String REPLY_START = "reply_start";
	public static final String REPLY_END = "reply_end";
	public static final String RECEIVE_START = "receive_start";
	public static final String RECEIVEY_END = "receive_end";
	public static final String REPLY_TEXT = "  回复  ";
	public static final int REPLY_TEXT_LENNTH = REPLY_TEXT.length();
	public static final String REPLY_TEMP = " : ";
	public static final int REPLY_TEMP_LENGTH = REPLY_TEMP.length();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	public String getReplyNickname() {
		return replyNickname;
	}
	public void setReplyNickname(String replyNickname) {
		this.replyNickname = replyNickname;
	}
	public String getReceiveId() {
		return receiveId;
	}
	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}
	public String getReceiveNickname() {
		return receiveNickname;
	}
	public void setReceiveNickname(String receiveNickname) {
		this.receiveNickname = receiveNickname;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "MessageContent [contentId=" + contentId + ", replyId="
				+ replyId + ", replyNickname=" + replyNickname + ", receiveId="
				+ receiveId + ", receiveNickname=" + receiveNickname
				+ ", content=" + content + "]";
	}
	
}
