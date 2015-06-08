package com.ilp.innovations.pushnotification;

public class Notification {
	
	private int notificationId;
	private String notifierName;
	private String notifierEmpId;
	private String category;
	private String contact;
	private String message;
	
	public Notification(int notificationId, String notifierName, String notifierEmpId,
			String category, String contact, String message) {
		super();
		this.notificationId = notificationId;
		this.notifierName = notifierName;
		this.notifierEmpId = notifierEmpId;
		this.category = category;
		this.contact = contact;
		this.message = message;
	}
	
	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getNotifierName() {
		return notifierName;
	}
	public void setNotifierName(String notifierName) {
		this.notifierName = notifierName;
	}
	public String getNotifierEmpId() {
		return notifierEmpId;
	}
	public void setNotifierEmpId(String notifierEmpId) {
		this.notifierEmpId = notifierEmpId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
