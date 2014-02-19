package com.example.taupstairs.bean;

public class InfoEndTask {

	private String endTaskString;
	private String statusId;
	private String statusPersonId;
	private String statusPersonNickname;
	private String statusTitle;
	private String endTaskPraise;
	
	public String getEndTaskString() {
		return endTaskString;
	}
	public void setEndTaskString(String endTaskString) {
		this.endTaskString = endTaskString;
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
	public String getEndTaskPraise() {
		return endTaskPraise;
	}
	public void setEndTaskPraise(String endTaskPraise) {
		this.endTaskPraise = endTaskPraise;
	}
	@Override
	public String toString() {
		return "InfoEndTask [endTaskString=" + endTaskString + ", statusId="
				+ statusId + ", statusPersonId=" + statusPersonId
				+ ", statusPersonNickname=" + statusPersonNickname
				+ ", statusTitle=" + statusTitle + ", endTaskPraise="
				+ endTaskPraise + "]";
	}
	
}
