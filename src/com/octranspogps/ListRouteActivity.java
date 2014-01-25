package com.octranspogps;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.octranspoBLL.Bus;
import com.octranspoBLL.BusStop;
import com.octranspoBLL.OCUtility;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import net.wakame.octranspodb.db.DbHelper;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.provider.Settings;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ListRouteActivity extends Activity {
	DbHelper mDbHelper;
	final Context self = this;
    private static final String stopDescription = "StopDescription";
    private static final String route = "Route", routeNo = "RouteNo",    direction = "DirectionID", routeHeading = "RouteHeading";
    private static final String LOG_TAG = "ListRouteActivity";
    ListAdapter boxAdapter;
    ArrayList<Bus> listOfBus;
    long busStopCode;
    BusStop aBusStop;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.listactivity_main);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
	        @Override
	        public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
	        	Log.e(LOG_TAG, "Uncaught Exception! - " + OCUtility.getErrorMessage(paramThrowable));
		        
	        }
	});

  
	}
	
	/* Get the selected bus id and get list of bus route numbers from OC Transpo */
	private void displayRoutesList() {
		// TODO Auto-generated method stub
		listOfBus = new ArrayList<Bus>();
		aBusStop = new BusStop();
		 
		  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
	        busStopCode = getIntent().getLongExtra("stopId", -1);

			if (busStopCode == -1 ) {
				// Error
			} else {
				
				/*
				 * * Get Route
				 * 	   get all bus route numbers and populate check boxes
				 */
				aBusStop.StopCode =String.valueOf(busStopCode);
				
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(getString(R.string.route_summary_URL));
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("appID", getString(R.string.ocAppID )));
				pairs.add(new BasicNameValuePair("apiKey", getString(R.string.ocAppKey )));
				pairs.add(new BasicNameValuePair("stopNo",String.valueOf(busStopCode) ));

				try {
					
					post.setEntity(new UrlEncodedFormEntity(pairs));
					//If Internet is disabled, UnknownHostException occurs here.
					HttpResponse response = client.execute(post);
					HttpEntity r_entity = response.getEntity();
			        String xmlString = EntityUtils.toString(r_entity, "UTF-8");
			        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			        DocumentBuilder db = factory.newDocumentBuilder();
			        
			        
			        InputSource inStream = new InputSource();
			        inStream.setEncoding("UTF-8");
			        inStream.setCharacterStream(new StringReader(xmlString));
			        Document doc = db.parse(inStream);  
			        
			        NodeList nl = doc.getElementsByTagName(stopDescription);
			        for(int i = 0; i < nl.getLength(); i++) {
			            if (nl.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
			                 Element nameElement = (Element) nl.item(i);
			                    aBusStop.StopName =nameElement.getFirstChild().getNodeValue().trim();
			             }
			        }
			        

			        NodeList nlr = doc.getElementsByTagName(route);
			        for(int i = 0; i < nlr.getLength(); i++) {
			        	Node nNode = nlr.item(i);
			        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			        		Element eElement = (Element) nlr.item(i);
			        		//change to load to list of busses Bus.RouteNo 
			        		Bus aBus = new Bus();
			        		String curRouteNo = eElement.getElementsByTagName(routeNo).item(0).getChildNodes().item(0)
	                                .getNodeValue().trim();
			        		String curDestination= eElement.getElementsByTagName(routeHeading).item(0).getChildNodes().item(0)
	                                .getNodeValue().trim();
			        		String curDirection= eElement.getElementsByTagName(direction).item(0).getChildNodes().item(0)
	                                .getNodeValue().trim();	        		

			        		aBus.RouteNo =curRouteNo;
			        		aBus.Destination = curDestination;
			        		aBus.Direction =  curDirection;
			        		listOfBus.add(aBus);
			        		//}
			        	}
			        }

			        
			    //populate check boxes
			        TextView searchTitle = (TextView) findViewById(R.id.searchPageTitle);
			        searchTitle.setText(aBusStop.StopName + " (" + aBusStop.StopCode + ")");
			        boxAdapter = new ListAdapter(this, listOfBus);
			        ListView lvMain = (ListView) findViewById(R.id.lvMain);
			        lvMain.setAdapter(boxAdapter);

			        
				} catch (UnsupportedEncodingException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
	}

	/* Check if network is available */
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	/* OnClick Search button event */
	public void showResult(View v) {
		  List<String> routesList = new ArrayList<String>();
	        for (Bus p : boxAdapter.getSearchFlag()) {
	          if (p.Searchflag){
	        	  routesList.add(p.RouteNo + "_" + p.Direction); //pass also p.Direction ----- 0 = Eastbound, 1 = WestBound 
	          }
	          }
	      
	  if (routesList.size() ==0 ){
		  Toast.makeText(this, "Please select at least one route.", Toast.LENGTH_LONG).show();
	  }
	  
	  else{
		  Intent intent =new Intent(self,  SearchResultActivity.class);
		  intent.putExtra("stopId", busStopCode );
		  intent.putExtra("stopName",  aBusStop.StopName + " (" + aBusStop.StopCode + ")");
		  intent.putStringArrayListExtra("routes", (ArrayList<String>) routesList);
		  try{   
			  startActivityForResult(intent, 0);
			  }
		 finally{  
			 
		 }
		  }
	  }
	  
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (resultCode == Activity.RESULT_OK && requestCode == 0) {

			}
		}
	
	public void onDestroy(Bundle savedInstanceState) {
			super.onDestroy();
	}

	@Override
	  public void onResume() {
	     super.onResume();
	     
	     if(!isOnline() ){
	    	 //network is not available
	    	 //show dialog and return to list activity
	    	 AlertDialog.Builder builder = new AlertDialog.Builder(self);
	 		builder.setMessage("Network Provider is not available. Enable the location setting.");
	 		builder.setCancelable(false)
	 				.setPositiveButton(android.R.string.ok,
	 						new DialogInterface.OnClickListener() {
	 							public void onClick(DialogInterface dialog,
	 									int which) {
	 							 	 startActivity(new Intent(self, ListActivityMain.class));
	 							}
	 						});
	 		builder.create().show();
	     }
	     else{
	      displayRoutesList();
	      }
	     Log.i("Map.OnResume is called", "");
	  }

}
