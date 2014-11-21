package com.rateit.androidapplication.http.handlers;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rateit.androidapplication.RateItAndroidApplication;


public final class HttpResponseHandler extends AsyncHttpResponseHandler {
	private IResponseHandler handler;
	private RateItAndroidApplication application;
	
	public HttpResponseHandler(IResponseHandler _handler, RateItAndroidApplication _application)
	{
		handler = _handler;
		application = _application;
	}

	@Override
    public void onStart() {
        handler.Start();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String response) {
    	handler.Success(statusCode, headers, response);
    }

    @Override
    public void onFailure(int statusCode, Throwable error, String content) {
    	if (statusCode == 401)
    	{
    		application.Autorize(true);
    		return;
    	}
    	handler.Failure(statusCode, error, content);
    }
}
