package com.example.newswatch.models.categorynews;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categorynews{

	@SerializedName("totalResults")
	@Expose
	private int totalResults;

	@SerializedName("articles")
	@Expose
	private List<ArticlesItem> articles;

	@SerializedName("status")
	@Expose
	private String status;

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}

	public void setArticles(List<ArticlesItem> articles){
		this.articles = articles;
	}

	public List<ArticlesItem> getArticles(){
		return articles;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}