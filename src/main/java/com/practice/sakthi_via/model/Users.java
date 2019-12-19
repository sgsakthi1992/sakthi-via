package com.practice.sakthi_via.model;

public class Users {
	int id;
	String name;
	String username;
	String emailId;

	public Users(int id, String name, String username, String emailId) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.emailId = emailId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", name=" + name + ", username=" + username + ", emailId=" + emailId + "]";
	}

}
