package com.rateit.androidapplication;

import com.rateit.androidapplication.R;
import com.rateit.androidapplication.handlers.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
//import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.Stack;

public class MainActivity extends Activity {
	private ListView listView;
	private Stack<IMethod> actionSeq;
	private ServiceActionInvoker actionInvoker;
	
	private IMethod backMethod;
	
	private void InitializeComponent()
	{
		listView = (ListView)findViewById(R.id.listView1);
		actionSeq = new Stack<IMethod>();
		actionInvoker = new ServiceActionInvoker();
		backMethod = new GoToCategories(null);
	}
	
	public void invokeGetCategories(Integer parentID, boolean saveLastState)
	{
		if (saveLastState)
			actionSeq.push(backMethod);
		Log.i("INVOKE", "GetCategories");
		String action = "getcategories";
		String params = (parentID == null ? "" : Integer.toString(parentID));
		IResponseHandler handler = new CategoriesHandler(this, listView);
		actionInvoker.executeAction(action, params, handler);
		
		backMethod = new GoToCategories(parentID);
		
	}
	public void invokeGetTypes(int categoryID, boolean saveLastState)
	{
		if (saveLastState)
			actionSeq.push(backMethod);	
		Log.i("INVOKE", "GetTypes");
		String action = "gettypes";
		String params = Integer.toString(categoryID);
		IResponseHandler handler = new TypesHandler(this, listView);
		actionInvoker.executeAction(action, params, handler);
		
		backMethod = new GoToTypes(categoryID);
	}
	public void invokeGetObjects(int typeID, boolean saveLastState)
	{
		if (saveLastState)
			actionSeq.push(backMethod);	
		Log.i("INVOKE", "GetTypes");
		String action = "gettypes";
		String params = Integer.toString(typeID);
		IResponseHandler handler = new ObjectsHandler(this, listView);
		actionInvoker.executeAction(action, params, handler);
		
		backMethod = new GoToObjects(typeID);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeComponent();
        Log.e("APPLICATION", "PREREQUEST");
        invokeGetCategories(null, true);
        Log.e("APPLICATION", "POSTREQUEST");
    }
    
    @Override
    public void onBackPressed() {
    	if (actionSeq.empty())
    		return;
    	IMethod method = actionSeq.pop();
    	method.Execute();
    }
    
   private class GoToCategories implements IMethod
   {
	   private Integer _id;
	   public GoToCategories(Integer id)
	   {
		   _id = id;
	   }
	   @Override
	   public void Execute() {
		   invokeGetCategories(_id, false);
	   }
   }
   private class GoToTypes implements IMethod     
   {
	   private Integer _id;
	   public GoToTypes(Integer id)
	   {
		   _id = id;
	   }
	   @Override
	   public void Execute() {
		   invokeGetTypes(_id, false);
	   }
   }
   private class GoToObjects implements IMethod       
   {
	   private Integer _id;
	   public GoToObjects(Integer id)
	   {
		   _id = id;
	   }
	   @Override
	   public void Execute() {
		   invokeGetObjects(_id, false);
	   }
   }
}
