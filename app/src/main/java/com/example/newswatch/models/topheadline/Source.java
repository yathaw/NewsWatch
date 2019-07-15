package com.example.newswatch.models.topheadline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source{

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("id")
	@Expose
	private String id;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}
}