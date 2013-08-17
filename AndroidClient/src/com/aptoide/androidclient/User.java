package com.aptoide.androidclient;

public class User {
	
	private String email;
	private String password;
	private String usertoken;
	
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUsertoken(String usertoken) {
		this.usertoken = usertoken;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUsertoken() {
		return usertoken;
	}

}
