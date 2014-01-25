package com.octranspoBLL;

import java.util.ArrayList;
import java.util.List;

public class BusStop {

		private String mStopName;
		private String mStopCode;
		private List <String> mRoutes;


		public BusStop () {
			mStopName = "";
			mStopCode ="";
			mRoutes = new ArrayList <String>();
		}

		public String getStopName(){
			return this.mStopName ;
		}
		
		public String getStopCode(){
			return this.mStopName;
		}
}
