package cvora.learningandroidgooglemaps.Chapter123.Chapter8_StreetView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

import cvora.learningandroidgooglemaps.R;

public class CustomizingStreetViewPanorama_Activity extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback {
    static final LatLng NEWARK = new LatLng(40.735188, -74.172414);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customizing_street_view_panorama_);
        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment)getFragmentManager().findFragmentById(R.id.map);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(NEWARK);
//        streetViewPanorama.setPanningGesturesEnabled(false);
//        streetViewPanorama.setUserNavigationEnabled(false);
//        streetViewPanorama.setZoomGesturesEnabled(false);
//        streetViewPanorama.setStreetNamesEnabled(false);

        streetViewPanorama.setOnStreetViewPanoramaClickListener(new StreetViewPanorama.OnStreetViewPanoramaClickListener() {
            @Override
            public void onStreetViewPanoramaClick(StreetViewPanoramaOrientation streetViewPanoramaOrientation) {
                float tilt = streetViewPanoramaOrientation.tilt;
                float bearing = streetViewPanoramaOrientation.bearing;
                Toast.makeText(getApplicationContext(),"Tilt : "+tilt+" Bearing : "+bearing,Toast.LENGTH_SHORT).show();
            }
        });

        streetViewPanorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
            @Override
            public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
                LatLng location = streetViewPanoramaLocation.position;
                Log.d("Panorama change","Location "+location+" Links : "+streetViewPanoramaLocation.links.length);
            }
        });

    }
}
