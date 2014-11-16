package com.rateit.androidapplication.http.handlers.custom;

import java.util.UUID;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.widget.Toast;

import com.rateit.androidapplication.AccountActivity;
import com.rateit.androidapplication.MainActivity;
import com.rateit.androidapplication.RateItAndroidApplication;
import com.rateit.androidapplication.User;
import com.rateit.androidapplication.http.handlers.IResponseHandler;

public class LoginHandler implements IResponseHandler {
	private AccountActivity activity;
	private RateItAndroidApplication application;
	
	public LoginHandler(AccountActivity _activity)
	{
		activity = _activity;
		application = (RateItAndroidApplication)activity.getApplication();
	}
	private class UserInfo
	{
		public UUID ID;
		public String Name;
	}
	
	private class ValidationUserResult
	{
		public int Status;
		public UserInfo User;
	}
	
	private ValidationUserResult ParseResponse(JSONObject object)
	{
		if (object == null)
			return null;
		ValidationUserResult result = new ValidationUserResult();
		boolean valid = true;
		try
		{
			result.Status = object.getInt("Status");
			UserInfo userInfo;
			if (object.isNull("User"))
				valid = false;
			else
			{
				userInfo = new UserInfo();
				JSONObject user = object.getJSONObject("User");
				userInfo.ID = UUID.fromString(user.getString("ID"));
				userInfo.Name = user.getString("Name");
				if (userInfo.Name == null || userInfo.Name.equalsIgnoreCase(""))
					valid = false;
				result.User = userInfo;
			}
		}
		catch (JSONException e)
		{
			valid = false;
		}
		if (valid)
			return result;
		return null;
	}
	
	@Override
	public void Start() {
		activity.disableView();
	}

	@Override
	public void Success(int statusCode, Header[] headers, String response) {
		JSONObject object = null;
		try
		{
			object = new JSONObject(response);
			if (object.getString("Status").equalsIgnoreCase("ok"))
				object = object.getJSONObject("Content");
			else
				return;
		}
		catch (JSONException e)
		{
		}
		
		ValidationUserResult result = ParseResponse(object);
		int status = (result == null || result.User == null) ? 0 : result.Status;
		
		switch (status)
		{
			case 1:
				User user = new User(result.User.ID, result.User.Name);
				application.setUser(user);
				
				Intent intent = new Intent(activity, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);
				break;
			case 2:
				Toast.makeText(activity, "Учетная запись не активирована", Toast.LENGTH_LONG).show();
				break;
			case 3:
				Toast.makeText(activity, "Данный пользователь удален", Toast.LENGTH_LONG).show();
				break;
			default:
			case 0:
				Toast.makeText(activity, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
				break;
		}
		activity.enableView();
	}

	@Override
	public void Failure(int statusCode, Throwable error, String content) {
		Toast.makeText(activity, "Невозможно подключиться к службе", Toast.LENGTH_LONG).show();
		activity.enableView();
	}
}
