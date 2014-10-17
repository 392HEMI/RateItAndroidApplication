package com.example.rateitapplication.handlers;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.rateitapplication.R;
import com.example.rateitapplication.models.CategoriesModel;
import com.example.rateitapplication.models.Category;
import com.example.rateitapplication.models.SubCategory;

public class CategoriesHandler implements IResponseHandler {
	private Activity _activity;
	private ListView _listView;
	
	public CategoriesHandler(Activity activity, ListView listView)
	{
		_activity = activity;
		_listView = listView;
	}
	
	@Override
	public void Start() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void Success(int statusCode, Header[] headers, String response) {
		Log.e("APPLICATION", response);
		try
		{
			JSONObject jsonObj = new JSONObject(response);
			CategoriesModel model = new CategoriesModel();
			
			if (jsonObj.isNull("category"))
			{
				model.category = null;
			}
			else
			{
				JSONObject category = jsonObj.getJSONObject("category");
				int id = category.getInt("ID");
				String title = category.getString("Title");
				model.category = new Category(id, title);
			}
			
			if (jsonObj.isNull("parent"))
			{
				model.parent = null;
			}
			else
			{
				model.parent = jsonObj.getInt("parent");
			}
			
			JSONArray subCategories = jsonObj.getJSONArray("subCategories");
			JSONObject subCategory;
			
			int count = subCategories.length();
			model.subCategories = new SubCategory[count];
			
			for (int i = 0; i < subCategories.length(); i++)
			{
				subCategory = subCategories.getJSONObject(i);
				
				int id = subCategory.getInt("ID");
				String title = subCategory.getString("Title");
				boolean isLast = subCategory.getBoolean("IsLast");
				
				model.subCategories[i] = new SubCategory(id, title, isLast);
			}
			attachAdapter(_listView, model.subCategories);
		}
		catch (JSONException e)
		{
			Log.e("EXEPTION " + getClass(), e.getMessage());
		}
	}
	@Override
	public void Failure(int statusCode, Throwable error, String content) {
		// TODO Auto-generated method stub
		Log.e("APPLICATION", error.getMessage());
	}
	
    private void attachAdapter(final ListView listView, SubCategory[] subCategories)
    {
    	int count = subCategories.length;
    	String[] strings = new String[count];
    	for (int i = 0; i < subCategories.length; i++)
    	{
    		strings[i] = subCategories[i].title;
    	}
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(
    			_activity,
    			R.layout.rowlayout,
    			R.id.label,
    			strings);
    	listView.setAdapter(adapter);
    }
}
