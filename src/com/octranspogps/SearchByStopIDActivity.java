package com.octranspogps;

import net.wakame.octranspodb.db.DbHelper;

import com.octranspoBLL.OCUtility;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SearchByStopIDActivity extends Activity {
	private final Context self = this;
	private final String LOG_TAG = "SearchByStopIDActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_by_stop_id);

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread paramThread,
					Throwable paramThrowable) {
				Log.e(LOG_TAG,
						"Uncaught Exception! - "
								+ OCUtility.getErrorMessage(paramThrowable));
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_by_stop_id, menu);
		return true;
	}

	public void callListRoute(View v) {
 		Intent intent = new Intent(self, ListRouteActivity.class);
		EditText mEdit = (EditText) findViewById(R.id.et_bus_stop_number);
	

		if (mEdit.getText().toString().length() > 0) {
			if (isValidBusStop(mEdit.getText().toString())) {
				intent.putExtra("stopId",
						Long.valueOf(mEdit.getText().toString()));
				try {
					startActivity(intent);
				} finally {

				}

			} else {
				// invalid bus stop
				new OCUtility().showError(this, 1);
			}
		} else {
			// bus stop number is not entered.
			new OCUtility().showError(this, 2);
		}
	}

	
	public void onDestroy(Bundle savedInstanceState) {
		super.onDestroy();

}
	
	@Override
	  public void onResume() {
	     super.onResume();
	     Log.i("OnResume is called", "");
	  }
	
	private boolean isValidBusStop(String pBusStopId) {
		
		DbHelper mDbHelper = new DbHelper(this);
		return mDbHelper.isValidBusStop(pBusStopId);

	}
}
