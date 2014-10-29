package com.rateit.androidapplication.handlers;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.rateit.androidapplication.adapters.CategoryAdapter;
import com.rateit.androidapplication.models.CategoriesModel;
import com.rateit.androidapplication.models.Category;
import com.rateit.androidapplication.models.SubCategory;
import com.rateit.androidapplication.MainActivity;

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
		try
		{
			JSONObject jsonObj = new JSONObject(response);
			if (jsonObj.getString("Status") == "ok")
				jsonObj = jsonObj.getJSONObject("Content");
			else
			{
				// error
			}
				
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
    	CategoryAdapter adapter = new CategoryAdapter(_activity, subCategories);
    	listView.setAdapter(adapter);
    	listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SubCategory subCategory = (SubCategory)parent.getItemAtPosition(position);
				MainActivity activity = (MainActivity)_activity;
				if (subCategory.isLast)
					activity.invokeGetTypes(subCategory.id, true);
				else
					activity.invokeGetCategories(subCategory.id, true);
			}
		});
    }
}
