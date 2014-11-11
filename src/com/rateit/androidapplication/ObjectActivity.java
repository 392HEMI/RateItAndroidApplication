package com.rateit.androidapplication;

import java.io.File;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.rateit.androidapplication.http.HttpClient;
import com.rateit.androidapplication.http.handlers.custom.CreateCommentHandler;
import com.rateit.androidapplication.http.handlers.custom.IFileDownloadCompleteHandler;
import com.rateit.androidapplication.http.handlers.custom.ObjectHandler;
import com.rateit.androidapplication.models.Comment;
import com.rateit.androidapplication.models.ObjectModel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class ObjectActivity extends Activity {
	private RateItAndroidApplication application;
	
	private HttpClient invoker;
	
	private TabHost tabhost;
	private LinearLayout commentList;
	
	private ObjectModel model;
	public void setModel(ObjectModel _model)
	{
		model = _model;
	}
	
	private void setObjectRating()
	{
	}
	
	private void setCommentRating(int commentID, boolean up)
	{
		
	}
	
	private void createComment(String text)
	{
		RateItAndroidApplication application = (RateItAndroidApplication)getApplication();
		UUID userID = application.getUser().getID();
		JSONObject object = new JSONObject();
		
		try
		{
			object.put("UserID", userID);
			object.put("Text", text);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		invoker.PostJSON("createComment", Integer.toString(model.ID), object, new CreateCommentHandler(this, commentList));
	}

	public View setupCommentRow(View view, final Comment comment)
	{
	    final ImageView avatar = (ImageView)view.findViewById(R.id.avatar);
	    TextView name = (TextView)view.findViewById(R.id.name);
	    TextView surname = (TextView)view.findViewById(R.id.surname);
	    TextView likesCount = (TextView)view.findViewById(R.id.likesCount);
	    ImageButton upButton = (ImageButton)view.findViewById(R.id.upRating);
	    ImageButton downButton = (ImageButton)view.findViewById(R.id.downRating);
	    TextView text = (TextView)view.findViewById(R.id.text);
	    
	    surname.setText(comment.User.Surname);
	    name.setText(comment.User.Name);
	    likesCount.setText(Integer.toString(comment.Likes));
	    text.setText(comment.Text);
	    
	    if (comment.Like == null || !comment.Like)
	    {
		    upButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
	    }
	    else
	    	upButton.setImageResource(R.drawable.backgroud_transperent);
	    
	    
	    if (comment.Like == null || comment.Like)
	    {
		    downButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
	    }
	    else
	    	downButton.setImageResource(R.drawable.backgroud_transperent);
	    
	    invoker.DownloadImage("http://192.168.1.101/Images/no_avatar.png", "no_avatar.png", "avatars", new IFileDownloadCompleteHandler() {
			@Override
			public void Complete(File file) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
				avatar.setImageBitmap(bitmap);
			}
		});
	    return view;
	}
	
	private void showPropertiesTab()
	{
		
	}

	private void showCommentsTab(Comment[] comments)
	{ 
		commentList.removeAllViewsInLayout();
		LayoutInflater inflater = getLayoutInflater();
		View v;
		for (int i = 0; i < comments.length; i++)
		{
			v = inflater.inflate(R.layout.comment_row_layout, commentList, false);
			v = setupCommentRow(v, comments[i]);
			commentList.addView(v);
		}
	}
	private void showImagesTab(String[] images)
	{
	}
	
	public void InitializeComponent()
	{
		commentList = (LinearLayout)findViewById(R.id.tab2);
		
		final ImageView logo = (ImageView)findViewById(R.id.logo);
		TextView title = (TextView)findViewById(R.id.titleText);
		TextView rating = (TextView)findViewById(R.id.rating);
		RatingBar ratingStars = (RatingBar)findViewById(R.id.ratingStarts);
		
		title.setText(model.Title);
		double _rate = model.Rating == null ? 0.0 : model.Rating;
		rating.setText(Double.toString(_rate));
		ratingStars.setRating((float)_rate);
		TabHost tabhost = (TabHost)findViewById(android.R.id.tabhost);
		
		tabhost.setup();
		LayoutInflater inflater = getLayoutInflater();
		
		View parameters_view = inflater.inflate(R.layout.parameters_tab_layout, null);
		View comments_view = inflater.inflate(R.layout.comments_tab_layout, null);
		View images_view = inflater.inflate(R.layout.images_tab_layout, null);
	
		TabHost.TabSpec tabspec;
		tabspec = tabhost.newTabSpec("tag1");
		tabspec.setIndicator(parameters_view);
		tabspec.setContent(R.id.tab1);
		tabhost.addTab(tabspec);


		tabspec = tabhost.newTabSpec("tag2");
		tabspec.setIndicator(comments_view);
		tabspec.setContent(R.id.tab2);
		tabhost.addTab(tabspec);
		
		tabspec = tabhost.newTabSpec("tag3");
		tabspec.setIndicator(images_view);
		tabspec.setContent(R.id.tab3);
		tabhost.addTab(tabspec);
		
		tabhost.setCurrentTabByTag("tag1");
		
		tabhost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equalsIgnoreCase("tag1"))
					showPropertiesTab();
				else if (tabId.equalsIgnoreCase("tag2"))
					showCommentsTab(model.Comments);
				else if (tabId.equalsIgnoreCase("tag3"))
					showImagesTab(model.Images);
			}
		});
		
	    invoker.DownloadImage("http://192.168.1.101/Images/" + model.Logo, model.Logo, "avatars", new IFileDownloadCompleteHandler() {
			@Override
			public void Complete(File file) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
				logo.setImageBitmap(bitmap);
			}
		});
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object);
        application = (RateItAndroidApplication)getApplication();
        invoker = application.getHttpClient();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int objID = bundle.getInt("objectID");
        invoker.executeAction("GetObject", Integer.toString(objID), new ObjectHandler(this));
    }
}
