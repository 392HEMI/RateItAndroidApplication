package com.rateit.androidapplication.models;

import java.util.UUID;

public class ObjectModel {
	public class Property
	{
		public String Title;
		public String Value;
	}
	public class Comment
	{
		public int ID;
		public User User;
		public String Text;
		public int Likes;
		public Boolean Like;
	}
	
	public class User
	{
		public UUID ID;
		public String Avatar;
		public String Name;
		public String Surname;
	}
	
	public int ID;
	public String Title;
	public Double Rating;
	public Integer MyMark;
	public Object[] Properties;
	public String[] Images;
	public Comment[] Comments;
}