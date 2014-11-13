package com.rateit.androidapplication;

import com.rateit.androidapplication.http.HttpClient;
import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.http.handlers.custom.LoginHandler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AccountActivity extends Activity {
	private RateItAndroidApplication application;
	private HttpClient httpClient;
	
	private EditText loginBox;
	private EditText passwordBox;
	private Button loginBtn;
	private Button registrationBtn;
	private Button noPasswordBtn;
	
	private void InitializeComponent()
	{
		loginBox = (EditText)findViewById(R.id.login);
		passwordBox = (EditText)findViewById(R.id.password);
		loginBtn = (Button)findViewById(R.id.loginBtn);
		registrationBtn = (Button)findViewById(R.id.registration);
		noPasswordBtn = (Button)findViewById(R.id.noPassword);
		
		
		final Context context = this;
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = loginBox.getText().toString();
				String password = passwordBox.getText().toString();
				IResponseHandler handler = new LoginHandler(context);
				httpClient.Autorize(username, password, handler);
			}
		});
		
		registrationBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		noPasswordBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        
        application = (RateItAndroidApplication)getApplication();
        httpClient = application.getHttpClient();
        InitializeComponent();
    }
}