package com.ypearson.myfirstbleapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Constants
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RESULT = 0;
    private static final int REQUEST_CODE_ENABLE_BLE = 0;

    // UI Objects
    private Handler handler;
    private Button button;
    private ListView listView;

    // Bluetooth objects
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    // State variable
    private int bleState;

    // Broadcast receivers
    private BleStateReceiver bleStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bleStateReceiver = new BleStateReceiver();
        registerReceiver(bleStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

        setContentView(R.layout.activity_main);

        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this, R.string.ble_supported, Toast.LENGTH_SHORT).show();
        }

        // Required for Marshmallow
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, RESULT);
            }
        }

        final BluetoothManager mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLE);
        }

        listView = (ListView)findViewById(R.id.listView);
        mLeDeviceListAdapter = new LeDeviceListAdapter(this,null);
        listView.setAdapter(mLeDeviceListAdapter);

        mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

//                Log.d(TAG, "device.getName() = "  + device.getName());
//                Log.d(TAG, "device.getAddress() = " + device.getAddress());
                mLeDeviceListAdapter.addDevice(device);
                mLeDeviceListAdapter.notifyDataSetChanged();
            }
        };

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button.setEnabled(false);
                button.setText("Scanning...");
                Runnable rs = new Runnable() {
                    @Override
                    public void run() {
                        startLeScan(true);
                    }
                };
                Runnable rp = new Runnable() {
                    @Override
                    public void run() {
                        startLeScan(false);
                    }
                };

                handler = new Handler();
                handler.post(rs);
                handler.postDelayed(rp, 10000);
            }
        });

    }

    private void startLeScan(boolean enable){

        if(enable) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            Log.d(TAG, "BLE scan started.");
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            Log.d(TAG, "BLE scan stopped.");

            button.setEnabled(true); //TODO: move to better location
            button.setText("Scan");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == REQUEST_CODE_ENABLE_BLE) {

            if(resultCode == RESULT_OK) {
                Log.d(TAG, "RESULT_OK");
            }
            else if(resultCode == RESULT_CANCELED) {
                Log.d(TAG, "RESULT_CANCELED");
            }
            else {
                Log.d(TAG, "RESULT ERROR");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class BleStateReceiver extends BroadcastReceiver {

        private final String TAG = BleStateReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {

            int state = intent.getExtras().getInt("android.bluetooth.adapter.extra.STATE");

            switch (state) {

                case BluetoothAdapter.STATE_ON:
                    Log.d(TAG, "STATE_ON");
                    break;

                case BluetoothAdapter.STATE_OFF:
                    Log.d(TAG, "STATE_OFF");

                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.d(TAG, "STATE_TURNING_ON");
                    break;

                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.d(TAG, "STATE_TURNING_OFF");
                    break;

                case BluetoothAdapter.STATE_CONNECTED:
                    Log.d(TAG, "STATE_CONNECTED");
                    break;
                default:
                    break;
            }
        }
    }
}

