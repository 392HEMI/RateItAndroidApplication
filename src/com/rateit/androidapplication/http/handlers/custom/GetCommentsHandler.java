package com.rateit.androidapplication.http.handlers.custom;

import java.util.UUID;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.rateit.androidapplication.ObjectActivity;
import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.models.Comment;
import com.rateit.androidapplication.models.User;

public class GetCommentsHandler implements IResponseHandler {
	private ObjectActivity activity;
	
	public GetCommentsHandler(ObjectActivity _activity)
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
			result.Like = object.getBoolean("Like");
			JSONObject userObj = object.getJSONObject("User");
			user.ID = UUID.fromString(userObj.getString("ID"));
			user.Avatar = userObj.getString("Avatar");
			user.Name = userObj.getString("Name");
			user.Surname = userObj.getString("Surname");
		} 
		catch (JSONException e)
		{
			validObject = false;
		}
		
		if (!validObject)
			return null;
		
		result.User = user;
		return result;
	}
	
	@Override
	public void Start() {
	}

	@Override
	public void Success(int statusCode, Header[] headers, String response) {
		JSONArray array = null;
		try
		{
			JSONObject obj  = new JSONObject(response);
			if (obj.getString("Status").equalsIgnoreCase("ok"))
				array = obj.getJSONArray("Content");
		}
		catch (JSONException e)
		{
			Log.i("awadawd", e.getMessage());
		}
		
		if (array == null)
			return;
		
		int count = array.length();
		Comment[] comments = new Comment[count];
		boolean valid = true;
		try
		{
			JSONObject commentObj;
			for (int i = 0; i < count; i++)
			{
				commentObj = array.getJSONObject(i);
				comments[i] = getComment(commentObj);
			}
		}
		catch (Exception e)
		{
			valid = false;
		}
		if (valid)
			activity.addComments(comments, true);
	}

	@Override
	public void Failure(int statusCode, Throwable error, String content) {
		// TODO Auto-generated method stub
		Log.i("awdawd", content);
	}

}
