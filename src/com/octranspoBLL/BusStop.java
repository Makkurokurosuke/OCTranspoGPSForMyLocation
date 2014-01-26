package com.octranspoBLL;



public class BusStop {

		private String mStopName;
		private String mStopCode;
		private Double 	mLongitude;
		private Double 	mLatitude;

		public BusStop () {
			mStopName = "";
			mStopCode ="";
			mLongitude = 0.0;
			mLatitude = 0.0;
		}

		public String getStopName(){
			return this.mStopName ;
		}
		
		public String getStopCode(){
			return this.mStopCode;
		}
		
		public void setStopName(String pStopName){
			this.mStopName = pStopName;
		}
		
		public void setStopCode(String pStopCode){
			this.mStopCode = pStopCode;
		}

		public void setLongitude(Double pLongitude){
			this.mLongitude = pLongitude;
		}	
		
		public Double getLongitude(){
			return this.mLongitude;
		}	
		
		public void setLatitude(Double pLatitude){
			this.mLatitude = pLatitude;
		}	
		
		public Double getLatitude(){
			return this.mLatitude;
		}	
}
