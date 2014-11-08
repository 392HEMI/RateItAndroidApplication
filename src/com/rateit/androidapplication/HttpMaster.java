package com.rateit.androidapplication;


import android.content.Context;

import com.rateit.androidapplication.handlers.IFileResponseHandler;
import com.rateit.androidapplication.handlers.IResponseHandler;
import com.rateit.androidapplication.handlers.ImageResponseHandler;
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
	public HttpMaster(Context _context)
	{
		client = new AsyncHttpClient();
		context = _context;
	}
}