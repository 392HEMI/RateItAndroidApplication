package com.rateit.androidapplication.http.handlers.custom;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.rateit.androidapplication.ObjectActivity;
import com.rateit.androidapplication.http.handlers.IResponseHandler;
import com.rateit.androidapplication.models.ObjectModel;

public class ObjectHandler implements IResponseHandler {
	private ObjectModel ParseJSON(JSONObject object)
	{
		ObjectModel model = new ObjectModel();
		try {
			model.ID = object.getInt("ID");
			model.Title = object.getString("Title");
			model.Logo = object.getString("Logo");
			model.Rating = object.isNull("Rating") ? null : object.getDouble("Rating");
			model.MyMark = object.isNull("MyMark") ? null : object.getInt("MyMark");
			
			//JSONArray properties = object.getJSONArray("");
			JSONArray images = object.getJSONArray("Images");
			
			//int properties_count = properties.length();
			int images_count = images.length();
			
			//model.Properties = new Object[properties_count];
			model.Images = new String[images_count];
			
			//JSONObject user;
			//for (int i = 0; i < properties_count; i++)
			//{
				
			//}
			for (int i = 0; i < images_count; i++)
			{
				model.Images[i] = images.getString(i);
			}
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
	
	private ObjectActivity _activity;
	public ObjectHandler(ObjectActivity activity)
	{
		_activity = activity;
	}
	
	@Override
	public void Start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Success(int statusCode, Header[] headers, String response) {
		JSONObject object = null;
		ObjectModel model = null;
		try
		{
			object = new JSONObject(response);
			if (object.getString("Status").equalsIgnoreCase("ok"))
			{
				object = object.getJSONObject("Content");
				model = ParseJSON(object);
				_activity.setModel(model);
				_activity.InitializeComponent();
			}
		}
		catch (JSONException e)
		{
		}
	}

	@Override
	public void Failure(int statusCode, Throwable error, String content) {
	}

}
