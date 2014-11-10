package com.rateit.androidapplication.handlers;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateCommentHandler implements IResponseHandler {
	@Override
	public void Start()
	{
		
	}
	@Override
	public void Success(int statusCode, Header[] headers, String response)
	{
		boolean isJSON = true;
		try
		{
			JSONObject object = new JSONObject(response);
		} catch (JSONException e) 
		{
			isJSON = false;
		}
		if (!isJSON)
		{
			object.
		}
	}
	@Override
	public void Failure(int statusCode, Throwable error, String content)
	{
		
	}
}
