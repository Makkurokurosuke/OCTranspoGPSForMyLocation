package com.octranspogps;



import com.octranspoBLL.OCUtility;
import com.octranspogps.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.wakame.octranspodb.db.DbHelper;
import net.wakame.octranspodb.db.StopsDao;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class DisplayMapActivity extends FragmentActivity {


	GoogleMap map;
	DbHelper mDbHelper;
	final Context self = this;
	boolean startListActivity = false;
	private static final String LOG_TAG = "DisplayMapActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Catch uncaught exception globally  
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread paramThread,
					Throwable paramThrowable) {
				Log.e(LOG_TAG,
						"Uncaught Exception! - "
								+ OCUtility.getErrorMessage(paramThrowable));
				
			}
		});

		setContentView(R.layout.activity_main);
		//handleMyLocation();
	}

	private void displayMap(String provider, LocationManager locationManager) {

		Location location = null;

		// It takes time to get current location. Display the previous
		// location first.

		location = locationManager.getLastKnownLocation(provider);
		LatLng position;

		if (location != null) {
			position = new LatLng(location.getLatitude(),
					location.getLongitude());
			// add markers
			drawMarker(location.getLatitude(), location.getLongitude());
		} else {
			/* Unable to find a previous location. */
			position = new LatLng(-121.45356, 46.51119);
			new OCUtility().showError(self, 4);
		}

		try {
			MapsInitializer.initialize(this);
		} catch (GooglePlayServicesNotAvailableException e) {
			Log.d(LOG_TAG, "GooglePlayServicesNotAvailableException!!");
			displayEnableNetworkProviderDialog();
		}

		// zoom to the last known/current location
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 18));
		
		// register Listener for My Location button clicked event
		createMapMyLocationBtnClickLister();
		
		// set Listener to re-create markers when the map location is moved
		map.setOnCameraChangeListener(getCameraChangeListener());
	}

	
	public OnCameraChangeListener getCameraChangeListener()
	{
	    return new OnCameraChangeListener() 
	    {
	        @Override
	        public void onCameraChange(CameraPosition camPosition) 
	        {
	        	drawMarker(camPosition.target.latitude, camPosition.target.longitude);
	        }
	    };
	}
	
	private void createMapMyLocationBtnClickLister() {
		map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
			@Override
			public boolean onMyLocationButtonClick() {
				Location currentLocation = map.getMyLocation();
				if (currentLocation != null) {
					drawMarker(currentLocation
							.getLatitude(),currentLocation.getLongitude());
					LatLng newPosition = new LatLng(currentLocation
							.getLatitude(), currentLocation.getLongitude());
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(
							newPosition, 18));
				} else {
					/* Unable to find Current Location. Try again. */
					new OCUtility().showError(self, 3);
				}
				return true;
			}

		});
	}

	private void createMapMarkerClickListner() {

		/*
		 * When Marker is clicked, open list of routes page for the selected bus
		 * stop
		 */
		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker aMarker) {
				String id[] = aMarker.getTitle().split(":");
				Intent intent = new Intent(self, ListRouteActivity.class);
				if (id.length > 0) {
					intent.putExtra("stopId", Long.valueOf(id[0]));
					try {
						startListActivity = true;
						startActivity(intent);
					} finally {
					}
				}
				return true;
			}
		});

	}

	private void displayEnableNetworkProviderDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(self);
		builder.setMessage("Network Provider is not available. Enable the location setting.");
		builder.setCancelable(false)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(intent);
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(new Intent(self,
										ListActivityMain.class));
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	protected void onStart() {
		super.onStart();
		 Log.i("Map.OnStart is called", "");
	}
	
	@Override
	  public void onResume() {
	     super.onResume();
	     handleMyLocation();
	     Log.i("Map.OnResume is called", "");
	  }
	
	@Override
	public void onRestart() {
		super.onRestart();
		 Log.i("Map.OnRestart is called", "");
	}
	
	@Override
	protected void onStop() {
	    super.onStop(); 
	    Log.i("Map.OnStop is called", "");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("OnDestory is called", "");
	}

	/* Create Marker for each bus stop on Map */
	private void drawMarker(double pLatitule, double pLongtitude) {
		map.clear();
		
		LatLng currentPosition = new LatLng(pLatitule, pLongtitude);
		
		/* Get list of bus stop's Longitude and Latitude */
		mDbHelper = new DbHelper(this);

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		//user clear the data, but the DB still exists. Need to re-populate the data
		if(!mDbHelper.isTableExists("STOPS", db)){
			mDbHelper.createStops(db);
		};
		
		Cursor c = StopsDao.getStopsByLonLat(db, currentPosition.latitude,
				currentPosition.longitude, 0.0025);//0.01

		if (c.moveToFirst()) {
			do {

				String busStopName = c.getString(c
						.getColumnIndex(StopsDao.COL_STOP_NAME));
				String busStopCode = c.getString(c
						.getColumnIndex(StopsDao.COL_STOP_CODE));
				Double busLat = c.getDouble(c
						.getColumnIndex(StopsDao.COL_STOP_LAT));
				Double busLon = c.getDouble(c
						.getColumnIndex(StopsDao.COL_STOP_LON));
				MarkerOptions markerOptions = new MarkerOptions()
						.position(new LatLng(busLat, busLon))
						.title(busStopCode + ": " + busStopName)
				 .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop));

				map.addMarker(markerOptions);

			} while (c.moveToNext());
		}
		mDbHelper.close();

		/*
		 * Add Markers Click Lister to the map. When marker is clicked, open
		 * list of routes page for the selected bus stop
		 */
		createMapMarkerClickListner();
	}
	
	void handleMyLocation() {

		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		if (map == null) {
			// Error: Google Map is not available
			new OCUtility().showErrorMsg(self, "Google Map is null!");
		} else {

			map.setMyLocationEnabled(true); // Set to display current location
			
			/* Get Location */
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			String provider = locationManager.getBestProvider(criteria, true);

			if (provider != null) {
				displayMap(provider, locationManager);
			} else {
				/* Location service needs to be enabled  */
				displayEnableNetworkProviderDialog();
			}
		}
	}
	
	


}
