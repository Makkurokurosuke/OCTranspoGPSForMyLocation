package com.octranspogps;

import java.util.ArrayList;

import com.octranspoBLL.Bus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<Bus> objects;

    ListAdapter(Context context, ArrayList<Bus> buses) {
        ctx = context;
        objects = buses;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Bus p = getBus(position);

        String abound = "Westbound";
        if (p.getDirection().equals("0")){
        	abound = "Eastbound";
        }
        ((TextView) view.findViewById(R.id.tvDescr)).setText(p.getRouteNo() + " "+ p.getDestination() + "");
        ((TextView) view.findViewById(R.id.tvBound)).setText(abound);
       
        CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
        cbBuy.setOnCheckedChangeListener(myCheckChangList);
        cbBuy.setTag(position);
        cbBuy.setChecked(p.getSearchflag());
        return view;
    }

    Bus getBus(int position) {
        return ((Bus) getItem(position));
    }

    ArrayList<Bus> getSearchFlag() {
        ArrayList<Bus> busList = new ArrayList<Bus>();
        for (Bus p : objects) {
            if (p.getSearchflag())
                busList.add(p);
        }
        return busList;
    }

    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            getBus((Integer) buttonView.getTag()).setSearchflag(isChecked);
        }
    };
}
