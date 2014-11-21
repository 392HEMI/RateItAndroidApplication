package com.rateit.androidapplication;

import com.rateit.androidapplication.http.HttpClient;
import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.http.handlers.custom.SignOutHandler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends LockableActivity {
	private RateItAndroidApplication application;
	private HttpClient httpClient;
	private User user;
	
	private void InitializeComponent()
	{
		final TextView account = (TextView)findViewById(R.id.account);
		final Button exitBtn = (Button)findViewById(R.id.exitBtn);
		final SettingsActivity activity = this;
		
		account.setText(user.getName());
		exitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				IResponseHandler handler = new SignOutHandler(activity);
				httpClient.SignOut(handler);
			}
		});
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.settings_layout);
        application = (RateItAndroidApplication)getApplication();
        httpClient = application.getHttpClient();
        user = application.getUser();
        
        InitializeComponent();
    }
}
