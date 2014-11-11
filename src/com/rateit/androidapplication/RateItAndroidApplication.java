package com.rateit.androidapplication;

import java.util.Stack;

import com.rateit.androidapplication.http.HttpClient;

import android.app.Application;

public class RateItAndroidApplication extends Application {
	private HttpClient httpClient;
	private Stack<IMethod> actionSeq;
	
	private User user;

	public User getUser()
	{
		return user;
	}
	
	public void setUser(User _user)
	{
		user = _user;
	}

	public HttpClient getHttpClient()
	{
		if (httpClient == null)
			httpClient = new HttpClient(getApplicationContext());
		return httpClient;
	}
	
	public void pushBackMethod(IMethod method)
	{
		actionSeq.push(method);
	}
	public void goBack()
	{
		if (actionSeq.empty())
			return;
		IMethod method = actionSeq.pop();
		method.Execute();
	}
	
	public RateItAndroidApplication()	
	{
		super();
		actionSeq = new Stack<IMethod>();
	}
}
