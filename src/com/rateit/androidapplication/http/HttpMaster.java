package com.rateit.androidapplication.http;


import java.io.UnsupportedEncodingException;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import android.content.Context;

import com.rateit.androidapplication.http.handlers.HttpFileResponseHandler;
import com.rateit.androidapplication.http.handlers.HttpResponseHandler;
import com.rateit.androidapplication.http.handlers.IFileResponseHandler;
import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.http.handlers.custom.IFileDownloadCompleteHandler;
import com.rateit.androidapplication.http.handlers.custom.ImageResponseHandler;
import com.loopj.android.http.*;

public final class HttpMaster {
	private Context context;
	private final String API_ADDRESS = "http://192.168.1.101/api/call/";
	private static AsyncHttpClient client;
	
	
	private void executeRequest(String url, IResponseHandler handler)
	{
		client.get(url, new HttpResponseHandler(handler));
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
		String url = API_ADDRESS + action + params;
		ByteArrayEntity entity = null;
		try
		{
			entity = new ByteArrayEntity(object.toString().getBytes("UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		client.post(context, url, entity, "application/json", new HttpResponseHandler(handler));
	}
	
	public HttpMaster(Context _context)
	{
		client = new AsyncHttpClient();
		context = _context;
	}
}