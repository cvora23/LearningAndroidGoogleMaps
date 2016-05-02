package cvora.learningandroidgooglemaps.Chapter123;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.google.android.gms.maps.MapFragment;

import cvora.learningandroidgooglemaps.R;

public class AddingMapsProgramatically extends Activity {

    Fragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_maps_programatically);

        mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_frame,mapFragment);
        fragmentTransaction.commit();

    }
}
