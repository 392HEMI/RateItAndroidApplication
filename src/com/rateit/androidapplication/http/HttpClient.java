package com.rateit.androidapplication.http;


import java.io.UnsupportedEncodingException;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.rateit.androidapplication.RateItAndroidApplication;
import com.rateit.androidapplication.http.handlers.HttpFileResponseHandler;
import com.rateit.androidapplication.http.handlers.HttpResponseHandler;
import com.rateit.androidapplication.http.handlers.IFileResponseHandler;
import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.http.handlers.custom.IFileDownloadCompleteHandler;
import com.rateit.androidapplication.http.handlers.custom.ImageResponseHandler;
import com.loopj.android.http.*;

public final class HttpClient {
	private Context context;
	private final String API_ADDRESS = "http://192.168.1.101/WebAPI/api/call/";
	private final String AUTH_ADDRESS = "http://192.168.1.101/WebAPI/api/account/";
	private static AsyncHttpClient client;
	
	private RateItAndroidApplication application;
	
	private void executeRequest(String url, IResponseHandler handler)
	{
		client.get(url, new HttpResponseHandler(handler, application));
	}

	private void downloadFile(String url, String[] allowedTypes, IFileResponseHandler handler)
	{
		client.get(url, new HttpFileResponseHandler(handler, allowedTypes));
	}

	public void DownloadImage(String url, String name, String directory, IFileDownloadCompleteHandler handler)
	{	
		String[] allowedTypes = new String[] { "image/png" };
		ImageResponseHandler ih = new ImageResponseHandler(context, name, directory, handler);
		downloadFile(url, allowedTypes, ih);
	}
	
	public void executeAction(String action, String params, IResponseHandler handler)
	{
		String url = API_ADDRESS + action + "/" + params;
		executeRequest(url, handler);
	}
	
	public void PostJSON(String action, String params, JSONObject object, IResponseHandler handler)
	{
		String url = API_ADDRESS + action + "/" + params;
		ByteArrayEntity entity = null;
		try
		{
			entity = new ByteArrayEntity(object.toString().getBytes("UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		client.post(context, url, entity, "application/json", new HttpResponseHandler(handler, application));
	}
	
	public void Autorize(String username, String password, IResponseHandler handler)
	{
		String url = AUTH_ADDRESS + "login";
		JSONObject obj = new JSONObject();
		try
		{
			obj.put("username", username);
			obj.put("password", password);
		} 
		catch (JSONException e)
		{
		}
		ByteArrayEntity entity = null;
		try
		{
			entity = new ByteArrayEntity(obj.toString().getBytes("UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
		}
		if (entity != null)
			client.post(context, url, entity, "application/json", new HttpResponseHandler(handler, application));
	}
	
	public HttpClient(Context _context, RateItAndroidApplication _application)
	{
		client = new AsyncHttpClient();
		context = _context;
		application = _application;
		
		PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(myCookieStore);
	}
}