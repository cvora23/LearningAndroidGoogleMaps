package cvora.safekidtestclient;


import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MyMapFragment";
    private static final LatLng HOME = new LatLng(38.6374478,-121.3846125);

    MapFragment mapFragment;
    private GoogleMap mMap;
    IntentFilter intentFilter;
    LatLng startLatLng;
    boolean isStartingPoint = false;

    public MyMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_content_frame,mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        // -intent to filter for new location data
        intentFilter = new IntentFilter();
        intentFilter.addAction("NEW_LOC_DATA");

        // -- register the receiver
        getActivity().registerReceiver(intentReceiver,intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_map, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(intentReceiver);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.addMarker(new MarkerOptions().position(HOME).title("Home").snippet("Home Sweet Home!!!!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HOME,8));
    }

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            String latitude = data.getString("latitude");
            String longitude = data.getString("longitude");
            Log.d(TAG, "Latitude: " + latitude);
            Log.d(TAG, "Longitude: " + longitude);

            Toast.makeText(getActivity(),"Latitude : "+latitude + " Longitude : "+longitude,Toast.LENGTH_SHORT).show();

            if(!isStartingPoint){
                startLatLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
                CircleOptions circleOptions = new CircleOptions().center(startLatLng).radius(40);
                mMap.addCircle(circleOptions);
                mMap.addMarker(new MarkerOptions().position(startLatLng));
                isStartingPoint = true;
            }else{
                LatLng newLatLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                PolylineOptions polygonOptions = new PolylineOptions().add(startLatLng).add(newLatLng).geodesic(true).color(Color.GREEN);
                mMap.addPolyline(polygonOptions);
                startLatLng = newLatLng;
            }

        }
    };
}
