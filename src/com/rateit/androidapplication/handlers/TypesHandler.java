package com.rateit.androidapplication.handlers;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rateit.androidapplication.R;
import com.rateit.androidapplication.models.TypesModel;
import com.rateit.androidapplication.models.Category;
import com.rateit.androidapplication.models.Type;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TypesHandler implements IResponseHandler {
	private Activity _activity;
	private ListView _listView;
	
	public TypesHandler(Activity activity, ListView listView)
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
		try
		{
			JSONObject jsonObj = new JSONObject(response);
			TypesModel model = new TypesModel();
			
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
			
			JSONArray subCategories = jsonObj.getJSONArray("types");
			JSONObject subCategory;
			
			int count = subCategories.length();
			model.types = new Type[count];
			
			for (int i = 0; i < subCategories.length(); i++)
			{
				subCategory = subCategories.getJSONObject(i);
				
				int id = subCategory.getInt("id");
				String title = subCategory.getString("title");
				boolean hasObjects = subCategory.getBoolean("hasObjects");
				
				model.types[i] = new Type(id, title, hasObjects);
			}
			attachAdapter(_listView, model.types);
		}
		catch (JSONException e)
		{
			Log.e("EXEPTION " + getClass(), e.getMessage());
		}
	}

	@Override
	public void Failure(int statusCode, Throwable error, String content) {
		// TODO Auto-generated method stub
		
	}
	
    private void attachAdapter(final ListView listView, Type[] types)
    {
    	int count = types.length;
    	String[] strings = new String[count];
    	for (int i = 0; i < types.length; i++)
    	{
    		strings[i] = types[i].title;
    	}
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(
    			_activity,
    			R.layout.rowlayout,
    			R.id.label,
    			strings);
    	listView.setAdapter(adapter);
    }
}
