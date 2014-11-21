package com.rateit.androidapplication.dialogs;

import org.json.JSONException;
import org.json.JSONObject;

import com.rateit.androidapplication.R;
import com.rateit.androidapplication.RateItActivity;
import com.rateit.androidapplication.RateItAndroidApplication;
import com.rateit.androidapplication.http.HttpClient;
import com.rateit.androidapplication.http.handlers.IResponseHandler;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class CreateCommentDialog extends Dialog {
	private EditText commentText;
	private Button okButton;
	private RateItAndroidApplication application;
	private int objectID;
	private IResponseHandler handler;
	
	private void InitializeComponent()
	{
		Window window = getWindow();
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		commentText = (EditText)findViewById(R.id.commentText);
		okButton = (Button)findViewById(R.id.okBtn);
		
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createComment();
				hide();
			}
		});
	}
	
	public CreateCommentDialog(RateItActivity activity, int _objectID, IResponseHandler _handler) {
		super(activity, R.style.full_screen_dialog);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setCancelable(false);
	    
	    setContentView(R.layout.comment_editor_dialog_layout);
	    application = activity.GetRateItApplication();
	    objectID = _objectID;
	    handler = _handler;
	    InitializeComponent();
	}
	
	private void createComment()
	{
		String text = commentText.getText().toString();
		JSONObject object = new JSONObject();
		
		try
		{
			object.put("Text", text);
		}
		catch (JSONException e)
		{
		}
		HttpClient httpClient = application.getHttpClient();
		httpClient.PostJSON("CreateComment", Integer.toString(objectID), object, handler);
	}
}
