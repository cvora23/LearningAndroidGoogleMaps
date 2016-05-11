package cvora.learningandroidgooglemaps.Chapter123.Chapter9_GoogleMapsIntents;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import cvora.learningandroidgooglemaps.R;

public class DisplayStreetViewPanorama_Activity extends AppCompatActivity {
    static final LatLng NEWARK =  new LatLng(40.735188, -74.172414);
    Button launch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_street_view_panorama_);
        launch = (Button)findViewById(R.id.btn);
        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMap();
            }
        });
    }

    public void launchMap(){

        String uriString =  String.format("google.streetview:cbll=%f,%f&cbp=%d,%d,%d,%d,%d", NEWARK.latitude,NEWARK.longitude,0,60,0,0,15);
        Uri intentUri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
