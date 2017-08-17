package com.ztgeo.entity;
//返回的结果类
public class reqMessage {
	private String errorCode;//状态码
	private String mobile;//手机号码
	private String msgGroup;//消息组 用来定位消息
	private String receiveDate;//发送时间
	private String reportStatus;//反馈码
	
	public reqMessage() {
	}

	public reqMessage(String errorCode, String mobile, String msgGroup, String receiveDate, String reportStatus) {
		super();
		this.errorCode = errorCode;
		this.mobile = mobile;
		this.msgGroup = msgGroup;
		this.receiveDate = receiveDate;
		this.reportStatus = reportStatus;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMsgGroup() {
		return msgGroup;
	}

	public void setMsgGroup(String msgGroup) {
		this.msgGroup = msgGroup;
	}

	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	
	
}
