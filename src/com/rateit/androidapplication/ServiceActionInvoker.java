package com.rateit.androidapplication;

import com.rateit.androidapplication.handlers.IResponseHandler;
import com.loopj.android.http.*;

public final class ServiceActionInvoker {
	private final String API_ADDRESS = "http://192.168.1.101/api/call/";
	private AsyncHttpClient client;
	
	private void executeRequest(String url, IResponseHandler handler)
	{
		client.get(url, new HttpResponseHandler(handler));
	}

	public void executeAction(String action, String params, IResponseHandler handler)
	{
		String url = API_ADDRESS + action + "/" + params;
		executeRequest(url, handler);
	}

	public ServiceActionInvoker()
	{
		client = new AsyncHttpClient();
	}
}