package com.octranspogps;

import net.wakame.octranspodb.db.DbHelper;
import net.wakame.octranspodb.db.StopsDao;

import com.octranspoBLL.OCUtility;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SearchByStopIDActivity extends Activity {
	final Context self = this;
	final String LOG_TAG = "SearchByStopIDActivity";

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
		boolean isValid = false;

		DbHelper mDbHelper = new DbHelper(this);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		//user clear the data, but the DB still exists. Need to re-populate the data
		if(!mDbHelper.isTableExists("STOPS", db)){
			mDbHelper.createStops(db);
		};
		Cursor c = StopsDao.getStopsByCode(db, pBusStopId);
		//if the bus stop exists in the DB, return true
		if (c.getCount() > 0) {
			isValid = true;
		}
		mDbHelper.close();

		return isValid;
	}
}
