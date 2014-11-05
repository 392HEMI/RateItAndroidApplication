package com.rateit.androidapplication.handlers;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rateit.androidapplication.MainActivity;
import com.rateit.androidapplication.ObjectActivity;
import com.rateit.androidapplication.adapters.ObjectsAdapter;
import com.rateit.androidapplication.models.ObjectsModel;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

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
			if (jsonObj.getString("Status").equalsIgnoreCase("ok"))
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
	
    private void attachAdapter(final ListView listView, ObjectsModel.GeneralObject[] objects)
    {
    	ObjectsAdapter adapter = new ObjectsAdapter(_activity, objects);
    	listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ObjectsModel.GeneralObject obj = (ObjectsModel.GeneralObject)parent.getItemAtPosition(position);
				Intent intent = new Intent(_activity.getApplicationContext(), ObjectActivity.class);
				intent.putExtra("objectID", obj.id);
				_activity.startActivity(intent);
			}
		});
    	listView.setAdapter(adapter);
    }
}
