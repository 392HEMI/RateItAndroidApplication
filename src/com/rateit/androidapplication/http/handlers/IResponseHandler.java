package com.rateit.androidapplication.http.handlers;

import org.apache.http.Header;

public interface IResponseHandler {
	public void Start();
	public void Success(int statusCode, Header[] headers, String response);
	public void Failure(int statusCode, Throwable error, String content);
}