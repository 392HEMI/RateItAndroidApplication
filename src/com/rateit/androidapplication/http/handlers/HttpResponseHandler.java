package com.rateit.androidapplication.http.handlers;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;


public final class HttpResponseHandler extends AsyncHttpResponseHandler {
	private IResponseHandler handler;
	
	public HttpResponseHandler(IResponseHandler _handler)
	{
		handler = _handler;
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
    	handler.Failure(statusCode, error, content);
    }
}
