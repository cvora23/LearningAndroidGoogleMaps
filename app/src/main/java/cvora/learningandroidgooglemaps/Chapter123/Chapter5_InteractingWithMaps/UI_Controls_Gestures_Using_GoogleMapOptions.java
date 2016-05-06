package cvora.learningandroidgooglemaps.Chapter123.Chapter5_InteractingWithMaps;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;

import cvora.learningandroidgooglemaps.R;

public class UI_Controls_Gestures_Using_GoogleMapOptions extends Activity {

    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui__controls__gestures__using__google_map_options);

        GoogleMapOptions options = new GoogleMapOptions();
        options.compassEnabled(false);
        options.zoomControlsEnabled(true);
        options.zoomGesturesEnabled(false);
        options.scrollGesturesEnabled(false);
        options.tiltGesturesEnabled(false);
        options.rotateGesturesEnabled(false);

        mapFragment = MapFragment.newInstance(options);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_frame,mapFragment);
        fragmentTransaction.commit();
    }
}
