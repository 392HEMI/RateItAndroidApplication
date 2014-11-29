package com.rateit.androidapplication.http.handlers.custom;

import com.rateit.androidapplication.MainActivity;
import com.rateit.androidapplication.http.handlers.IJsonResponseHandler;
import com.rateit.androidapplication.models.TypesModel;
import com.rateit.androidapplication.models.Type;

import com.rateit.androidapplication.adapters.TypeAdapter;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TypesHandler implements IJsonResponseHandler<TypesModel> {
	private Activity _activity;
	private ListView _listView;
	
	public TypesHandler(Activity activity, ListView listView)
	{
		_activity = activity;
		_listView = listView;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onFinish() {
		
	}
	
	@Override
	public void onSuccess(int statusCode, TypesModel[] model)
	{
		
	}

	@Override
	public void onSuccess(int statusCode, TypesModel model) {
		attachAdapter(_listView, model.types);
	}

	@Override
	public void onFailure(int statusCode, Throwable error, String content) {
		
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
