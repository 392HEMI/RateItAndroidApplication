package com.rateit.androidapplication;

import android.app.Activity;
import android.app.Dialog;

public class RateItActivity extends Activity {
	private Dialog dlg;
	public void disableView()
	{
		if (dlg == null)
			dlg = new Dialog(this, R.style.full_screen_dialog);
		dlg.setCancelable(false);
		dlg.setContentView(R.layout.loading_dialog_layout);
		dlg.show();
	}
	
	public void enableView()
	{
		if (dlg != null)
			dlg.hide();
	}
}
