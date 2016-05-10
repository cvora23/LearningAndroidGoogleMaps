package cvora.learningandroidgooglemaps.Chapter123.Chapter7_WorkingWithLocationData;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import cvora.learningandroidgooglemaps.R;

public class GoogleLocationApi_LocatIonUpdates_Activity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Button start, stop;
    double latitude;
    double longitude;
    Boolean update = false;
    int requestCode1 = 1;
    int requestCode2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_location_api__locat_ion_updates_);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();


        locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        start = (Button) findViewById(R.id.btn_start);
        stop = (Button) findViewById(R.id.btn_stop);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                update = true;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(GoogleLocationApi_LocatIonUpdates_Activity.this, new String[]
                            {Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode1);
                    ActivityCompat.requestPermissions(GoogleLocationApi_LocatIonUpdates_Activity.this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION}, requestCode2);
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,GoogleLocationApi_LocatIonUpdates_Activity.this);

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update = false;
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,GoogleLocationApi_LocatIonUpdates_Activity.this);

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (update)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode2);
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,GoogleLocationApi_LocatIonUpdates_Activity.this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("Updates", "Latitude :" + latitude);
            Log.d("Updates","Longitude :" + longitude);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("onRequestPermRes","ACCESS_COARSE_LOCATION success");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("onRequestPermRes","ACCESS_COARSE_LOCATION error");
                }
                return;
            }
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("onRequestPermRes","ACCESS_FINE_LOCATION success");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("onRequestPermRes","ACCESS_FINE_LOCATION error");
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

}
