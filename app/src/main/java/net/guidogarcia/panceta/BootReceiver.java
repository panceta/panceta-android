package net.guidogarcia.panceta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.estimote.sdk.BeaconManager;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        BeaconManager beaconManager = new BeaconManager(context);
        if (beaconManager.isBluetoothEnabled()) {
            Intent estimoteServiceIntent = new Intent(context, EstimoteService.class);
            context.startService(estimoteServiceIntent);
        }
    }
}