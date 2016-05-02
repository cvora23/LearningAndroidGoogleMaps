package cvora.learningandroidgooglemaps.Chapter123.Chapter4;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import cvora.learningandroidgooglemaps.R;

public class CreateInfoWindowForMarkerOnMap  extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    static final LatLng NEWARK = new LatLng(40.735188, -74.172414);
    static final LatLng MUMBAI = new LatLng(19.076, 72.8777);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_info_window_for_marker_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(MUMBAI).title("Mumbai").snippet("Meri Jaan !!!!"));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View view = getLayoutInflater().inflate(R.layout.info_window,null);

                LatLng latLng = marker.getPosition();
                ImageView icon = (ImageView)view.findViewById(R.id.icon);
                TextView title = (TextView)view.findViewById(R.id.title);
                TextView snippet = (TextView)view.findViewById(R.id.snippet);
                TextView lat = (TextView)view.findViewById(R.id.lat);
                TextView lng = (TextView)view.findViewById(R.id.lng);

                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());
                if(marker.getTitle().equals("Mumbai")){
                    icon.setImageResource(R.mipmap.mumbai);
                }
                lat.setText("Latitude: "+latLng.latitude);
                lng.setText("Longitude: "+latLng.longitude);

                return view;
            }
        });

    }
}
