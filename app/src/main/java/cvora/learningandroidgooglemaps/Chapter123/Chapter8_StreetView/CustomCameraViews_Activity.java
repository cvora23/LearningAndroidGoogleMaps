package cvora.learningandroidgooglemaps.Chapter123.Chapter8_StreetView;

import android.app.Dialog;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

import cvora.learningandroidgooglemaps.R;

public class CustomCameraViews_Activity extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback {

    Dialog cameraDialog;
    EditText zoom,tilt,bearing;
    Button move_camera;
    static final LatLng AGRA = new LatLng(27.174356, 78.042183);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera_views_);
        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment)getFragmentManager().findFragmentById(R.id.map);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
        cameraDialog = new Dialog(this);
        cameraDialog.setTitle("Move Camera");
        cameraDialog.setContentView(R.layout.dialog_camera);

        zoom = (EditText)cameraDialog.findViewById(R.id.zoom);
        tilt = (EditText)cameraDialog.findViewById(R.id.tilt);
        bearing = (EditText)cameraDialog.findViewById(R.id.bearing);
        move_camera = (Button)cameraDialog.findViewById(R.id.button);
    }

    @Override
    public void onStreetViewPanoramaReady(final StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(AGRA);
        streetViewPanorama.setOnStreetViewPanoramaClickListener(new StreetViewPanorama.OnStreetViewPanoramaClickListener() {
            @Override
            public void onStreetViewPanoramaClick(StreetViewPanoramaOrientation streetViewPanoramaOrientation) {
                cameraDialog.show();
                Point point = streetViewPanorama.orientationToPoint(streetViewPanoramaOrientation);
                Log.d("Orientation","Tilt : "+streetViewPanoramaOrientation.tilt +" Bearing : "+streetViewPanoramaOrientation.bearing);
                Log.d("Orientation","X : "+point.x +" Y : "+point.y);

                StreetViewPanoramaOrientation orientation = streetViewPanorama.pointToOrientation(new Point(400,300));
                Log.d("Orientation","Tilt : "+orientation.tilt +" Bearing : "+orientation.bearing);

                LatLng location = streetViewPanorama.getLocation().position;
                Log.d("Location","Latitude : "+location.latitude +" Longitude : "+location.longitude);
            }
        });

        move_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String zoomText = zoom.getText().toString();
                String tiltText = tilt.getText().toString();
                String bearingText = bearing.getText().toString();

                if(!TextUtils.isEmpty(zoomText) && !TextUtils.isEmpty(tiltText) & !TextUtils.isEmpty(bearingText)){
                    try{
                        StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder()
                                .zoom(Float.valueOf(zoomText))
                                .bearing(Float.valueOf(bearingText))
                                .tilt(Float.valueOf(tiltText))
                                .build();
                        streetViewPanorama.animateTo(camera,500);
                        cameraDialog.dismiss();
                    }catch (NumberFormatException nfe){
                        Log.d("Map","Number format error");
                        cameraDialog.dismiss();
                    }
                }

            }
        });

    }
}
