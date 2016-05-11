package cvora.learningandroidgooglemaps.Chapter123.Chapter8_StreetView;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

import cvora.learningandroidgooglemaps.R;

public class StreetViewPanoramaFragment_Activity extends Activity implements OnStreetViewPanoramaReadyCallback{
    static final LatLng NEWARK = new LatLng(40.735188, -74.172414);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_view_panorama_fragment_);
        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment)getFragmentManager().findFragmentById(R.id.map);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(NEWARK);
    }
}
