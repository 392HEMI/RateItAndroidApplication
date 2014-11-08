package com.rateit.androidapplication.handlers;

import java.util.UUID;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.rateit.androidapplication.ObjectActivity;
import com.rateit.androidapplication.models.ObjectModel;

public class ObjectHandler implements IResponseHandler {
	private ObjectModel ParseJSON(JSONObject object)
	{
		ObjectModel model = new ObjectModel();
		try {
			model.Title = object.getString("Title");
			model.Logo = object.getString("Logo");
			model.Rating = object.isNull("Rating") ? null : object.getDouble("Rating");
			model.MyMark = object.isNull("MyMark") ? null : object.getInt("MyMark");
			
			//JSONArray properties = object.getJSONArray("");
			JSONArray comments = object.getJSONArray("Comments");
			JSONArray images = object.getJSONArray("Images");
			
			//int properties_count = properties.length();
			int comments_count = comments.length();
			int images_count = images.length();
			
			//model.Properties = new Object[properties_count];
			model.Comments = new ObjectModel.Comment[comments_count];
			model.Images = new String[images_count];
			
			JSONObject comment;
			JSONObject user;
			//for (int i = 0; i < properties_count; i++)
			//{
				
			//}
			for (int i = 0; i < comments_count; i++)
			{
				comment = comments.getJSONObject(i);
				user = comment.getJSONObject("User");
				ObjectModel.Comment com = model.new Comment();
				ObjectModel.User usr = model.new User();
				com.ID = comment.getInt("ID");
				com.Likes = comment.getInt("Likes");
				com.Like = comment.isNull("Like") ? null : comment.getBoolean("Like");
				com.Text = comment.getString("Text");
				usr.ID = UUID.fromString(user.getString("ID"));
				usr.Name = user.getString("Name");
				usr.Surname = user.getString("Surname");
				usr.Avatar = user.getString("Avatar");
				
				com.User = usr;
				
				model.Comments[i] = com;
			}
			for (int i = 0; i < images_count; i++)
			{
				model.Images[i] = images.getString(i);
			}
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
	
	private ObjectActivity _activity;
	public ObjectHandler(ObjectActivity activity)
	{
		_activity = activity;
	}
	
	@Override
	public void Start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Success(int statusCode, Header[] headers, String response) {
		// TODO Auto-generated method stub
		JSONObject object = null;
		ObjectModel model = null;
		try
		{
			object = new JSONObject(response);
			if (object.getString("Status").equalsIgnoreCase("ok"))
			{
				object = object.getJSONObject("Content");
				model = ParseJSON(object);
				_activity.setModel(model);
				_activity.InitializeComponent();
			}
		}
		catch (JSONException e)
		{
			
		}
	}

	@Override
	public void Failure(int statusCode, Throwable error, String content) {
		// TODO Auto-generated method stub
	}

}
