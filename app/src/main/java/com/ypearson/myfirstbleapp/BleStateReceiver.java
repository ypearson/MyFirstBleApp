package com.ypearson.myfirstbleapp;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class BleStateReceiver extends BroadcastReceiver {

    private static final String TAG = BleStateReceiver.class.getSimpleName();

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
        }

    }
}

class BleState {

    int state;

    int bleGetState()
    {
        return state;
    }
    void bleSetState(int s)
    {
        state = s;
    }
}