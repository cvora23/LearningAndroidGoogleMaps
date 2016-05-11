package cvora.learningandroidgooglemaps.Chapter123.Chapter8_StreetView;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.model.LatLng;

import cvora.learningandroidgooglemaps.R;

public class AddingStreetViewPanoramaFragmentProgramatically_Activity extends AppCompatActivity {
    static final LatLng NEWARK = new LatLng(40.735188, -74.172414);
    Fragment streetViewPanoramaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_street_view_panorama_fragment_programatically_);

        StreetViewPanoramaOptions streetViewPanoramaOptions = new StreetViewPanoramaOptions();
        streetViewPanoramaOptions.position(NEWARK);

        streetViewPanoramaFragment = StreetViewPanoramaFragment.newInstance(streetViewPanoramaOptions);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_frame,streetViewPanoramaFragment);
        fragmentTransaction.commit();

    }
}
