package com.rateit.androidapplication;

import com.rateit.androidapplication.models.ObjectModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class ObjectActivity extends Activity {
	private RateItAndroidApplication application;
	
	private ServiceActionInvoker invoker;
	
	private TabHost tabhost;
	private LinearLayout tab1;
	
	private ObjectModel model;

	private View setupCommentRow(View view, final ObjectModel.Comment comment)
	{
	    ImageView avatar = (ImageView)view.findViewById(R.id.avatar);
	    TextView name = (TextView)view.findViewById(R.id.name);
	    TextView surname = (TextView)view.findViewById(R.id.surname);
	    TextView likesCount = (TextView)view.findViewById(R.id.likesCount);
	    ImageButton upButton = (ImageButton)view.findViewById(R.id.upRating);
	    ImageButton downButton = (ImageButton)view.findViewById(R.id.downRating);
	    TextView text = (TextView)view.findViewById(R.id.text);
	    
	    surname.setText(comment.User.Surname);
	    name.setText(comment.User.Name);
	    likesCount.setText(comment.Likes);
	    text.setText(comment.Text);
	    // download icon
	    
	    upButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	    
	    downButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	    return view;
	}
	
	private void showPropertiesTab()
	{

	}
	
	private void showCommentsTab(ObjectModel.Comment[] comments)
	{
		ListView listView = new ListView(getApplicationContext());
		LayoutInflater inflater = getLayoutInflater();
		
		View v;
		for (int i = 0; i <= comments.length; i++)
		{
			v = inflater.inflate(R.layout.comment_row_layout, null);
			setupCommentRow(v, comments[i]);
			listView.addHeaderView(v);
		}
		
		tab1.addView(listView);		
	}
	
	private void showImagesTab(String[] images)
	{
		
	}

	private boolean GetModel(int objID)
	{
		//invoker.executeAction("GetObject", Integer.toString(objID), new ObjectHandler());
		return true;
	}
	private void InitializeComponent()
	{
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
				if (tabId.equalsIgnoreCase("tab1"))
					showPropertiesTab();
				else if (tabId.equalsIgnoreCase("tab2"))
					showCommentsTab(model.Comments);
				else if (tabId.equalsIgnoreCase("tab3"))
					showImagesTab(model.Images);
			}
		});
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object);
        
        application = (RateItAndroidApplication)getApplication();
        invoker = application.getHttpInvoker();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int objID = bundle.getInt("objID");
        
        if (GetModel(objID))
        	InitializeComponent();
        else
        {
        	// on get model error
        	return;
        }
    }
}
