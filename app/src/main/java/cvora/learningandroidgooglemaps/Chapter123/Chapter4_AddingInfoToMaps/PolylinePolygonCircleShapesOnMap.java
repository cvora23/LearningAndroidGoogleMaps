package cvora.learningandroidgooglemaps.Chapter123.Chapter4_AddingInfoToMaps;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cvora.learningandroidgooglemaps.R;

public class PolylinePolygonCircleShapesOnMap extends Activity implements OnMapReadyCallback {

    MapFragment mapFragment;
    GoogleMap map;
    Button button;
    Button overlayButton;
    PopupMenu popup;
    PopupMenu overlayPopup;

    static final LatLng PARIS = new LatLng(48.8588589,2.3470599);
    static final LatLng NEWYORK = new LatLng(40.7033127,-73.979681);
    static final LatLng MADRID = new LatLng(40.4378271,-3.6795367);

    static final LatLng CHENNAI = new LatLng(13.0475604,80.2089535);
    static final LatLng BENGALURU = new LatLng(12.9539974,77.6309395);
    static final LatLng HYDERABAD = new LatLng(17.4123487,78.4080455);
    static final LatLng MUMBAI = new LatLng(19.076, 72.8777);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polyline_polygon_circle_shapes_on_map);

        button = (Button)findViewById(R.id.button);
        overlayButton = (Button)findViewById(R.id.overlayButton);
        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.fragMap);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NONE);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        overlayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showOverlayPopup();
            }
        });
    }

    public void showOverlayPopup(){
        if(overlayPopup == null){
            overlayPopup = new PopupMenu(PolylinePolygonCircleShapesOnMap.this, overlayButton);
            overlayPopup.getMenuInflater().inflate(R.menu.overlay_menu, overlayPopup.getMenu());
        }
        overlayPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.ground_overlay){
                    showGroundOverlay();
                    button.setText("Ground Overlay Example");
                }else if(item.getItemId() == R.id.polygon){
                    showTileOverlay();
                    button.setText("Tile Overlay Example");
                }
                return true;
           }
        });
        overlayPopup.show();
    }

    public void showGroundOverlay(){
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)).
                position(MUMBAI,200f,200f);
        map.addGroundOverlay(groundOverlayOptions);
    }

    public void showTileOverlay(){

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {

                String s = String.format("http://192.168.43.253/images/%d/%d/%d.jpg",
                        zoom, x, y);

                try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }
        };
        TileOverlayOptions tileOverlayOptions = new TileOverlayOptions().tileProvider(tileProvider);
        map.addTileOverlay(tileOverlayOptions);
    }

    public void showPopup() {
        if (popup == null) {
            popup = new PopupMenu(PolylinePolygonCircleShapesOnMap.this, button);
            popup.getMenuInflater().inflate(R.menu.shapes_on_map, popup.getMenu());
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId() == R.id.polyline){
                    showPolyLine();
                    button.setText("Polyline Example");
                }else if(item.getItemId() == R.id.polygon){
                    showPolygon();
                    button.setText("Polygon Example");
                }else if(item.getItemId() == R.id.hollow_polygon){
                    showHollowPolygon();
                    button.setText("Hollow Polygon Example");
                }else if(item.getItemId() == R.id.circle) {
                    showCircle();
                    button.setText("Circle Example");
                }
                return true;
            }
        });
        popup.show();
    }

    public void showPolyLine(){
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        ArrayList<LatLng>coordinates = new ArrayList<>();
        coordinates.add(PARIS);
        coordinates.add(NEWYORK);
        coordinates.add(MADRID);
        PolylineOptions polylineOptions = new PolylineOptions().addAll(coordinates);
        polylineOptions.geodesic(true);
        map.addPolyline(polylineOptions);
    }

    public void showPolygon(){
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        ArrayList<LatLng>coordinates = new ArrayList<>();
        coordinates.add(CHENNAI);
        coordinates.add(BENGALURU);
        coordinates.add(HYDERABAD);
        PolygonOptions polygonOptions = new PolygonOptions().addAll(coordinates);
        map.addPolygon(polygonOptions);
    }

    public void showHollowPolygon(){
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        final LatLng Outer1 = new LatLng(13.079624, 80.332285);
        final LatLng Outer2 = new LatLng(13.079624, 80.462747);
        final LatLng Outer3 = new LatLng(12.939798, 80.475794);
        final LatLng Outer4 = new LatLng(12.941806, 80.315119);

        final LatLng Inner1 = new LatLng(13.060896, 80.350137);
        final LatLng Inner2 = new LatLng(13.061565, 80.413995);
        final LatLng Inner3 = new LatLng(12.985969, 80.413995);
        final LatLng Inner4 = new LatLng(12.985969, 80.334345);

        ArrayList<LatLng> outerCoordinates = new ArrayList<>();
        ArrayList<LatLng> innerCoordinates = new ArrayList<>();
        outerCoordinates.add(Outer1);
        outerCoordinates.add(Outer2);
        outerCoordinates.add(Outer3);
        outerCoordinates.add(Outer4);
        innerCoordinates.add(Inner1);
        innerCoordinates.add(Inner2);
        innerCoordinates.add(Inner3);
        innerCoordinates.add(Inner4);

        PolygonOptions polygonOptions = new PolygonOptions().addAll(outerCoordinates).addHole(innerCoordinates).fillColor(Color.GREEN);
        map.addPolygon(polygonOptions);
    }

    public void showCircle(){
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CircleOptions circleOptions = new CircleOptions().center(MUMBAI).radius(25).strokeColor(Color.BLUE).fillColor(Color.BLUE);
        map.addCircle(circleOptions);
    }

}
