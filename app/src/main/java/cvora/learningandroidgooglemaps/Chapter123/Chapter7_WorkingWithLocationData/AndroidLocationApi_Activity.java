package cvora.learningandroidgooglemaps.Chapter123.Chapter7_WorkingWithLocationData;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cvora.learningandroidgooglemaps.R;

public class AndroidLocationApi_Activity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    double latitude, longitude;
    int requestCode1 = 1;
    int requestCode2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_location_api_);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
            printLocationDetails(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }else{
            Toast.makeText(getApplicationContext(),"Enable GPS",Toast.LENGTH_LONG).show();
        }
    }

    private void printLocationDetails(Location location){

        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Toast.makeText(getApplicationContext(),"Latitde: "+latitude + "\nLongitude : "+longitude,Toast.LENGTH_SHORT).show();
        }else{
            Log.e("prinLocationDetails","location obj is null");
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        printLocationDetails(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Updates",provider+" Changed");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Updates",provider+" Enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Updates",provider+" Disabled");
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
