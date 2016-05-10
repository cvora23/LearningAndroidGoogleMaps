package cvora.learningandroidgooglemaps.Chapter123.Chapter5_InteractingWithMaps;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

import cvora.learningandroidgooglemaps.R;

public class GoogleMapsProjectionClass extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps_projection_class);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                Projection projection = googleMap.getProjection();
                LatLng latLng = projection.fromScreenLocation(new Point(100,100));
                VisibleRegion visibleRegion = projection.getVisibleRegion();
                Log.d("Projection","North East : "+visibleRegion.latLngBounds.northeast.latitude + ","+visibleRegion.latLngBounds.northeast.longitude);
                Log.d("Projection","South West : "+visibleRegion.latLngBounds.southwest.latitude + ","+visibleRegion.latLngBounds.southwest.longitude);
                Log.d("Projection","Point 100, 100 :"+latLng.latitude + ","+latLng.longitude);
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Projection projection = googleMap.getProjection();
                Point point = projection.toScreenLocation(latLng);
                Log.d("Projection","X : "+point.x +",Y : "+point.y);
            }
        });

    }
}
