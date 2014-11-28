package com.rateit.androidapplication;

import android.app.Activity;
import android.app.Dialog;

public class LockableActivity extends Activity {
	private Dialog dlg;
	
	
	public void lock()
	{
		if (dlg == null)
			dlg = new Dialog(this, R.style.full_screen_dialog);
		dlg.setCancelable(false);
		dlg.setContentView(R.layout.loading_dialog_layout);
		dlg.show();
	}
	
	public void unlock()
	{
		if (dlg != null)
			dlg.hide();
	}
}
