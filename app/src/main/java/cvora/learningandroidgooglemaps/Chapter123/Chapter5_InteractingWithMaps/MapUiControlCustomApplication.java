package cvora.learningandroidgooglemaps.Chapter123.Chapter5_InteractingWithMaps;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import cvora.learningandroidgooglemaps.R;

public class MapUiControlCustomApplication extends AppCompatActivity implements OnMapReadyCallback {

    CharSequence mapSettings[] = {"Zoom Controls", "Compass", "My Location button", "Level Picker"};
    boolean b1[] = new boolean[mapSettings.length];
    boolean zoom = false;
    boolean compass = false;
    boolean location = false;
    boolean level = false;
    int requestCode1 = 1;
    int requestCode2 = 1;

    AlertDialog.Builder alert;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_ui_control_custom_application);
        initDialog();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
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
        map.setMyLocationEnabled(true);
        alert.show();
    }

    public void initDialog(){
        alert = new AlertDialog.Builder(this);
        alert.setTitle("Map Settings");
        alert.setMultiChoiceItems(mapSettings, b1, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (which == 0) {
                    zoom = isChecked;
                } else if (which == 1) {
                    compass = isChecked;
                } else if (which == 2) {
                    location = isChecked;
                } else if (which == 3) {
                    level = isChecked;
                }
            }
        });

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (zoom) {
                    map.getUiSettings().setZoomControlsEnabled(true);
                } else {
                    map.getUiSettings().setZoomControlsEnabled(false);
                }
                if (compass) {
                    map.getUiSettings().setCompassEnabled(true);
                } else {
                    map.getUiSettings().setCompassEnabled(false);
                }
                if (location) {
                    map.getUiSettings().setMyLocationButtonEnabled(true);
                } else {
                    map.getUiSettings().setMyLocationButtonEnabled(false);
                }
                if (level) {
                    map.getUiSettings().setIndoorLevelPickerEnabled(true);
                } else {
                    map.getUiSettings().setIndoorLevelPickerEnabled(false);
                }
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
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
