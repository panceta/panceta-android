package net.guidogarcia.panceta;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class EstimoteBluetoothReceiver extends BroadcastReceiver {
    private Intent estimoteServiceIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            switch (state) {
                case BluetoothAdapter.STATE_ON:
                    Log.i("PANCETA", "BT state ON " + state);
                    if (estimoteServiceIntent == null) {
                        estimoteServiceIntent = new Intent(context, EstimoteService.class);
                        context.startService(estimoteServiceIntent);
                    }
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.i("PANCETA", "BT state TURNING_OFF " + state);
                    if (estimoteServiceIntent != null) {
                        context.stopService(estimoteServiceIntent);
                        estimoteServiceIntent = null;
                    }
                    break;
                default:
                    Log.i("PANCETA", "BT state ignored " + state);
                    break;
            }
        }
    }
}