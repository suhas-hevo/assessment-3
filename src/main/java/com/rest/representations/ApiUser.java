package com.rest.representations;

public class ApiUser {

	private Integer userId;
	private String userName;
	private String userPassword;
	private Integer userPrivilageLevel;

	public ApiUser(Integer userId, String userName, String userPassword, Integer userPrivilageLevel) {
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userPrivilageLevel = userPrivilageLevel;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer uid) {
		this.userId = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return userPassword;
	}

	public void setPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Integer getPrivilageLevel() {
		return userPrivilageLevel;
	}

	public void setPrivilageLevel(Integer userPrivilageLevel) {
		this.userPrivilageLevel = userPrivilageLevel;
	}
}