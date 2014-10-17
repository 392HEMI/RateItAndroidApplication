package com.example.rateitapplication;

import com.example.rateitapplication.handlers.CategoriesHandler;
import com.example.rateitapplication.handlers.IResponseHandler;

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
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeComponent();
        Log.e("APPLICATION", "PREREQUEST");
        invokeGetCategories(null);
        Log.e("APPLICATION", "POSTREQUEST");
    }
    
    //private void attachAdapter(final ListView listView)
    //{
    //    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
    //            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
    //            "Linux", "OS/2" };
            // use your custom layout
    //        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
    //            R.layout.rowlayout, R.id.label, values);
    //        listView.setAdapter(adapter);
    //}
}
