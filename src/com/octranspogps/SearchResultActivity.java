package com.octranspogps;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.octranspoBLL.Bus;
import com.octranspoBLL.BusStop;
import com.octranspoBLL.OCUtility;

import android.widget.TableRow.LayoutParams;
import net.wakame.octranspodb.db.DbHelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SearchResultActivity extends Activity {
	DbHelper mDbHelper;
	final Context self = this;
	private static final String tripDestination = "TripDestination",
			adjustedScheduleTime = "AdjustedScheduleTime";
	private TableLayout tl;
	private TableRow tr;

	List<Bus> aBusList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		long busStopCode = getIntent().getLongExtra("stopId", -1);
		ArrayList<String> routesList = getIntent().getStringArrayListExtra(
				"routes");

		if (busStopCode == -1) {
			// Error
			new OCUtility().showErrorMsg(this, "Error: Buss top is null.");
		} else {

			getBusResult(busStopCode, routesList);
			setContentView(R.layout.table);
			tl = (TableLayout) findViewById(R.id.maintable);
			addHeaders();
			addData(aBusList);

			TextView searchTitle = (TextView) findViewById(R.id.searchPageTitle);
			searchTitle.setText(getIntent().getExtras().getString("stopName"));
		}
	}

	private void getBusResult(long busStopCode, ArrayList<String> routesList) {
		/*
		 * * Get Route get all bus route numbers and populate check boxes need
		 * to be refactored.
		 */

		BusStop aBusStop = new BusStop();
		aBusStop.StopCode = String.valueOf(busStopCode);
		aBusList = new ArrayList<Bus>();
		try {

			/* get next available buses */
			for (String strRoute : routesList) {
				String[] routeNum = strRoute.split("_");
				HttpClient client2 = new DefaultHttpClient();
				HttpPost post2 = new HttpPost(getString(R.string.next_trip_URL));
				List<NameValuePair> pairs2 = new ArrayList<NameValuePair>();
				pairs2.add(new BasicNameValuePair("appID",
						getString(R.string.ocAppID)));
				pairs2.add(new BasicNameValuePair("apiKey",
						getString(R.string.ocAppKey)));
				pairs2.add(new BasicNameValuePair("stopNo", String
						.valueOf(busStopCode)));
				pairs2.add(new BasicNameValuePair("routeNo", routeNum[0]));

				post2.setEntity(new UrlEncodedFormEntity(pairs2));
				HttpResponse response2 = client2.execute(post2);
				HttpEntity r_entity2 = response2.getEntity();
				String xmlString2 = EntityUtils.toString(r_entity2, "UTF-8");
				DocumentBuilderFactory factory2 = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db2 = factory2.newDocumentBuilder();
				InputSource inStream2 = new InputSource();
				inStream2.setCharacterStream(new StringReader(xmlString2));
				Document doc2 = db2.parse(inStream2);

				LoadFromXML(doc2, routeNum);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		Collections.sort(aBusList);
	}

	private void LoadFromXML(Document doc2, String[] routeNum) {
		NodeList nList = doc2.getElementsByTagName("RouteDirection");
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				String aNodeDirection = eElement
						.getElementsByTagName("Direction").item(0)
						.getTextContent();

				String directionID = "1";
				if (aNodeDirection.equals("Eastbound")) {
					directionID = "0"; // /Direction ----- 0 =
										// Eastbound, 1 = WestBound
				}

				if (routeNum[1].equals(directionID)) {
					NodeList tripList = eElement.getElementsByTagName("Trip");

					for (int temp2 = 0; temp2 < tripList.getLength(); temp2++) {

						Node tripNode = tripList.item(temp2);
						if (tripNode.getNodeType() == Node.ELEMENT_NODE) {

							Element tripElement = (Element) tripNode;
							try {

								String inMins = tripElement
										.getElementsByTagName(
												adjustedScheduleTime).item(0)
										.getChildNodes().item(0).getNodeValue()
										.trim();
								String strTripDestination;

								strTripDestination = new String(tripElement
										.getElementsByTagName(tripDestination)
										.item(0).getChildNodes().item(0)
										.getNodeValue().trim().getBytes(),
										"UTF-8");

								String destinations[] = strTripDestination
										.split("/");

							aBusList.add(new Bus(routeNum[0], Integer
									.parseInt(inMins), destinations[0].trim(),
									false, ""));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (DOMException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				}

			}
		}
	}

	public void addHeaders() {

		/** Create a TableRow dynamically **/
		tr = new TableRow(this);

		/** Creating a TextView (Route) to add to the row **/
		tr.addView(createHeaderTextView("Route",0f));

		/** Creating textview (Time) **/
		tr.addView(createHeaderTextView("Time",0f));

		/** Creating textview (Destination) **/
		tr.addView(createHeaderTextView("Destination",1f));

		// Add the TableRow to the TableLayout
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

	}

	private View createHeaderTextView(String header,float weigh) {
		TextView aTextView = new TextView(this);
		aTextView.setText(header);
		aTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		if (weigh == 0 ){
			aTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
		}else{
			aTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT,weigh));
		}

		aTextView.setPadding(10, 10, 10, 10);
		aTextView.setTextColor(Color.rgb(225, 225, 225));
		aTextView.setTextSize(18.0f);
		aTextView.setBackgroundResource(R.drawable.cell_shape);
		return aTextView;
	}

	/** This function add the data to the table **/
	public void addData(List<Bus> aBusList) {
		for (Bus aBus : aBusList) {

			/** Create a TableRow dynamically **/
			tr = new TableRow(this);

			/** Creating a TextView to add to the row **/
			tr.addView(getTDTextView(aBus.RouteNo, 0f)); // Adding textView to
														// tablerow.

			/** Creating Time textview **/
			tr.addView(getTDTextView("In "
					+ Integer.toString(aBus.AdjustedScheduleTime) + " mins  ",0f));

			/** Creating another textview **/
			tr.addView(getTDTextView(aBus.Destination,1f));

			// Add the TableRow to the TableLayout
			tl.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}

	}

	private View getTDTextView(String routeNo,float weigh) {
		TextView tv = new TextView(this);
		tv.setText(routeNo);
		
		if (weigh == 0 ){
			tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
		}else{
			tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT,weigh));
		}

		tv.setPadding(10, 10, 10, 10);
		tv.setTextColor(Color.rgb(128, 128, 128));
		tv.setTextSize(18.0f);
		tv.setBackgroundResource(R.drawable.tdcell_shape);
		return tv;
	}

	public void onClick(View view) {
		finish();
	}

	@Override
	public void finish() {
		Intent data = new Intent();
		getIntent().getLongExtra("stopId", -1);
		data.putExtra("stopId", getIntent().getLongExtra("stopId", -1));
		setResult(RESULT_OK, data);
		super.finish();
	}

	public void onDestroy(Bundle savedInstanceState) {
		super.onDestroy();

}
}
