package com.rateit.androidapplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

import com.rateit.androidapplication.components.CustomScrollView;
import com.rateit.androidapplication.dialogs.CreateCommentDialog;
import com.rateit.androidapplication.http.HttpClient;
import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.http.handlers.custom.CreateCommentHandler;
import com.rateit.androidapplication.http.handlers.custom.GetCommentsHandler;
import com.rateit.androidapplication.http.handlers.custom.IFileDownloadCompleteHandler;
import com.rateit.androidapplication.http.handlers.custom.ObjectHandler;
import com.rateit.androidapplication.models.Comment;
import com.rateit.androidapplication.models.ObjectModel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class ObjectActivity extends RateItActivity {
	private RateItAndroidApplication application;
	
	private HttpClient httpClient;
	
	private CustomScrollView scrollView;
	private TabHost tabhost;
	private LinearLayout commentList;
	private Button commentBtn;
	
	private View loadingView;
	private boolean loading;
	
	
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
	
	public View setupUserCommentRow(View view, final Comment comment)
	{
	    final ImageView avatar = (ImageView)view.findViewById(R.id.avatar);
	    TextView name = (TextView)view.findViewById(R.id.name);
	    TextView surname = (TextView)view.findViewById(R.id.surname);
	    TextView likesCount = (TextView)view.findViewById(R.id.likesCount);
	    TextView text = (TextView)view.findViewById(R.id.text);
	    
	    surname.setText(comment.User.Surname);
	    name.setText(comment.User.Name);
	    likesCount.setText(Integer.toString(comment.Likes));
	    text.setText(comment.Text);
	    
	    httpClient.DownloadImage("http://192.168.1.101/Images/no_avatar.png", "no_avatar.png", "avatars", new IFileDownloadCompleteHandler() {
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
	    	upButton.setImageResource(android.R.color.transparent);
	    
	    
	    if (comment.Like == null || comment.Like)
	    {
		    downButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
	    }
	    else
	    	downButton.setImageResource(android.R.color.transparent);
	    
	    httpClient.DownloadImage("http://192.168.1.101/Images/no_avatar.png", "no_avatar.png", "avatars", new IFileDownloadCompleteHandler() {
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
	
	private void showImagesTab(String[] images)
	{
	}
	
	public void InitializeComponent()
	{
		final ObjectActivity thisActivity = this;
		
		LayoutInflater inflater = getLayoutInflater();
		
		// Init vars
		comments = new ArrayList<Comment>();
		commentsPage = 0;
		commentsPageSize = 10;
		loading = false;
		
		// Init components
		scrollView = (CustomScrollView)findViewById(R.id.scrollView);
		commentList = (LinearLayout)findViewById(R.id.commentList);
		commentBtn = (Button)findViewById(R.id.commentBtn);
		
		loadingView = inflater.inflate(R.layout.loading_row_layout, scrollView, false);
		
		final ImageView logo = (ImageView)findViewById(R.id.logo);
		final TextView title = (TextView)findViewById(R.id.titleText);
		final TextView rating = (TextView)findViewById(R.id.rating);
		final RatingBar ratingStars = (RatingBar)findViewById(R.id.ratingStarts);
		
		// Setup fields
		title.setText(model.Title);
		double _rate = model.Rating == null ? 0.0 : model.Rating;
		rating.setText(Double.toString(_rate));
		ratingStars.setRating((float)_rate);
		TabHost tabhost = (TabHost)findViewById(android.R.id.tabhost);
		
	    httpClient.DownloadImage("http://192.168.1.101/Images/" + model.Logo, model.Logo, "avatars", new IFileDownloadCompleteHandler() {
			@Override
			public void Complete(File file) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
				logo.setImageBitmap(bitmap);
			}
		});
	    
		// Setup tabhost
		tabhost.setup();
		
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
					showCommentsTab();
				else if (tabId.equalsIgnoreCase("tag3"))
					showImagesTab(model.Images);
			}
		});
		
		final TabHost th = tabhost;
		scrollView.setOnScrollEndListener(new CustomScrollView.OnScrollEndListener() {
			@Override
			public void onScrollDown(View v) {
				int tab = th.getCurrentTab();
				if (tab == 1)
				{
					getComments(commentsPage + 1, commentsPageSize);
				}
			}
		});
		
		
		commentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				IResponseHandler handler = new CreateCommentHandler(thisActivity);
				CreateCommentDialog dlg = new CreateCommentDialog(thisActivity, model.ID, handler);
				dlg.show();
			}
		});
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.object_view_layout);
        application = (RateItAndroidApplication)getApplication();
        httpClient = application.getHttpClient();
        
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int objID = bundle.getInt("objectID");
        httpClient.executeAction("GetObject", Integer.toString(objID), new ObjectHandler(this));
    }
    
    
    //////////// Comments
    
    private int commentsPage;
    private int commentsPageSize;
    
    private Comment userComment;
    private ArrayList<Comment> comments;
     
    public void setUserComment(Comment comment)
    {
    	userComment = comment;
    }
    
    public void addComments(Collection<Comment> _comments, boolean incPage, boolean refreshView)
    {
		for (Comment c : _comments)
			if (userComment == null || c.User.ID != userComment.User.ID)
				comments.add(c);
		
		if (refreshView)
			displayComments();
		if (incPage)
			commentsPage = commentsPage + 1;
    }
    
    public void displayComments()
    {
		commentList.removeAllViewsInLayout();
		LayoutInflater inflater = getLayoutInflater();
		View v;
		
		if (userComment != null)
		{
			v = inflater.inflate(R.layout.user_comment_row_layout, commentList, false);
			v = setupUserCommentRow(v, userComment);
			commentList.addView(v);
		}
		
		for (Comment c : comments)
		{
			v = inflater.inflate(R.layout.comment_row_layout, commentList, false);
			v = setupCommentRow(v, c);
			commentList.addView(v);
		}
    }   

    private void getComments(int pageNumber, int pageSize)
    {
    	JSONObject object = new JSONObject();
		try
		{
			object.put("PageNumber", pageNumber);
			object.put("PageSize", pageSize);
		}
		catch (JSONException e)
		{
		}
		httpClient.PostJSON("GetComments", Integer.toString(model.ID), object, new GetCommentsHandler(this));
    }

    
	private void showCommentsTab()
	{
		commentsPage = 1;
		getComments(commentsPage, commentsPageSize);
	}
}