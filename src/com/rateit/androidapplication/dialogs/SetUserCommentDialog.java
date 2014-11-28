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

public class SetUserCommentDialog extends Dialog {
	private EditText commentText;
	private Button okBtn;
	private Button cancelBtn;
	
	private RateItAndroidApplication application;
	private int objectID;
	private IResponseHandler handler;
	private boolean isNew;
	
	private void InitializeComponent()
	{
		Window window = getWindow();
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		commentText = (EditText)findViewById(R.id.commentText);
		okBtn = (Button)findViewById(R.id.okBtn);
		cancelBtn = (Button)findViewById(R.id.cancelBtn);
		
		if (isNew)
			okBtn.setText(R.string.comment_editor_dialog_layout_update_button);
		
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setComment();
				hide();
			}
		});
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hide();
			}
		});
	}
	
	public SetUserCommentDialog(RateItActivity activity, int _objectID, IResponseHandler _handler, boolean _isNew) {
		super(activity, R.style.full_screen_dialog);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setCancelable(false);
	    
	    setContentView(R.layout.comment_editor_dialog_layout);
	    application = activity.GetRateItApplication();
	    objectID = _objectID;
	    handler = _handler;
	    isNew = _isNew;
	    InitializeComponent();
	}
	
	private void setComment()
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
		httpClient.PostJSON("SetUserComment", Integer.toString(objectID), object, handler);
	}
}
