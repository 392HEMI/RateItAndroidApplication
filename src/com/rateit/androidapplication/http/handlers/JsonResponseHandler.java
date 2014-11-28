package com.rateit.androidapplication.http.handlers;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.rateit.androidapplication.RateItAndroidApplication;

public class JsonResponseHandler<T> extends JsonHttpResponseHandler {
	
	private Class<T> cls;
	//private boolean responseArray;
	private IJsonResponseHandler<T> handler;
	private RateItAndroidApplication application;
	
	public JsonResponseHandler(RateItAndroidApplication _application, Class<T> _cls, IJsonResponseHandler<T> _handler)
	{
		application = _application;
		cls = _cls;
		handler = _handler;
		//responseArray = false;
	}
	
	public JsonResponseHandler(RateItAndroidApplication _application, Class<T> _cls, IJsonResponseHandler<T> _handler, boolean _responseArray)
	{
		application = _application;
		cls = _cls;
		handler = _handler;
		//responseArray = _responseArray;
	}

	private void setFieldValue(Field field, Object object, JSONObject json) throws JSONException
	{
		Type fieldType = field.getType();
		String fieldName = field.getName();
		try
		{
			if (fieldType.equals(boolean.class))
			{
				boolean value = json.getBoolean(fieldName);
				field.setBoolean(object, value);
			} else if (fieldType.equals(int.class))
			{
				int value = json.getInt(fieldName);
				field.setInt(object, value);
			} else if (fieldType.equals(long.class))
			{
				long value = json.getLong(fieldName);
				field.setLong(object, value);
			} else if (fieldType.equals(double.class))
			{
				double value = json.getDouble(fieldName);
				field.setDouble(object, value);
			} else if (fieldType.equals(String.class))
			{
				String value = json.getString(fieldName);
				field.set(object, value);
			} else
			{
				Object value = parseObject(json, field.getType().getClass());
				field.set(object, value);
			}
		}
		catch (IllegalAccessException e)
		{
			
		}
		catch (IllegalArgumentException e)
		{
			
		}
	}
	
	private Collection<T> parseArray(JSONArray array)
	{
		int count = array.length();
		Collection<T> result = new ArrayList<T>(count);
		T obj;
		JSONObject object;
		for (int i = 0; i < count; i++)
		{
			try
			{
				object = array.getJSONObject(i);
				obj = parseObject(object, cls);
				result.add(obj);
			} 
			catch (JSONException e)
			{
			}
		}
		return result;
	}

	private <Q> Q parseObject(JSONObject object, Class<Q> cls) throws JSONException
	{
		Q result = null;
		try
		{
			result = cls.newInstance();
		}
		catch (InstantiationException e) 
		{
		}
		catch (IllegalAccessException e)
		{
		}
		
		if (result == null)
			return null;
		
		for (Field f : cls.getFields())
			setFieldValue(f, result, object);
		
		return result;
	}

	private T parseResponse(JSONObject object) throws JSONException
	{
		return (T)parseObject(object, cls);
	}
	
	@Override
	public void onStart()
	{
		handler.onStart();
	}
	
	@Override
	public void onSuccess(int statusCode, JSONArray array)
	{
		Collection<T> collection = parseArray(array);
		handler.onSuccess(statusCode, collection);
	}
	
	@Override
	public void onSuccess(int statusCode, JSONObject object)
	{
		T result = null;
		try
		{
			result = parseResponse(object);
		}
		catch (JSONException e)
		{
			
		}
		handler.onSuccess(statusCode, result);
	}
	
	@Override
	public void onFailure(int statusCode, Throwable e, String response)
	{
    	if (statusCode == 401)
    	{
    		application.Autorize(true);
    		return;
    	}
    	handler.onFailure(statusCode, e, response);
	}
	
	@Override
	public void onFinish()
	{
		
	}
}
