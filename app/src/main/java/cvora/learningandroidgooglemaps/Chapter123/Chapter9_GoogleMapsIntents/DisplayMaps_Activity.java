package cvora.learningandroidgooglemaps.Chapter123.Chapter9_GoogleMapsIntents;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import cvora.learningandroidgooglemaps.R;

public class DisplayMaps_Activity extends AppCompatActivity {
    static final LatLng AGRA = new LatLng(27.174356, 78.042183);
    Button launch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_maps_);

        launch = (Button)findViewById(R.id.btn);
        launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMap();
            }
        });

    }

    public void launchMap(){

        String uriString = String.format("geo:%f,%f?z=%d",  AGRA.latitude, AGRA.longitude,12);
        Uri intentUri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW,intentUri);
        intent.setPackage("com.google.android.apps.maps");
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }
}
