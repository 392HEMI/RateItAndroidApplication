package com.rateit.androidapplication.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {



	private OnScrollEndListener onScrollEndListener;
	public void setOnScrollEndListener(OnScrollEndListener listener)
	{
		onScrollEndListener = listener;
	}
	public CustomScrollView(Context context)
	{
		super(context);
	}
	
	public CustomScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}
	
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        View view = (View)getChildAt(getChildCount() - 1);
        int diff = (view.getBottom() - (getHeight() + getScrollY() + view.getTop()));
        if(diff == 0) {
        	if (onScrollEndListener != null)
        		onScrollEndListener.onScrollDown(this);
        }        
	}
	public interface OnScrollEndListener
	{
		public void onScrollDown(View v);
	}
}
