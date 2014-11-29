package com.rateit.androidapplication.http.handlers.custom;

import com.rateit.androidapplication.ObjectActivity;
import com.rateit.androidapplication.http.handlers.IJsonResponseHandler;

public class SetCommentRatingHandler implements IJsonResponseHandler<SetCommentRatingHandler.Response> {
	private ObjectActivity activity;
	
	public class Response
	{
		public boolean Status;
		
		public Response()
		{
		}
	}
	
	public SetCommentRatingHandler(ObjectActivity _activity)
	{
		activity = _activity;
	}
	
	@Override
	public void onStart() {
		activity.lock();
	}
	
	@Override
	public void onFinish() {
		activity.unlock();
	}

	@Override
	public void onSuccess(int statusCode, Response[] response) {
		
	}
	
	@Override
	public void onSuccess(int statusCode, Response response) {
		if (!response.Status)
			return;
		activity.refreshComments();
	}

	@Override
	public void onFailure(int statusCode, Throwable error, String content) {
	}
	
}
