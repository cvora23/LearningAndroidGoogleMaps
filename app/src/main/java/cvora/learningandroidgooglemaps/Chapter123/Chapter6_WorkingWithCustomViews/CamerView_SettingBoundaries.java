package cvora.learningandroidgooglemaps.Chapter123.Chapter6_WorkingWithCustomViews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import cvora.learningandroidgooglemaps.R;

public class CamerView_SettingBoundaries extends AppCompatActivity implements OnMapReadyCallback{

    static final LatLng NE = new LatLng(40.461341, -3.688721);
    static final LatLng SW = new LatLng(40.435998, -3.717903);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camer_view__setting_boundaries);
        MapFragment mapFragment =  (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLngBounds latLngBounds = new LatLngBounds(SW,NE);
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,600,600,20));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngBounds.getCenter(),8));
    }
}
