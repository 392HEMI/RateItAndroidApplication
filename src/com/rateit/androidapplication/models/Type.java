package com.rateit.androidapplication.models;

public class Type {
	public int id;
	public String title;
	public boolean hasObjects;
	
	public Type(int _id, String _title, boolean _hasObjects)
	{
		id = _id;
		title = _title;
		hasObjects = _hasObjects;
	}
}
