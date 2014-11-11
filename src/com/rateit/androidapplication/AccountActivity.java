package com.rateit.androidapplication;

import com.rateit.androidapplication.http.HttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AccountActivity extends Activity {
	private RateItAndroidApplication application;
	private HttpClient client;
	
	private EditText login;
	private EditText password;
	private Button loginBtn;
	private Button registrationBtn;
	private Button noPasswordBtn;
	
	private void InitializeComponent()
	{
		login = (EditText)findViewById(R.id.login);
		password = (EditText)findViewById(R.id.password);
		loginBtn = (Button)findViewById(R.id.loginBtn);
		registrationBtn = (Button)findViewById(R.id.registration);
		noPasswordBtn = (Button)findViewById(R.id.noPassword);
		
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
        client = application.getHttpClient();
        InitializeComponent();
    }
}