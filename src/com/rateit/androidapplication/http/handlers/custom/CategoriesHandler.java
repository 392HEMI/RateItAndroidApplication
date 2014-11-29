package com.rateit.androidapplication.http.handlers.custom;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.rateit.androidapplication.adapters.CategoryAdapter;
import com.rateit.androidapplication.http.handlers.IJsonResponseHandler;
import com.rateit.androidapplication.models.CategoriesModel;
import com.rateit.androidapplication.models.SubCategory;
import com.rateit.androidapplication.MainActivity;

public class CategoriesHandler implements IJsonResponseHandler<CategoriesModel> {
	private Activity _activity;
	private ListView _listView;
	
	public CategoriesHandler(Activity activity, ListView listView)
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
	public void onSuccess(int statusCode, CategoriesModel[] model) {

	}
	
	@Override
	public void onSuccess(int statusCode, CategoriesModel model) {
		attachAdapter(_listView, model.SubCategories);
	}
	
	@Override
	public void onFailure(int statusCode, Throwable error, String content) {
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
				if (subCategory.IsLast)
					activity.invokeGetTypes(subCategory.ID, true);
				else
					activity.invokeGetCategories(subCategory.ID, true);
			}
		});
    }
}
