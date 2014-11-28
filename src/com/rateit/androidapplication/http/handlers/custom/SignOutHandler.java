package com.rateit.androidapplication.http.handlers.custom;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import com.rateit.androidapplication.RateItAndroidApplication;
import com.rateit.androidapplication.SettingsActivity;
import com.rateit.androidapplication.http.handlers.IResponseHandler;

public class SignOutHandler implements IResponseHandler {
	private SettingsActivity activity;
	
	public SignOutHandler(SettingsActivity _activity)
	{
		activity = _activity;
	}
	
	@Override
	public void Start() {
		activity.lock();
	}

	@Override
	public void Success(int statusCode, String response) {
		JSONObject obj = null;
		boolean valid = true;
		boolean success = false;
		try
		{
			obj = new JSONObject(response);
			if (obj.getString("Status").equalsIgnoreCase("ok"))
				success = obj.getBoolean("Content");
		}
		catch (JSONException ex)
		{
			valid = false;
		}
		
		if (!valid)
			return;
		
		if (!success)
		{
			Toast.makeText(activity, "Неудалось выйти из системы", Toast.LENGTH_LONG).show();
		}
		
		RateItAndroidApplication application = (RateItAndroidApplication)activity.getApplication();
		application.Autorize(true);
	}

	@Override
	public void Failure(int statusCode, Throwable error, String content) {
		activity.unlock();
		Toast.makeText(activity, "Неудалось выполнить запрос", Toast.LENGTH_LONG).show();
	}
}
