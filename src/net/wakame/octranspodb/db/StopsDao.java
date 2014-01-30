package net.wakame.octranspodb.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StopsDao {
  public static final String TABLE_NAME = "STOPS";
  public static final String COL_STOP_ID = "stop_id";
  public static final String COL_STOP_CODE = "stop_code";
  public static final String COL_STOP_NAME = "stop_name";
  public static final String COL_STOP_DESC = "stop_desc";
  public static final String COL_STOP_LAT = "stop_lat";
  public static final String COL_STOP_LON = "stop_lon";


  public static void insert(SQLiteDatabase db, String stop_id, String stop_code, String stop_name,
		  String stop_desc, Double stop_lat, Double stop_lon) {
    ContentValues values = new ContentValues();
    values.put(COL_STOP_ID , stop_id);
    values.put(COL_STOP_CODE, stop_code);
    values.put(COL_STOP_NAME , stop_name);
    values.put(COL_STOP_DESC, stop_desc);
    values.put(COL_STOP_LAT , stop_lat);
    values.put(COL_STOP_LON , stop_lon);
    db.insert(TABLE_NAME, null, values);
  }

public static Cursor getStop(SQLiteDatabase db) {
		  return db.query(true, TABLE_NAME, new String[] {COL_STOP_ID },
		    null, null, null, null, null, null);
		}

public static Cursor getAllStops(SQLiteDatabase db) {
	  String sql = "SELECT *  FROM " + TABLE_NAME;
	  return db.rawQuery(sql, null);
	}

public static Cursor getStopsByLonLat(SQLiteDatabase db, double left, double top, double right, double bottom) {
	  String sql = "SELECT *  FROM STOPS WHERE " + 
			   COL_STOP_LAT  + " > " + bottom + " and " + 
			   COL_STOP_LAT  + " < " + top  + " and " + 
			   COL_STOP_LON  + " > " + left + " and " + 
			   COL_STOP_LON  + " < " + right;
	  return db.rawQuery(sql, null);
	}//double left, double top, double right, double  bottom

public static Cursor getStopsByCode(SQLiteDatabase db, String pBusCode) {
	  String sql = "SELECT *  FROM STOPS WHERE " + 
			  COL_STOP_CODE  + " = "  + pBusCode ;
	  return db.rawQuery(sql, null);
	}
}