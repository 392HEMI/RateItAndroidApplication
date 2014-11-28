package com.rateit.androidapplication.http.handlers.custom;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.rateit.androidapplication.ObjectActivity;
import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.models.Comment;
import com.rateit.androidapplication.models.User;

public class SetUserCommentHandler implements IResponseHandler {
	private ObjectActivity activity;
	public SetUserCommentHandler(ObjectActivity _activity)
	{
		activity = _activity;
	}
	
	private Comment getComment(JSONObject object)
	{
		Comment result = new Comment();
		User user = new User();
		
		boolean validObject = true;
		try
		{
			result.ID = object.getInt("ID");
			result.Text = object.getString("Text");
			result.Likes = object.getInt("Likes");
			result.Like = false;
			JSONObject userObj = object.getJSONObject("User");
			user.ID = UUID.fromString(userObj.getString("ID"));
			user.Avatar = userObj.getString("Avatar");
			user.Name = userObj.getString("Name");
			user.Surname = userObj.getString("Surname");
		} 
		catch (JSONException e)
		{
			Log.i("ml;ml", e.getMessage());
			validObject = false;
		}
		
		if (!validObject)
			return null;
		
		result.User = user;
		return result;
	}
	
	@Override
	public void Start()
	{
		activity.lock();
	}
	
	@Override
	public void Success(int statusCode, String response)
	{
		JSONObject object = null;
		boolean valid = true;
		try
		{
			object = new JSONObject(response);
			if (object.getString("Status").equalsIgnoreCase("ok"))
				object = object.getJSONObject("Content");
		}
		catch (JSONException e)
		{
			valid = false;
		}
		
		if (!valid)
			return;
		
		Comment comment = getComment(object);
		activity.setUserComment(comment);
		activity.displayComments();
		activity.unlock();
	}
	@Override
	public void Failure(int statusCode, Throwable error, String content)
	{
		activity.unlock();
	}

}
