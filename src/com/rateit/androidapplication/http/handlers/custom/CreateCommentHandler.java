package com.rateit.androidapplication.http.handlers.custom;

import java.util.UUID;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rateit.androidapplication.ObjectActivity;
import com.rateit.androidapplication.R;
import com.rateit.androidapplication.RateItAndroidApplication;
import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.models.Comment;
import com.rateit.androidapplication.models.User;

public class CreateCommentHandler implements IResponseHandler {
	private ObjectActivity activity;
	private ViewGroup parent;
	public CreateCommentHandler(ObjectActivity _activity, ViewGroup _parent)
	{
		activity = _activity;
		parent = _parent;
	}
	
	@Override
	public void Start()
	{
		
	}
	@Override
	public void Success(int statusCode, Header[] headers, String response)
	{
		boolean status = false;
		JSONObject object = null;
		try
		{
			object = new JSONObject(response);
			status = object.getBoolean("Status");
		}
		catch (JSONException e) 
		{
			status = false;
		}
		if (!status)
			return;
		
		
		try
		{
			JSONArray array = object.getJSONArray("Content");
			
			int commentsCount = array.length();
			Comment[] comments = new Comment[commentsCount];
			Comment com;
			JSONObject comment;
			JSONObject user;
			
			RateItAndroidApplication application = (RateItAndroidApplication)activity.getApplication();
			UUID userID = application.getUser().getID();
			int myComment = -1;
			
			for (int i = 0; i < commentsCount; i++)
			{
				comment = array.getJSONObject(i);
				user = comment.getJSONObject("User");
				com = new Comment();
				User usr = new User();
				com.ID = comment.getInt("ID");
				com.Likes = comment.getInt("Likes");
				com.Like = comment.isNull("Like") ? null : comment.getBoolean("Like");
				com.Text = comment.getString("Text");
				usr.ID = UUID.fromString(user.getString("ID"));
				usr.Name = user.getString("Name");
				usr.Surname = user.getString("Surname");
				usr.Avatar = user.getString("Avatar");
				
				com.User = usr;
				
				if (usr.ID == userID)
					myComment = i;
			}
			
			if (myComment > 0)
			{
				com = comments[myComment];
				for (int i = myComment - 1; i >= 0; i--)
					comments[i] = comments[i + 1];
				comments[0] = com;
			}
			
		    LayoutInflater inflater = activity.getLayoutInflater();
		    View v;
			for (Comment c : comments)
			{
				v = inflater.inflate(R.layout.comment_row_layout, null);
				v = activity.setupCommentRow(v, c);
				parent.addView(v);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void Failure(int statusCode, Throwable error, String content)
	{
		
	}

}
