package com.example.newswatch.models;

public class Category
{
    private String name;
    private int imageUrl;

    public Category(String name, int imageUrl)
    {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName()
    {
        return name;
    }

    public int getImageUrl()
    {
        return imageUrl;
    }
}
