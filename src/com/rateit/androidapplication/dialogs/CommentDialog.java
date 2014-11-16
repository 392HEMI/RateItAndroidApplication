package com.rateit.androidapplication.dialogs;

import com.rateit.androidapplication.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class CommentDialog extends Dialog {

	private EditText commentText;
	private Button okButton;
	
	private void InitializeComponent()
	{
		Window window = getWindow();
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		commentText = (EditText)findViewById(R.id.commentText);
		okButton = (Button)findViewById(R.id.okBtn);
		
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hide();
			}
		});
	}
	
	public CommentDialog(Context context) {
		super(context, R.style.full_screen_dialog);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setCancelable(false);
	    
	    setContentView(R.layout.comment_editor_dialog_layout);
	    InitializeComponent();
	}
}
