package cvora.learningandroidgooglemaps.Chapter123.Chapter5_InteractingWithMaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;

import cvora.learningandroidgooglemaps.R;

public class MapEventsActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_events);


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(getApplicationContext(), "Latitude : "+latLng.latitude + "\nLongitude : "+latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                Toast.makeText(getApplicationContext(),"Camera changed",Toast.LENGTH_LONG).show();
                Log.d("Latitude ",String.valueOf(cameraPosition.target.latitude));
                Log.d("Longitude ",String.valueOf(cameraPosition.target.longitude));
                Log.d("Bearing ",String.valueOf(cameraPosition.bearing));
                Log.d("Tilt ",String.valueOf(cameraPosition.tilt));
                Log.d("Zoom",String.valueOf(cameraPosition.zoom));
            }
        });

        googleMap.setOnIndoorStateChangeListener(new GoogleMap.OnIndoorStateChangeListener() {
            @Override
            public void onIndoorBuildingFocused() {
                Toast.makeText(getApplicationContext(),"Building Focused",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onIndoorLevelActivated(IndoorBuilding indoorBuilding) {
                Toast.makeText(getApplicationContext(),"Level Activated : "+indoorBuilding.getActiveLevelIndex(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
