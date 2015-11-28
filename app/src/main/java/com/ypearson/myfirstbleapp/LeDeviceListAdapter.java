package com.ypearson.myfirstbleapp;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class LeDeviceListAdapter extends BaseAdapter {

    private ArrayList<BluetoothDevice> mLeDevices;
    private LayoutInflater mInflator;

    public LeDeviceListAdapter(Context context, String[] list) {
        super();
        mLeDevices = new ArrayList<BluetoothDevice>();
        mInflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addDevice(BluetoothDevice device) {
        if(!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }

    public BluetoothDevice getDevice(int position) {

        return mLeDevices.get(position);
    }

    public void clear() {

        mLeDevices.clear();
    }

    @Override
    public int getCount() {

        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {

        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View viewRow = mInflator.inflate(R.layout.rowlayout, viewGroup, false);
        TextView tv = (TextView)viewRow.findViewById(R.id.deviceName);
        tv.setText(mLeDevices.get(i).getName());
        tv = (TextView)viewRow.findViewById(R.id.macAddress);
        tv.setText(mLeDevices.get(i).getAddress());
        return viewRow;
    }
}
