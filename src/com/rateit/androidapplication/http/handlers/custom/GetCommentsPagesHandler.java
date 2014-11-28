package com.rateit.androidapplication.http.handlers.custom;

import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rateit.androidapplication.ObjectActivity;
import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.models.Comment;
import com.rateit.androidapplication.models.User;

public class GetCommentsPagesHandler implements IResponseHandler {
	private ObjectActivity activity;
	private UUID userID;
	
	public GetCommentsPagesHandler(ObjectActivity _activity)
	{
		activity = _activity;
		userID = activity.GetRateItApplication().getUser().getID();
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
	public void Success(int statusCode, String response) {
		JSONArray array = null;
		try
		{
			JSONObject obj  = new JSONObject(response);
			if (obj.getString("Status").equalsIgnoreCase("ok"))
				array = obj.getJSONArray("Content");
		}
		catch (JSONException e)
		{
		}
		
		if (array == null)
			return;
		
		int count = array.length();
		ArrayList<Comment> comments = new ArrayList<Comment>(count);
		
		boolean valid = true;
		try
		{
			JSONObject commentObj;
			Comment comment = new Comment();
			for (int i = 0; i < count; i++)
			{
				commentObj = array.getJSONObject(i);
				comment = getComment(commentObj);
				if (comment.User.ID.equals(userID))
				{
					activity.setUserComment(comment);
					continue;
				}
				comments.add(comment);
			}
		}
		catch (Exception e)
		{
			valid = false;
		}
		if (valid)
			activity.setComments(comments, true);
	}

	@Override
	public void Failure(int statusCode, Throwable error, String content) {
		activity.unlock();
	}

}
