package com.rateit.androidapplication;

import com.rateit.androidapplication.http.HttpClient;
import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.http.handlers.custom.LoginHandler;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AccountActivity extends RateItActivity {
	private RateItAndroidApplication application;
	private HttpClient httpClient;
	
	private int page;
	
	private EditText loginBox;
	private EditText passwordBox;
	private Button loginBtn;
	private Button registrationBtn;
	private Button noPasswordBtn;
	
	private void initializeComponentForLoginPage()
	{
		loginBox = (EditText)findViewById(R.id.login);
		passwordBox = (EditText)findViewById(R.id.password);
		loginBtn = (Button)findViewById(R.id.loginBtn);
		registrationBtn = (Button)findViewById(R.id.registration);
		noPasswordBtn = (Button)findViewById(R.id.noPassword);
		
		final AccountActivity activity = this;
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = loginBox.getText().toString();
				String password = passwordBox.getText().toString();
				IResponseHandler handler = new LoginHandler(activity);
				httpClient.Autorize(username, password, handler);
			}
		});
		
		registrationBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				page = 2;
				setContentView(R.layout.register_user_layout);
			}
		});
		
		noPasswordBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				page = 3;
				setContentView(R.layout.no_password_layout);
			}
		});		
	}
	private void initializeComponentForRegisterUser()
	{
		
	}
	private void initializeComponentForNoPassword()
	{
		
	}
	
	private void InitializeComponent(int resId)
	{
		switch (resId)
		{
		case R.layout.login_layout:
		{
			initializeComponentForLoginPage();
			break;
		}
		case R.layout.register_user_layout:
		{
			initializeComponentForRegisterUser();
			break;
		}
		case R.layout.no_password_layout:
		{
			initializeComponentForNoPassword();
			break;
		}
		}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        
        application = (RateItAndroidApplication)getApplication();
        httpClient = application.getHttpClient();
        
        InitializeComponent(R.layout.login_layout);
    }
    
    @Override
	public void onBackPressed()
    {
    	if (page == 1)
    		return;
    	int resId = R.layout.login_layout;
    	setContentView(resId);
    	page = 1;
    	InitializeComponent(resId);
    }
}