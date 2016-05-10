package cvora.learningandroidgooglemaps.Chapter123.Chapter7_WorkingWithLocationData;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cvora.learningandroidgooglemaps.R;

public class GoogleLocation_GetLastKnowLocation_Activity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient googleApiClient;
    Location location;
    AlertDialog.Builder alertDialog;
    Button button;
    double latitude;
    double longitude;
    int requestCode1 = 1;
    int requestCode2 = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_location__get_last_know_location_);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Location");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude != 0 && longitude != 0) {
                    alertDialog.show();
                }
            }
        });

    }


    public void getAddress(Location location){
        if(Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            String addressText = null;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                addressText = String.format(
                        "%s, %s, %s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality(),
                        address.getCountryName(),
                        address.getPostalCode());

            }
            alertDialog.setMessage(addressText);
        } else {
            Toast.makeText(this, "Geocoder could not implemented", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},requestCode1);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},requestCode2);
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            alertDialog.setMessage("Latitude : "+latitude + "\nLongitude : "+longitude);
        }

        if (location != null) {
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run(){
                    getAddress(location);
                }
            });
            thread.start();

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Connection","Code : "+i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Connection","Code :"+connectionResult.getErrorCode());
        try {
            connectionResult.startResolutionForResult(GoogleLocation_GetLastKnowLocation_Activity.this, 1000);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                if (!googleApiClient.isConnected()){
                    googleApiClient.connect();
                }
            }
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
