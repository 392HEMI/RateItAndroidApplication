package com.rateit.androidapplication;

import java.util.Stack;

import android.app.Application;

public class RateItAndroidApplication extends Application {

	private HttpMaster httpInvoker;
	private Stack<IMethod> actionSeq;

	public HttpMaster getHttpInvoker()
	{
		if (httpInvoker == null)
			httpInvoker = new HttpMaster(getApplicationContext());
		return httpInvoker;
	}	
	public void setHttpInvoker(HttpMaster invoker)
	{
		if (invoker == null)
			return;
		httpInvoker = invoker;
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
