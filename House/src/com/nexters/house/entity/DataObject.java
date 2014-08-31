package com.nexters.house.entity;

public class DataObject {
	private int color;
	private String name;
	
	public DataObject(String name,int color){
		this.name=name;
		this.color=color;
	}
	public DataObject(String path) {
		this.name = path;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
