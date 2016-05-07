package cvora.learningandroidgooglemaps.Chapter123.Chapter6_WorkingWithCustomViews;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import cvora.learningandroidgooglemaps.R;

public class CameraView_CameraPositon2_Activity extends AppCompatActivity implements OnMapReadyCallback{

    Dialog cameraDialog;
    EditText zoom,tilt,bearing;
    Button move_camera;
    LatLng lngClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view__camera_positon2_);
        initDialog();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void initDialog(){
        cameraDialog = new Dialog(this);
        cameraDialog.setTitle("Move Camera");
        cameraDialog.setContentView(R.layout.dialog_camera);

        zoom = (EditText) cameraDialog.findViewById(R.id.zoom);
        tilt = (EditText)cameraDialog.findViewById(R.id.tilt);
        bearing = (EditText)cameraDialog.findViewById(R.id.bearing);
        move_camera = (Button)cameraDialog.findViewById(R.id.button);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        initMapLongClick(googleMap);
        moveCamera(googleMap);
    }

    public void initMapLongClick(GoogleMap googleMap){
        googleMap.setOnMapLongClickListener(new  GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                cameraDialog.show();
                lngClick = latLng;
            }
        });
    }

    public void moveCamera(final GoogleMap googleMap){
        move_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zoomText = zoom.getText().toString();
                String tiltText = tilt.getText().toString();
                String bearingText = bearing.getText().toString();

                Log.d(".setOnClickListener","Zoom : " + zoomText);
                Log.d(".setOnClickListener","Tilt : " + tiltText);
                Log.d(".setOnClickListener","Bearing : " + bearingText);

                if(!(zoomText.isEmpty() || tiltText.isEmpty() ||  bearingText.isEmpty() )){

                    try {
                        CameraPosition cameraPosition =  new CameraPosition.Builder().target(lngClick).zoom(Float.valueOf(zoomText)).tilt(Float.valueOf(tiltText)).bearing(Float.valueOf(bearingText)).build();
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        cameraDialog.dismiss();
                    } catch (NumberFormatException nfe) {
                        Log.d("Map","Number Format Error");
                    }
                }
                cameraDialog.dismiss();
            }
        });
    }


}
