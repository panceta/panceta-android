package net.guidogarcia.panceta;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import net.guidogarcia.panceta.events.EnterRegionEvent;
import net.guidogarcia.panceta.events.LeaveRegionEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

// https://github.com/Estimote/Android-SDK/blob/master/Demos/src/main/java/com/estimote/examples/demos/NotifyDemoActivity.java
public class EstimoteService extends Service {
    private static BeaconManager beaconManager;

    public static final String ESTIMOTE_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";

    private static final String REGION_ID = "PANCETA";
    private static final Region REGION = new Region(REGION_ID, ESTIMOTE_UUID, null, null);

    private static EventBus bus = EventBus.getDefault();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i("PANCETA", "Start service");

        bus.register(this);

        beaconManager = new BeaconManager(this);
        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(5), TimeUnit.SECONDS.toMillis(1));
        beaconManager.setForegroundScanPeriod(TimeUnit.SECONDS.toMillis(5), TimeUnit.SECONDS.toMillis(1));

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                Log.i("PANCETA", "Beacons discovered " + region.getIdentifier());
                for (Beacon beacon: beacons) {
                    Log.d("PANCETA", "Power " + beacon.getMacAddress() + "-> power=" + beacon.getMeasuredPower() + " rssi=" + beacon.getRssi());
                }
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                Log.i("PANCETA", "Enter region");
                for (Beacon beacon: beacons) {
                    Log.d("PANCETA", "Power enter " + beacon.getMacAddress() + "-> power=" + beacon.getMeasuredPower() + " rssi=" + beacon.getRssi());
                }
                bus.post(new EnterRegionEvent(region));
            }

            @Override
            public void onExitedRegion(Region region) {
                Log.i("PANCETA", "Leave region");
                bus.post(new LeaveRegionEvent(region));
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startMonitoring(REGION);
                    beaconManager.startRanging(REGION);
                } catch (Exception e) {
                    Log.e("PANCETA", "Not able to start monitoring", e);
                }
            }
        });

        return START_STICKY;
    }

    public void onEvent(LeaveRegionEvent event) {
        Toast.makeText(this, "Leave Region", Toast.LENGTH_LONG).show();
    }

    public void onEvent(EnterRegionEvent event) {
        Toast.makeText(this, "Enter Region " + event.getRegion().getIdentifier(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        Log.d("PANCETA", "Destroy service");
        super.onDestroy();
        beaconManager.disconnect();
    }
}