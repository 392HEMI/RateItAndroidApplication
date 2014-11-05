package com.rateit.androidapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rateit.androidapplication.R;
import com.rateit.androidapplication.models.ObjectsModel;

public class ObjectsAdapter extends BaseAdapter {
	  Context ctx;
	  LayoutInflater lInflater;
	  ObjectsModel.GeneralObject[] objects;

	  public ObjectsAdapter(Context context, ObjectsModel.GeneralObject[] objs) {
	    ctx = context;
	    objects = objs;
	    lInflater = (LayoutInflater) ctx
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }

	  @Override
	  public int getCount() {
	    return objects.length;
	  }

	  @Override
	  public Object getItem(int position) {
	    return objects[position];
	  }

	  @Override
	  public long getItemId(int position) {
	    return position;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View view = convertView;
	    if (view == null) {
	      view = lInflater.inflate(R.layout.rowlayout, parent, false);
	    }

	    ObjectsModel.GeneralObject obj = (ObjectsModel.GeneralObject)getItem(position);
	    TextView label = (TextView)view.findViewById(R.id.label);
	    label.setText(obj.title);
	    return view;
	  }
}
