package com.example.rateitapplication;

import com.example.rateitapplication.handlers.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
//import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListView listView;
	private ServiceActionInvoker actionInvoker;
	
	private void InitializeComponent()
	{
		listView = (ListView)findViewById(R.id.listView1);
		actionInvoker = new ServiceActionInvoker();
	}
	
	private void invokeGetCategories(Integer parentID)
	{
		String action = "getcategories";
		String params = (parentID == null ? "" : Integer.toString(parentID));
		IResponseHandler handler = new CategoriesHandler(this, listView);
		actionInvoker.executeAction(action, params, handler);
	}
	private void invokeGetTypes(int categoryID)
	{
		String action = "gettypes";
		String params = Integer.toString(categoryID);
		IResponseHandler handler = new TypesHandler(this, listView);
		actionInvoker.executeAction(action, params, handler);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeComponent();
        Log.e("APPLICATION", "PREREQUEST");
        invokeGetTypes(10);
        Log.e("APPLICATION", "POSTREQUEST");
    }
}
