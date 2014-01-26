package com.octranspogps;

//import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListActivityMain extends ListActivity {

	private final Context self = this;
	private ProgressDialog progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// set exception handler to generate an error log
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread thread, Throwable e) {
				Log.e("Error !!! ", e.toString());
				StackTraceElement[] elements = e.getStackTrace();
				for (StackTraceElement element : elements) {
					Log.e("Error !!! ", element.toString());
				}

			}
		});
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		SimpleAdapter adapter = new SimpleAdapter(this, getList(),
				R.layout.list_activity_main, new String[] { "title" },
				new int[] { android.R.id.text1 });
		setListAdapter(adapter);
	}

	private List<Map<String, Object>> getList() {
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		addItem(listMap, "Search by bus stop #", new Intent(this,
				SearchByStopIDActivity.class));
		addItem(listMap, "Search by my location", new Intent(this,
				DisplayMapActivity.class));
		return listMap; // return list of Maps
	}

	private void addItem(List<Map<String, Object>> data, String name,
			Intent intent) {
		Map<String, Object> tmp = new HashMap<String, Object>();
		tmp.put("title", name); // 1 of Map <"title", name value>
		tmp.put("intent", intent); // 2 of Map <"intent", intent value>
		data.add(tmp); // add the map to the list of Maps
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		new CallMapActivityTask(position, l).execute();

	}

	private class CallMapActivityTask extends AsyncTask<Void, Void, Void> {
		private int m_position;
		private ListView m_listView;

		CallMapActivityTask(int p_position, ListView p_listView) {
			m_position = p_position;
			m_listView = p_listView;
		}

		protected void onPostExecute(Void pvoid) {

			progress.dismiss();
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(self, "Loading Map", "processing",
					true);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// get the clicked Map
			Map<String, Object> map = (Map<String, Object>) m_listView
					.getItemAtPosition(m_position);
			Intent intent = (Intent) map.get("intent");
			try {
				startActivity(intent);
			} catch (Exception e) {
				Log.e("Exception Error !!! ", e.toString());
			}

			return null;
		}
	}

	public void onDestroy(Bundle savedInstanceState) {
		super.onDestroy();
	}

}