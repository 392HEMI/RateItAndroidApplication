package com.rateit.androidapplication.http.handlers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rateit.androidapplication.RateItAndroidApplication;

public class JsonStatusedResponseHandler<T> extends JsonResponseHandler<T> 
{
	public JsonStatusedResponseHandler(RateItAndroidApplication application, Class<T> _cls, IJsonResponseHandler<T> _handler)
	{
		super(application, _cls, _handler);
	}	
	
	public JsonStatusedResponseHandler(RateItAndroidApplication application, Class<T> _cls, IJsonResponseHandler<T> _handler, boolean responseArray)
	{
		super(application, _cls, _handler, responseArray);
	}
	
	@Override
	public void onSuccess(int statusCode, JSONObject object)
	{
		boolean valid = true;
		try
		{
			if (object.getString("Status").equalsIgnoreCase("ok"))
				object = object.getJSONObject("Content");
		}
		catch (JSONException e)
		{
			valid = false;
		}
		
		// Если это JSONObject
		if (valid)
			super.onSuccess(statusCode, object);		
		
		// Если это возможно массив
		if (!valid)
		{
			valid = true;
			JSONArray array = null;
			try
			{
				array = object.getJSONArray("Content");
			}
			catch (JSONException e)
			{
				valid = false;
			}
			if (valid)
				super.onSuccess(statusCode, array);
		}
	}
}
