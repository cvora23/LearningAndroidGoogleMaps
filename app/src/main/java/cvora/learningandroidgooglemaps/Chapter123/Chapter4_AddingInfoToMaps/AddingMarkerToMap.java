package cvora.learningandroidgooglemaps.Chapter123.Chapter4_AddingInfoToMaps;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import cvora.learningandroidgooglemaps.R;

public class AddingMarkerToMap extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    static final LatLng NEWARK = new LatLng(40.735188, -74.172414);
    static final LatLng MUMBAI = new LatLng(19.076, 72.8777);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_marker_to_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDescriptor descriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        //BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        MarkerOptions options = new MarkerOptions().position(NEWARK).icon(descriptor).alpha(0.5f).flat(true).anchor(0.5f,0.5f).rotation(180.0f);
        mMap.addMarker(options);

        mMap.addMarker(new MarkerOptions().position(MUMBAI));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.setVisible(true);
                marker.setTitle("Mumbai");
                marker.setSnippet("Meri Jaan !!!!!");

                Toast.makeText(getApplicationContext(),"You clicked the marker",Toast.LENGTH_LONG).show();
                return false;
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener(){

            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(getApplicationContext(),"Drag started",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Toast.makeText(getApplicationContext(),"Dragging",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(getApplicationContext(),"Drag ended",Toast.LENGTH_LONG).show();

                LatLng latLng = marker.getPosition();
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;
                Toast.makeText(getApplicationContext(),"Latitude : "+latitude+"\n" +
                        "Longitude : "+longitude,Toast.LENGTH_LONG).show();

            }
        });

    }
}
