package com.rateit.androidapplication.handlers;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.rateit.androidapplication.models.ObjectModel;

public class ObjectHandler implements IResponseHandler {
	private ObjectModel ParseJSON(JSONObject object)
	{
		ObjectModel model = new ObjectModel();
		try {
			model.ID = object.getInt("ID");
			model.Title = object.getString("Title");
			model.Rating = object.getDouble("Rating");
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return model;
	}
	
	
	@Override
	public void Start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Success(int statusCode, Header[] headers, String response) {
		// TODO Auto-generated method stub
		JSONObject object = null;
		ObjectModel model = null;
		try
		{
			object = new JSONObject(response);
			if (object.getString("Status").equalsIgnoreCase("ok"))
			{
				object = object.getJSONObject("Content");
				model = ParseJSON(object);
			}
		}
		catch (JSONException e)
		{
			
		}
	}

	@Override
	public void Failure(int statusCode, Throwable error, String content) {
		// TODO Auto-generated method stub
		
	}

}
