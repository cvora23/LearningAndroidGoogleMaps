package cvora.learningandroidgooglemaps.Chapter123.Chapter6_WorkingWithCustomViews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import cvora.learningandroidgooglemaps.R;

public class AnimatingCamera_Activity extends AppCompatActivity implements OnMapReadyCallback {

    static final LatLng LONDON= new LatLng(51.519029, -0.130094);
    Button start,stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animating_camera_);
        MapFragment mapFragment =  (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        start = (Button)findViewById(R.id.btn_start);
        stop = (Button)findViewById(R.id.btn_stop);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final CameraPosition cameraPosition = new CameraPosition.Builder().
                target(LONDON).
                zoom(8).
                build();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),2000,new GoogleMap.CancelableCallback(){

                    @Override
                    public void onFinish() {
                        Log.d("GoogleMap","Animation finished");
                    }

                    @Override
                    public void onCancel() {
                        Log.d("GoogleMap","Animation interrupted");
                    }
                });

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.stopAnimation();
            }
        });

    }
}
