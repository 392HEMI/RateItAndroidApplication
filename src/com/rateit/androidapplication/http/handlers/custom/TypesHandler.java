package com.rateit.androidapplication.http.handlers.custom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rateit.androidapplication.MainActivity;
import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.models.TypesModel;
import com.rateit.androidapplication.models.Category;
import com.rateit.androidapplication.models.Type;

import com.rateit.androidapplication.adapters.TypeAdapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

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
	public void Success(int statusCode, String response) {
		try
		{
			JSONObject jsonObj = new JSONObject(response);
			if (jsonObj.getString("Status").equalsIgnoreCase("ok"))
				jsonObj = jsonObj.getJSONObject("Content");
			else
			{
				// error
			}
			Log.i("PREIVNOKE", "");
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
			
			JSONArray types = jsonObj.getJSONArray("types");
			JSONObject type;
			
			int count = types.length();
			model.types = new Type[count];
			
			for (int i = 0; i < types.length(); i++)
			{
				type = types.getJSONObject(i);
				
				int id = type.getInt("id");
				String title = type.getString("title");
				boolean hasObjects = type.getBoolean("hasObjects");
				
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
    	TypeAdapter adapter = new TypeAdapter(_activity, types);		
    	listView.setAdapter(adapter);
    	listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> types, View view, int position, long id) {
				Type type = (Type)types.getItemAtPosition(position);
				MainActivity activity = (MainActivity)_activity;
				activity.invokeGetObjects(type.id, true);
			}
		});
    }
}
