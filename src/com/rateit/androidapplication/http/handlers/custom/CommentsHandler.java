package com.rateit.androidapplication.http.handlers.custom;

import java.util.UUID;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.models.Comment;
import com.rateit.androidapplication.models.User;

public class CommentsHandler implements IResponseHandler {

	
	private User GetUser(JSONObject object) throws JSONException
	{
		User usr = new User();
		usr.ID = UUID.fromString(object.getString("ID"));
		usr.Name = object.getString("Name");
		usr.Surname = object.getString("Surname");
		usr.Avatar = object.getString("Avatar");
		return usr;
	}
	
	private Comment GetComment(JSONObject object)
	{
		boolean valid = true;
		Comment com = new Comment();
		try
		{
			JSONObject usrObj = object.getJSONObject("User");
			com.ID = object.getInt("ID");			
			com.Like = object.isNull("Like") ? null : object.getBoolean("Like");
			com.Likes = object.getInt("Likes");
			com.User = GetUser(usrObj);			
		}
		catch (JSONException e)
		{
			valid = false;
		}
		if (valid)
			return com;
		else
			return null;
	}
	
	@Override
	public void Start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Success(int statusCode, Header[] headers, String response) {
		JSONArray array = null;
		try
		{
			JSONObject object = new JSONObject(response);
			if (object.getString("Status").equalsIgnoreCase("ok"))
				array = object.getJSONArray("Content");
		}
		catch (JSONException e) 
		{
		}
		
		if (array == null)
			return;
		
		boolean valid = true;
		int count = array.length();
		Comment[] comments = new Comment[count];
		
		JSONObject obj;
		Comment c;
		
		try
		{
			for (int i = 0; i < count; i++)
			{
				obj = array.getJSONObject(i);
				c = GetComment(obj);
				if (c == null)
				{
					valid = false;
					break;
				}
				comments[i] = c;
			}
		}
		catch (JSONException e)
		{
			valid = false;
		}
		if (!valid)
			return;
		
	}

	@Override
	public void Failure(int statusCode, Throwable error, String content) {
		// TODO Auto-generated method stub
		
	}

}
