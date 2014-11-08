package com.rateit.androidapplication;

import java.io.File;
import org.apache.http.Header;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.rateit.androidapplication.handlers.IFileResponseHandler;

public class HttpFileResponseHandler extends BinaryHttpResponseHandler {
	
	private IFileResponseHandler handler;
	
	public HttpFileResponseHandler(IFileResponseHandler _handler, String[] allowedTypes)
	{
		super(allowedTypes);
		handler = _handler;
	}
	
    @Override
    public void onSuccess(byte[] imageData) {
    	handler.Success(imageData);
    }

    @Override
    public void onFailure(Throwable e) {
        // Response failed :(
    }
}
