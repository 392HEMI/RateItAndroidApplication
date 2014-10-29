package com.rateit.androidapplication.models;

public class ObjectsModel {
	public class Type
	{
		public int id;
		public String title;
		
		public Type(int _id, String _title)
		{
			id = _id;
			title = _title;
		}
	}
	public class GeneralObject
	{
		public int id;
		public String title;
		
		public GeneralObject(int _id, String _title)
		{
			id = _id;
			title = _title;
		}
	}
	
	public Type type;
	public GeneralObject[] objects;

	public ObjectsModel()
	{
	}	
	
	public ObjectsModel(Type _type, GeneralObject[] _objects)
	{
		type = _type;
		objects = _objects;
	}
}
