package cvora.learningandroidgooglemaps.Chapter123.Chapter6_WorkingWithCustomViews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import cvora.learningandroidgooglemaps.R;

public class Panning_Map_Activity extends AppCompatActivity implements OnMapReadyCallback {

    Button up,down,left,right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panning__map_);

        MapFragment mapFragment =  (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        up = (Button)findViewById(R.id.btn_up);
        down = (Button)findViewById(R.id.btn_down);
        left = (Button)findViewById(R.id.btn_left);
        right = (Button)findViewById(R.id.btn_right);

    }

    @Override
    public void onMapReady(final GoogleMap map) {
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.moveCamera(CameraUpdateFactory.scrollBy(0,-10));
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.moveCamera(CameraUpdateFactory.scrollBy(0,10));

            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.moveCamera(CameraUpdateFactory.scrollBy(-10,0));

            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.moveCamera(CameraUpdateFactory.scrollBy(10,0));

            }
        });
    }
}
