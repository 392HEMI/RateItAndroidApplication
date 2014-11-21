package com.rateit.androidapplication;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class RateItActivity extends LockableActivity {
	
	public RateItAndroidApplication GetRateItApplication()
	{
		return (RateItAndroidApplication)getApplication();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_layout, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.settings:
	        showSettings();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void showSettings()
	{
		Intent intent = new Intent(this, SettingsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}