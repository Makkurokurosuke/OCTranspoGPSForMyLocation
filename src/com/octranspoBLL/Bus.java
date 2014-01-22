package com.octranspoBLL;


public class Bus implements Comparable <Bus>{
public String RouteNo;
public Integer AdjustedScheduleTime;
public String Destination;
public boolean Searchflag;
public String Direction;

public Bus () {
	RouteNo = "";
	AdjustedScheduleTime =0;
	Destination = "";
	Direction = "";
}

public Bus (String pRouteNo, Integer pArrivingTime, String pDestination, boolean pSearchflag, String pDirection) {
    RouteNo = pRouteNo;
    AdjustedScheduleTime = pArrivingTime;
    Destination = pDestination;
	Searchflag = pSearchflag; 
	Direction = pDirection;
}


@Override
public int compareTo(Bus another) {	
	return  (int) this.AdjustedScheduleTime - another.AdjustedScheduleTime;
}

}
