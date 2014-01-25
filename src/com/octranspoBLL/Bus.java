package com.octranspoBLL;


public class Bus implements Comparable <Bus>{
private String 	mRouteNo;
private Integer mAdjustedScheduleTime;
private String 	mDestination;
private boolean mSearchflag;
private String 	mDirection;
private Double 	mLongtitude;
private Double 	mLatitude;

public Bus() {
	mRouteNo = "";
	mAdjustedScheduleTime = 0;
	mDestination = "";
	mDirection = "";
	mLongtitude = 0.0;
	mLatitude = 0.0;
}

public Bus (String pRouteNo, Integer pArrivingTime, String pDestination, boolean pSearchflag, 
	String pDirection, Double pLongititude, Double pLatitude) {
    mRouteNo = pRouteNo;
    mAdjustedScheduleTime = pArrivingTime;
    mDestination = pDestination;
	mSearchflag = pSearchflag; 
	mDirection = pDirection;
	mLongtitude = pLongititude;
	mLatitude = pLatitude;
}


@Override
public int compareTo(Bus another) {	
	return  (int) this.mAdjustedScheduleTime - another.mAdjustedScheduleTime;
}

public boolean getSearchflag(){ 
	return this.mSearchflag;
}

public void setSearchflag(boolean pSearchflag){ 
	this.mSearchflag = pSearchflag;
}

public String  getDirection(){ 
	return this.mDirection;
}

public String  getRouteNo(){ 
	return this.mRouteNo;
}

public String getDestination(){
	return this.mDestination;
}

}
