package net.guidogarcia.panceta;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.provider.Settings.Secure;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import net.guidogarcia.panceta.events.EnterRegionEvent;
import net.guidogarcia.panceta.events.LeaveRegionEvent;

import org.apache.http.Header;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class PancetaActivity extends ActionBarActivity {

    private static EventBus bus = EventBus.getDefault();

    private String androidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panceta);
        com.estimote.sdk.utils.L.enableDebugLogging(true);

        androidId = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        TextView deviceView = (TextView) findViewById(R.id.device_id);
        deviceView.setText(androidId);

        bus.register(this);

        // TODO
        // BeaconManager beaconManager = new BeaconManager(this);
        // if (beaconManager.isBluetoothEnabled()) {
        //    Intent estimoteServiceIntent = new Intent(this, EstimoteService.class);
        //    this.startService(estimoteServiceIntent);
        // }
    }

    public void onEvent(EnterRegionEvent event) {
        Log.d("PANCETA", "NEW BUS EVENT");
        TextView eventView = (TextView) findViewById(R.id.event);
        eventView.setText("ENTER REGION " + event.getRegion().getIdentifier() + " " + event.getRegion().getProximityUUID());

        RequestParams params = new RequestParams();
        params.add("region", event.getRegion().getIdentifier());
        params.add("region_id", event.getRegion().getProximityUUID());
        params.add("device_id", androidId);

        String endpoint = "/" + EstimoteService.ESTIMOTE_UUID + "/events";
        PancetaRestClient.post(endpoint, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO
                Log.d("PANCETA", "REQUEST OK: " + response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO
                Log.d("PANCETA", "REQUEST FAIL");
            }
        });
    }

    public void onEvent(LeaveRegionEvent event) {
        Log.d("PANCETA", "NEW BUS EVENT");
        TextView eventView = (TextView) findViewById(R.id.event);
        eventView.setText("LEAVE REGION " + event.getRegion().getIdentifier());
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }
}
