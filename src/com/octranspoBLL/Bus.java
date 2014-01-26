package com.octranspoBLL;


public class Bus implements Comparable <Bus>{
private String 	mRouteNo;
private Integer mAdjustedScheduleTime;
private String 	mDestination;
private boolean mSearchflag;
private String 	mDirection;


public Bus() {
	mRouteNo = "";
	mAdjustedScheduleTime = 0;
	mDestination = "";
	mDirection = "";
}

public Bus (String pRouteNo, Integer pArrivingTime, String pDestination, boolean pSearchflag, 
	String pDirection) {
    mRouteNo = pRouteNo;
    mAdjustedScheduleTime = pArrivingTime;
    mDestination = pDestination;
	mSearchflag = pSearchflag; 
	mDirection = pDirection;
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

public void setRouteNo(String pRouteNo){
	this.mRouteNo = pRouteNo;
}
public void setDestination(String pDestination){
	this.mDestination = pDestination;
}

public void setDirection(String pDirection){
	this.mDirection = pDirection;
}

public  Integer getAdjustedScheduleTime(){
	return this.mAdjustedScheduleTime ;
}
}
