package com.example.test.entity;

public class FriendEntity {

	private int id;
	private String name;
	private String gender;
	private String addTime;

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	@Override
	public String toString() {
		return "id=" + id + ",name=" + name + ",gender" + gender + ",addTime=" + addTime;
	}

}
