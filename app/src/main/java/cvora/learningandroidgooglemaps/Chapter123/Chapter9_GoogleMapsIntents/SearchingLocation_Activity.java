package cvora.learningandroidgooglemaps.Chapter123.Chapter9_GoogleMapsIntents;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cvora.learningandroidgooglemaps.R;

public class SearchingLocation_Activity extends AppCompatActivity {
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
        //1: Searching for a location
        //Uri intentUri = Uri.parse("geo:0,0?q=Agra,+Uttar+Pradesh,+India");

        //2: Advanced Search  - we can search for places of interest such as restaurants, beach houses, and gas filling stations.
        // Uri intentUri = Uri.parse("geo:0,0?q=beach resort");

        //3: Label Location
        Uri intentUri =  Uri.parse("geo:0,0?q=27.175015,78.042155(Taj Mahal + Agra)");
        Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
