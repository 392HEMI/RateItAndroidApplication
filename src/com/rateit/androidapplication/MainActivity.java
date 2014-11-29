package com.rateit.androidapplication;

import com.rateit.androidapplication.R;
import com.rateit.androidapplication.http.HttpClient;
import com.rateit.androidapplication.http.handlers.IJsonResponseHandler;
import com.rateit.androidapplication.http.handlers.custom.CategoriesHandler;
import com.rateit.androidapplication.http.handlers.custom.ObjectsHandler;
import com.rateit.androidapplication.http.handlers.custom.TypesHandler;
import com.rateit.androidapplication.models.CategoriesModel;
import com.rateit.androidapplication.models.ObjectsModel;
import com.rateit.androidapplication.models.TypesModel;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import java.util.Stack;

public class MainActivity extends RateItActivity {
	private ListView listView;
	private Stack<IMethod> actionSeq;
	private HttpClient httpClient;
	
	private IMethod backMethod;
	
	private void InitializeComponent()
	{
		listView = (ListView)findViewById(R.id.listView1);
		actionSeq = new Stack<IMethod>();
		
		RateItAndroidApplication application = (RateItAndroidApplication)getApplication();
		httpClient = application.getHttpClient();
		
		backMethod = new GoToCategories(null);
	}
	
	public void invokeGetCategories(Integer parentID, boolean saveLastState)
	{
		if (saveLastState)
			actionSeq.push(backMethod);
		
		String action = "getcategories";
		String params = (parentID == null ? "" : Integer.toString(parentID));
		IJsonResponseHandler<CategoriesModel> handler = new CategoriesHandler(this, listView);
		httpClient.get(CategoriesModel.class, action, params, handler);
		
		backMethod = new GoToCategories(parentID);
		
	}
	public void invokeGetTypes(int categoryID, boolean saveLastState)
	{
		if (saveLastState)
			actionSeq.push(backMethod);
		String action = "gettypes";
		String params = Integer.toString(categoryID);
		IJsonResponseHandler<TypesModel> handler = new TypesHandler(this, listView);
		httpClient.get(TypesModel.class, action, params, handler);
		
		backMethod = new GoToTypes(categoryID);
	}
	public void invokeGetObjects(int typeID, boolean saveLastState)
	{
		if (saveLastState)
			actionSeq.push(backMethod);	
		String action = "getobjects";
		String params = Integer.toString(typeID);
		IJsonResponseHandler<ObjectsModel> handler = new ObjectsHandler(this, listView);
		httpClient.get(ObjectsModel.class, action, params, handler);
		
		backMethod = new GoToObjects(typeID);
		
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeComponent();
        invokeGetCategories(null, true);
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
