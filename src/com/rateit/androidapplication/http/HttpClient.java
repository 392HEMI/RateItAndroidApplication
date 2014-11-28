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
import com.rateit.androidapplication.http.handlers.IHttpResponseHandler;
import com.rateit.androidapplication.http.handlers.IJsonResponseHandler;
import com.rateit.androidapplication.http.handlers.JsonResponseHandler;
import com.rateit.androidapplication.http.handlers.custom.IFileDownloadCompleteHandler;
import com.rateit.androidapplication.http.handlers.custom.ImageResponseHandler;
import com.rateit.androidapplication.http.handlers.custom.LoginHandler;
import com.loopj.android.http.*;

public final class HttpClient {
	private Context context;
	private final String API_ADDRESS = "http://192.168.1.101/WebAPI/api/call/";
	private final String AUTH_ADDRESS = "http://192.168.1.101/WebAPI/api/account/";
	private static AsyncHttpClient client;
	
	private RateItAndroidApplication application;

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
	
	private String getAPIActionUrl(String action, String params)
	{
		if (params.charAt(0) != '/')
			params = '/' + params;
		else
		{
			boolean b = false;
			int i;
			for (i = 1; i < params.length(); i++)
				if (params.charAt(i) == '/')
					b = true;
				else
				{
					b = false;
					break;
				}
			params = params.substring(i - 1);	
		}
		return action + params;
	}
	
	private void get(String url, ResponseHandlerInterface responseHandler)
	{
		client.get(url, responseHandler);
	}
	
	private void get(String url, RequestParams requestParams, ResponseHandlerInterface responseHandler)
	{
		client.get(url, requestParams, responseHandler);
	}	

	private void post(String url, ResponseHandlerInterface responseHandler)
	{
		client.post(url, responseHandler);
	}
	
	private void post(String url, RequestParams requestParams, ResponseHandlerInterface responseHandler)
	{
		client.post(url, requestParams, responseHandler);
	}
	
	private void post(String url, JSONObject object, ResponseHandlerInterface responseHandler)
	{
		ByteArrayEntity entity = null;
		try
		{
			entity = new ByteArrayEntity(object.toString().getBytes("UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		client.post(context, url, entity, "application/json", responseHandler);
	}
	
	public void get(String action, String urlParams, IHttpResponseHandler responseHandler)
	{
		String url = getAPIActionUrl(action, urlParams);
		HttpResponseHandler handler = new HttpResponseHandler(application, responseHandler);
		get(url, handler);
	}
	
	public void get(String action, String urlParams, RequestParams requestParams, IHttpResponseHandler responseHandler)
	{
		String url = getAPIActionUrl(action, urlParams);
		HttpResponseHandler handler = new HttpResponseHandler(application, responseHandler);
		get(url, requestParams, handler);
	}
	
	public <T> void get(Class<T> cls, String action, String urlParams, RequestParams requestParams, IJsonResponseHandler<T> responseHandler)
	{
		String url = getAPIActionUrl(action, urlParams);
		JsonResponseHandler<T> handler = new JsonResponseHandler<T>(application, cls, responseHandler);
		get(url, requestParams, handler);
	}
	
	public <T> void post(Class<T> cls, String action, String params, JSONObject object, IJsonResponseHandler<T> responseHandler)
	{
		String url = getAPIActionUrl(action, params);
		JsonResponseHandler<T> handler = new JsonResponseHandler<T>(application, cls, responseHandler); 
		post(url, object, handler);
	}
	
	
	// Авторизация
	public void Autorize(String username, String password, IHttpResponseHandler handler)
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
		Class<?> cls = LoginHandler.UserInfo.class;
		post(url, obj, new JsonResponseHandler(application, cls, handler));
	}
	
	public void SignOut(IHttpResponseHandler responseHandler)
	{
		String url = AUTH_ADDRESS + "SignOut";
		HttpResponseHandler handler = new HttpResponseHandler(application, responseHandler);
		get(url, handler);
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