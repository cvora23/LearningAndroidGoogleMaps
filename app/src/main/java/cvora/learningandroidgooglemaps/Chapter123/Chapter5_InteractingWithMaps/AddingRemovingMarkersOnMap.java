package cvora.learningandroidgooglemaps.Chapter123.Chapter5_InteractingWithMaps;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import cvora.learningandroidgooglemaps.R;

public class AddingRemovingMarkersOnMap extends AppCompatActivity implements OnMapReadyCallback {

    Dialog dialog;
    EditText title,snippet;
    Button add,cancel;
    AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_removing_markers_on_map);

        initAddMarkerDialog();
        initRemoveMarkerDialog();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        initInfoWindowClick(googleMap);
        initMapClick(googleMap);

    }
    public void initAddMarkerDialog(){
        dialog = new Dialog(this);
        dialog.setTitle("Add marker !");
        dialog.setContentView(R.layout.dialog_add);
        title = (EditText)dialog.findViewById(R.id.title);
        snippet = (EditText)dialog.findViewById(R.id.snippet);
        add = (Button)dialog.findViewById(R.id.btn_add);
        cancel = (Button)dialog.findViewById(R.id.btn_cancel);
    }

    public void initRemoveMarkerDialog(){
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Delete Marker !");
        alertDialog.setMessage("Do you want to delete the Marker !");
    }

    public  void initInfoWindowClick(GoogleMap googleMap){
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        marker.remove();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }
    public void initMapClick(final GoogleMap googleMap){
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                title.setText("");
                snippet.setText("");
                dialog.show();
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (title.getText().toString().isEmpty() || snippet.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Text fields cannot be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            googleMap.addMarker(new MarkerOptions().position(latLng).title(title.getText().toString()).snippet(snippet.getText().toString()));
                            dialog.dismiss();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
