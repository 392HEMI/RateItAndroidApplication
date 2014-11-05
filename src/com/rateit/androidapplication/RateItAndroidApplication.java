package com.rateit.androidapplication;

import java.util.Stack;

import android.app.Application;

public class RateItAndroidApplication extends Application {

	private ServiceActionInvoker httpInvoker;
	private Stack<IMethod> actionSeq;

	public ServiceActionInvoker getHttpInvoker()
	{
		if (httpInvoker == null)
			httpInvoker = new ServiceActionInvoker();
		return httpInvoker;
	}	
	public void setHttpInvoker(ServiceActionInvoker invoker)
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
		httpInvoker = new ServiceActionInvoker();
		actionSeq = new Stack<IMethod>();
	}
}
