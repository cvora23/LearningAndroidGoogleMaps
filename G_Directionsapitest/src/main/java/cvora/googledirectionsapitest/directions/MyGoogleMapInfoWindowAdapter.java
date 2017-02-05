package cvora.googledirectionsapitest.directions;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cvora.googledirectionsapitest.R;
import cvora.googledirectionsapitest.common.GlobalValues;
import cvora.googledirectionsapitest.common.Util;
import cvora.googledirectionsapitest.common.WayPointMgr;
import cvora.googledirectionsapitest.routing.AbstractRouting;
import cvora.googledirectionsapitest.routing.Route;
import cvora.googledirectionsapitest.routing.RouteException;
import cvora.googledirectionsapitest.routing.Routing;
import cvora.googledirectionsapitest.routing.RoutingListener;

/**
 * Created by Admin on 7/12/2016.
 */
public class MyGoogleMapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter,RoutingListener {

    private LatLng endLocation;
    private Activity baseActivity;
    private Marker currMarker;
    private static boolean isEtaReceived = false;
    private static final String TAG = "MyGMapInfWindAdptr";
    private View view;

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    private String getAddress(Marker location){
        return Util.getAddress(baseActivity,location.getPosition());
    }

    @Override
    public View getInfoContents(Marker marker) {

        currMarker = marker;
        LatLng latLng = marker.getPosition();
        //TextView title = (TextView)view.findViewById(R.id.title);
        //TextView snippet = (TextView)view.findViewById(R.id.snippet);
        //TextView lat = (TextView)view.findViewById(R.id.lat);
        //TextView lng = (TextView)view.findViewById(R.id.lng);

        //title.setText("Current Location");
        //snippet.setText("Address : " + getAddress(marker));
        //lat.setText("Latitude: " + latLng.latitude);
        //lng.setText("Longitude: " + latLng.longitude);

        // Pre Defined ETA
        TextView eta = (TextView)view.findViewById(R.id.eta);
        eta.setText("ETA : " + WayPointMgr.getInstance().getEta(latLng));

        Button crossedButton = (Button)view.findViewById(R.id.crossed);
        Button notCrossedButton = (Button)view.findViewById(R.id.not_crossed);
        if(WayPointMgr.getInstance().isCrossed(latLng)){
            crossedButton.setVisibility(View.VISIBLE);
            notCrossedButton.setVisibility(View.INVISIBLE);
        }else{
            crossedButton.setVisibility(View.INVISIBLE);
            notCrossedButton.setVisibility(View.VISIBLE);
        }

        // Real Time ETA

//        if(isEtaReceived){
//            isEtaReceived = false;
//            return view;
//        }else {
//            getEta(latLng);
//            return null;
//        }
        return view;
    }

    private void getEta(LatLng startLocation){

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(startLocation, endLocation)
                .key(GlobalValues.DEVELOPER_KEY)
                .build();
        routing.execute();

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        // The Routing request failed
        if(e != null) {
            Toast.makeText(baseActivity, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(baseActivity, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(List<Route> route, int shortestRouteIndex) {

        // \\TODO: Change 1 to route.size to get all direction options available from directions api.
        for (int i = 0; i <1; i++) {

            TextView eta = (TextView)view.findViewById(R.id.eta);
            eta.setText("ETA : " + route.get(i).getDurationText());
//            TextView distance = (TextView)view.findViewById(R.id.distance);
//            distance.setText("Distance to Destn : " + route.get(i).getDistanceText());

            if(!isEtaReceived){
                isEtaReceived = true;
                currMarker.showInfoWindow();
            }
        }
    }

    @Override
    public void onRoutingCancelled() {
        Log.i(TAG, "Routing was cancelled.");
    }

    public void setEndLocation(LatLng endLocation){
        this.endLocation = endLocation;
    }

    public void setBaseActivity(Activity activity){
        baseActivity = activity;
        view = baseActivity.getLayoutInflater().inflate(R.layout.info_window,null);
    }
}
