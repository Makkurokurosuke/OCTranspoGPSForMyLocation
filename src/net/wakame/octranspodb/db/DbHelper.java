package net.wakame.octranspodb.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.octranspoBLL.BusStop;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {

	static final String DB_NAME = "OCTranspo.db";
	static final int DB_VERSION = 1;
	private Context thisContext;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		thisContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	// create Item table
		createStops(db);
	}

	public void createStops(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE STOPS (stop_id TEXT PRIMARY KEY, stop_code TEXT, stop_name TEXT, stop_desc TEXT, stop_lat REAL, stop_lon REAL)");
		insertStopsData(db);
	}
	
	public void droptableStops(SQLiteDatabase db) {
		// create Item table
			db.execSQL("DROP TABLE IF EXISTS STOPS");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/*
	 * Insert each bus stop record to Stops table in SQLite
	 * */
	public void insertStopsData(SQLiteDatabase db) {
		db.beginTransaction();
		  try {
			  BufferedReader reader = new BufferedReader(
		        new InputStreamReader(thisContext.getAssets().open("stops.txt")));
			  String mLine = reader.readLine();//Skip the first line. The first line is heading.
			  mLine = reader.readLine();
			    while (mLine != null) {
			        String values[] = mLine.split(",");
			        StopsDao.insert(db, values[0], values[1], values[2], values[3],Double.parseDouble(values[4]), Double.parseDouble(values[5]));
			       mLine = reader.readLine();
			    }

			    reader.close();
	    } catch (IOException e) {

	    } finally {    }

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public boolean isTableExists(String tableName, SQLiteDatabase mDatabase) {
	    android.database.Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
	    if(cursor!=null) {
	        if(cursor.getCount()>0) {
	                            cursor.close();
	            return true;
	        }
	                    cursor.close();
	    }
	    return false;
	}

	public  List<BusStop> getBusStopsNearLocation(double left, double top, double right, double  bottom) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		//user clear the data, but the DB still exists. Need to re-populate the data
		if(!isTableExists("STOPS", db)){
			this.createStops(db);
		};
		
		Cursor c = StopsDao.getStopsByLonLat(db,  left,  top, right,  bottom);
		List<BusStop> busStops = new ArrayList<BusStop>();
		if (c.moveToFirst()) {
			do {
				BusStop aBusStop = new BusStop();
				aBusStop.setStopName(c.getString(c
						.getColumnIndex(StopsDao.COL_STOP_NAME)));
				aBusStop.setStopCode(c.getString(c
						.getColumnIndex(StopsDao.COL_STOP_CODE)));
				aBusStop.setLatitude(c.getDouble(c
						.getColumnIndex(StopsDao.COL_STOP_LAT)));
				aBusStop.setLongitude( c.getDouble(c
						.getColumnIndex(StopsDao.COL_STOP_LON)));
				
				busStops.add(aBusStop);
			} while (c.moveToNext());
		}
		this.close();
		return busStops ;
	}

	public boolean isValidBusStop(String pBusStopId) {
		boolean isValid = false;

		SQLiteDatabase db = getReadableDatabase();
		//user clear the data, but the DB still exists. Need to re-populate the data
		if(! isTableExists("STOPS", db)){
			createStops(db);
		};
		
		Cursor c = StopsDao.getStopsByCode(db, pBusStopId);
		
		//if the bus stop exists in the DB, return true
		
		if (c.getCount() > 0) {
			isValid = true;
		}
		close();
		
		return isValid;
	}

}
