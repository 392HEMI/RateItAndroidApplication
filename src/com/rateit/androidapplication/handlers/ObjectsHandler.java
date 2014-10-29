package com.rateit.androidapplication.handlers;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rateit.androidapplication.R;
import com.rateit.androidapplication.models.ObjectsModel;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ObjectsHandler implements IResponseHandler {
	private Activity _activity;
	private ListView _listView;
	
	public ObjectsHandler(Activity activity, ListView listView)
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
			JSONObject type = jsonObj.getJSONObject("Type");
			ObjectsModel model = new ObjectsModel();
			
			int typeID = type.getInt("ID");
			String typeTitle = type.getString("Title");
			ObjectsModel.Type typeObj = model.new Type(typeID, typeTitle);
			
			JSONArray objects = jsonObj.getJSONArray("Objects");
			JSONObject object;
			int count = objects.length();
			
			ObjectsModel.GeneralObject[] objs = new ObjectsModel.GeneralObject[count];
			
			for (int i = 0; i < objects.length(); i++)
			{
				object = objects.getJSONObject(i);
				
				int id = object.getInt("ID");
				String title = object.getString("Title");
				
				objs[i] = model.new GeneralObject(id, title);
			}
			model.type = typeObj;
			model.objects = objs;
			attachAdapter(_listView, model.objects);
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
	
    private void attachAdapter(final ListView listView, ObjectsModel.GeneralObject[] types)
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
