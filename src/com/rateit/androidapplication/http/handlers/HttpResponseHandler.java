package com.rateit.androidapplication.http.handlers;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rateit.androidapplication.RateItAndroidApplication;


public final class HttpResponseHandler extends AsyncHttpResponseHandler {
	private IHttpResponseHandler handler;
	private RateItAndroidApplication application;
	
	public HttpResponseHandler(RateItAndroidApplication _application, IHttpResponseHandler _handler)
	{
		handler = _handler;
		application = _application;
	}

	@Override
    public void onStart() {
        handler.onStart();
    }
	
	public void onFinish()
	{
		handler.onFinish();
	}

    @Override
    public void onSuccess(int statusCode, String response) {
    	handler.onSuccess(statusCode, response);
    }

    @Override
    public void onFailure(int statusCode, Throwable error, String content) {
    	if (statusCode == 401)
    	{
    		application.Autorize(true);
    		return;
    	}
    	handler.onFailure(statusCode, error, content);
    }
}
