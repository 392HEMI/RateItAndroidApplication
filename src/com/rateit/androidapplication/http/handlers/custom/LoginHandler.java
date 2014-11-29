package com.rateit.androidapplication.http.handlers.custom;

import android.content.Intent;
import android.widget.Toast;

import com.rateit.androidapplication.AccountActivity;
import com.rateit.androidapplication.MainActivity;
import com.rateit.androidapplication.RateItAndroidApplication;
import com.rateit.androidapplication.User;
import com.rateit.androidapplication.http.handlers.IJsonResponseHandler;
import com.rateit.androidapplication.models.ValidationUserResult;

public class LoginHandler implements IJsonResponseHandler<ValidationUserResult> {
	private AccountActivity activity;
	private RateItAndroidApplication application;
	
	public LoginHandler(AccountActivity _activity)
	{
		activity = _activity;
		application = (RateItAndroidApplication)activity.getApplication();
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
	public void onSuccess(int statusCode, ValidationUserResult[] array) {
		
	}
	
	@Override
	public void onSuccess(int statusCode, ValidationUserResult result) {
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
		activity.unlock();
	}

	@Override
	public void onFailure(int statusCode, Throwable error, String content) {
		Toast.makeText(activity, "Невозможно подключиться к службе", Toast.LENGTH_LONG).show();
	}
}
