package com.ypearson.myfirstbleapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int RESULT = 0;
    private static final int REQUEST_ENABLE_BT = 0;

    private Button button;
    private ListView listView;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this, R.string.ble_supported, Toast.LENGTH_SHORT).show();
        }

        // This is needed for Marshmallow
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, RESULT);
        }

        final BluetoothManager mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }

        //String[] names = {"fred", "bob", "john"};
        listView = (ListView)findViewById(R.id.listView);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.rowlayout, names);
        mLeDeviceListAdapter = new LeDeviceListAdapter(this,null);
        listView.setAdapter(mLeDeviceListAdapter);



        mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

                Log.d(TAG, "device.getName() = "  + device.getName());
                Log.d(TAG, "device.getAddress() = " + device.getAddress());

                mLeDeviceListAdapter.addDevice(device);
                mLeDeviceListAdapter.notifyDataSetChanged();

            }
        };

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBluetoothAdapter.startLeScan(mLeScanCallback);

            }
        });

    }

    private void startLeScan(boolean enable){

        if(enable) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }



}

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
        tv.setText(mLeDevices.get(i).getName() + " " + mLeDevices.get(i).getAddress());

        return viewRow;
    }
}