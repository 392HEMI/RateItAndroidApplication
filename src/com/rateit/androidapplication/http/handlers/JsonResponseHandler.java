package com.rateit.androidapplication.http.handlers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

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
		Class<?> fieldType = field.getType();
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
			} else if (fieldType.equals(UUID.class))
			{
				String stringValue = json.getString(fieldName);
				UUID value = UUID.fromString(stringValue);
				field.set(object, value);
			} else if (fieldType.isArray())
			{
				Object value = null;
				if (!json.isNull(fieldName))
					value = parseArray(fieldType.getComponentType(), json.getJSONArray(fieldName));
				else
					value = java.lang.reflect.Array.newInstance(fieldType.getComponentType(), 0);
				field.set(object, value);
			}
			else
			{
				Object value = null;
				if (!json.isNull(fieldName))
					value = parseObject(json.getJSONObject(fieldName), field.getType());
				field.set(object, value);
			}
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}
	
	private <Q> Q[] parseArray(Class<Q> cls, JSONArray array)
	{
		int count = array.length();
		Q[] result = (Q[])java.lang.reflect.Array.newInstance(cls, count);
		Q obj;
		JSONObject object;
		for (int i = 0; i < count; i++)
		{
			try
			{
				object = array.getJSONObject(i);
				obj = parseObject(object, cls);
				Log.i("awdawd", obj == null ? "null" : "not null");
				result[i] = obj;
			} 
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}

	private <Q> Q parseObject(JSONObject object, Class<Q> cls) throws JSONException
	{
		Q result = null;
		try
		{
			Constructor<Q> constr = null;
			Constructor<Q>[] constructors = (Constructor<Q>[])cls.getConstructors();
			for (Constructor<?> c : constructors)
				if (c.getParameterTypes().length == 0)
				{
					constr = (Constructor<Q>)c;
					break;
				}
			if (constr == null)
				throw new IllegalAccessException();
			result = constr.newInstance();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		
		if (result == null)
			return null;
		
		for (Field f : cls.getFields())
			setFieldValue(f, result, object);
		
		return result;
	}

	private T parseResponse(JSONObject object) throws JSONException
	{
		return parseObject(object, cls);
	}
	
	@Override
	public void onStart()
	{
		handler.onStart();
	}
	
	@Override
	public void onSuccess(int statusCode, JSONArray array)
	{
		T[] collection = parseArray(cls, array);
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
			e.printStackTrace();
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
