package com.octranspoBLL;



import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class OCUtility {

public final static String getErrorMessage(Throwable pThrowable) {
	  if (pThrowable.getCause() == null) {
		  return pThrowable.getLocalizedMessage();
	   } else {
	       return pThrowable.getCause().getLocalizedMessage();
	   }
}

public void showError(Context pActivity, int pErrorNum) {
	String errorMsg = "";
	if (pErrorNum == 1){
		errorMsg = "Invalid bus stop number is entered!";
	}else if(pErrorNum == 2){
		errorMsg = "Please enter a bus stop number!";			
	}else if(pErrorNum == 3){
		errorMsg = "Satelite couldn't locate your location. Wait for a few seconds and try again! This could take about 10 seconds.";
	}else if(pErrorNum == 4){
		errorMsg = "Unable to get the previous location. Press the current location button.";
	}else if(pErrorNum == 5){
		errorMsg = "Network Provider is not available. Enable the location setting.";
	}
	AlertDialog.Builder builder = new AlertDialog.Builder(pActivity);
	builder.setMessage(errorMsg)
	       .setCancelable(false)
	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                //do things
	           }
	       });
	AlertDialog alert = builder.create();
	alert.show();
}

public AlertDialog showErrorMsg(Context pActivity, String msg) {
	AlertDialog.Builder builder = new AlertDialog.Builder(pActivity);
	builder.setMessage(msg)
	       .setCancelable(false)
	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                //do things
	           }
	       });
	AlertDialog alert = builder.create();
	alert.show();
	return  alert;
}

/* Convert text to bitmap image */
public BitmapDescriptor textAsBitmap(String text) {
	Typeface tf = Typeface.create("Arial", Typeface.NORMAL);
	Paint paint = new Paint();
	paint.setTextSize(22);
	paint.setStyle(Style.FILL);
	paint.setColor(Color.BLACK);
	paint.setAntiAlias(true);
	paint.setStyle(Paint.Style.FILL);
	paint.setTypeface(tf);
	paint.setTextAlign(Paint.Align.LEFT);
	int width = (int) (paint.measureText(text) + 0.5f); // round
	float baseline = (int) (-paint.ascent() + 0.5f); // ascent() is negative
	int height = (int) (baseline + paint.descent() + 0.5f);
	Bitmap image = Bitmap.createBitmap(width, height,
			Bitmap.Config.ARGB_8888);
	Canvas canvas = new Canvas(image);
	canvas.drawText(text, 0, baseline, paint);
	BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(image);
	return icon;
}



}



