package com.rateit.androidapplication.http.handlers.custom;

import java.util.UUID;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.rateit.androidapplication.MainActivity;
import com.rateit.androidapplication.http.handlers.IResponseHandler;

public class LoginHandler implements IResponseHandler {
	private Context context;
	
	
	public LoginHandler(Context _context)
	{
		context = _context;
	}
	private class UserInfo
	{
		public UUID ID;
		public String Username;
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
				userInfo = null;
			else
			{
				userInfo = new UserInfo();
				JSONObject user = object.getJSONObject("User");
				userInfo.ID = UUID.fromString(user.getString("ID"));
				userInfo.Username = user.getString("Username");				
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
	}

	@Override
	public void Success(int statusCode, Header[] headers, String response) {
		JSONObject object = null;
		try
		{
			object = new JSONObject(response);
			if (object.getString("Status").equalsIgnoreCase("ok"))
				object = object.getJSONObject("Content");
		}
		catch (JSONException e)
		{
		}
		Log.i("awdwad", response);
		ValidationUserResult result = ParseResponse(object);
		
		switch (result.Status)
		{
		case 0:
		{
			Toast.makeText(context, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
			break;
		}
		case 1:
		{
			Intent intent = new Intent(context, MainActivity.class);
			context.startActivity(intent);
			break;
		}
		case 2:
		{
			Toast.makeText(context, "Учетная запись не активирована", Toast.LENGTH_LONG).show();
			break;
		}
		case 3:
		{
			Toast.makeText(context, "Данный пользователь удален", Toast.LENGTH_LONG).show();
			break;
		}
		}
	}

	@Override
	public void Failure(int statusCode, Throwable error, String content) {
		Log.i("awdawd", Integer.toString(statusCode));
	}
}
