package cvora.learningandroidgooglemaps.Chapter123.Chapter6_WorkingWithCustomViews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import cvora.learningandroidgooglemaps.R;

public class CameraView_CameraPosition_Activity extends AppCompatActivity implements OnMapReadyCallback {

    static final LatLng NEWARK = new LatLng(40.735188, -74.172414);
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view__camera_position_);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn = (Button)findViewById(R.id.button);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NEWARK,4));
            }
        });

    }
}
