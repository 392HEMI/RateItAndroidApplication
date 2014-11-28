package com.rateit.androidapplication.http.handlers.custom;

import org.json.JSONException;
import org.json.JSONObject;

import com.rateit.androidapplication.ObjectActivity;
import com.rateit.androidapplication.http.handlers.IResponseHandler;

public class SetCommentRatingHandler implements IResponseHandler {
	private ObjectActivity activity;
	
	public SetCommentRatingHandler(ObjectActivity _activity)
	{
		activity = _activity;
	}
	
	
	@Override
	public void Start() {
		activity.lock();
	}

	@Override
	public void Success(int statusCode, String response) {
		boolean valid = false;
		try
		{
			JSONObject object = new JSONObject(response);
			if (object.getString("Status").equalsIgnoreCase("ok"))
				valid = object.getBoolean("Content");
		}
		catch (JSONException e)
		{
		}
		if (!valid)
			return;
		activity.refreshComments();
	}

	@Override
	public void Failure(int statusCode, Throwable error, String content) {
		activity.unlock();
	}
	
}
