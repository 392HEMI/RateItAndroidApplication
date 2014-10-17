package com.example.rateitapplication.models;

public class SubCategory
{
	public int id;
	public String title;
	public boolean isLast;
	
	public SubCategory(int _id, String _title, boolean _isLast)
	{
		id = _id;
		title = _title;
		isLast = _isLast;
	}
}