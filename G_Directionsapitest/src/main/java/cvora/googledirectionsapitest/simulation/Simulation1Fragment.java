package cvora.googledirectionsapitest.simulation;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import cvora.googledirectionsapitest.R;
import cvora.googledirectionsapitest.animation.MarkerAnimation;
import cvora.googledirectionsapitest.common.FileHelper;
import cvora.googledirectionsapitest.common.GlobalValues;
import cvora.googledirectionsapitest.common.Util;
import cvora.googledirectionsapitest.common.WayPoint;
import cvora.googledirectionsapitest.common.WayPointMgr;
import cvora.googledirectionsapitest.directions.MyGoogleMapInfoWindowAdapter;
import cvora.googledirectionsapitest.routing.AbstractRouting;
import cvora.googledirectionsapitest.routing.Route;
import cvora.googledirectionsapitest.routing.RouteException;
import cvora.googledirectionsapitest.routing.Routing;
import cvora.googledirectionsapitest.routing.RoutingListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Simulation1Fragment extends Fragment implements OnMapReadyCallback,RoutingListener {

    private LatLng start,end,home;
    private TextView driverAddress,schoolAddress,homeAddress;
    List<LatLng> waypoints = new ArrayList<LatLng>();
    private Toolbar toolbar;
    private GoogleMap map;
    private Marker startMrkr;
    private List<LatLng> routingPts;
    private MyGoogleMapInfoWindowAdapter myGoogleMapInfoWindowAdapter;
    SupportMapFragment mapFragment;
    Button eta,startSim,stopSim;
    private boolean stopSimulation = false;

    private static final int[] COLORS = new int[]{R.color.primary_dark,R.color.primary,R.color.primary_light,R.color.accent,R.color.primary_dark_material_light};
    private static final String TAG = "Simulation1Fragment";
    private static final int HOME_ADDR_NO = 4;
    private static final int ZOOM_LEVEL = 10;


    public Simulation1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG,"Simulation1Fragment::onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simulation1, container, false);

//        driverAddress = (TextView)view.findViewById(R.id.start_addr);
//        schoolAddress = (TextView)view.findViewById(R.id.dest_addr);
//        homeAddress = (TextView)view.findViewById(R.id.home_addr);

        initMap();

        startSim = (Button)view.findViewById(R.id.start_simulation_1);
        startSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Start Simulation 1");
                startSim.setEnabled(false);
                reset();
                route();
                stopSimulation = false;
                keepUpdatingEta();
            }
        });

        stopSim = (Button)view.findViewById(R.id.stop_simulation_1);
        stopSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                MarkerAnimation.setShutdown(true);
                startMrkr.setVisible(false);
                startSim.setEnabled(true);
            }
        });

        eta = (Button)view.findViewById(R.id.eta_button);

        return view;
    }

    private void reset(){
        stopSimulation = true;
        WayPointMgr.getInstance().clear();
        map.clear();
        setAddresses();
        setWayPts();
        eta.setText("ETA : ");
    }

    private void keepUpdatingEta(){
        stopSimulation = false;
        final int MULTIPLE = 10;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(!stopSimulation){
                    eta.setText("ETA : "+ WayPointMgr.getInstance().getEta(home));
                    handler.postDelayed(this,MarkerAnimation.ANIMATION_DELAY * MULTIPLE);
                }
            }
        });

    }

    private void initMap(){
        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        /*
        * Updates the bounds being used by the auto complete adapter based on the position of the
        * map.
        * */
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
            }
        });

        map.setInfoWindowAdapter(myGoogleMapInfoWindowAdapter);
    }

    private void setWayPts(){

        // Add all waypoints using waypoint manager.
        for(int i=0;i<waypoints.size();i++){
            WayPoint wayPoint = new WayPoint();
            wayPoint.setLatLng(waypoints.get(i));
            WayPointMgr.getInstance().addWayPt(wayPoint);
        }

        // Set Driver, Home and School Markers.
        Marker driverMarker,homeMarker,schoolMarker;
        driverMarker = map.addMarker(new MarkerOptions().position(start));
        startMrkr = map.addMarker(new MarkerOptions().position(start));
        startMrkr.setVisible(false);
        homeMarker = map.addMarker(new MarkerOptions().position(home));
        schoolMarker = map.addMarker(new MarkerOptions().position(end));

        BitmapDescriptor driverDefaultIconDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        BitmapDescriptor homeDefaultIconDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        BitmapDescriptor schoolDefaultIconDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
//        BitmapDescriptor driverIconDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.driver);
//        BitmapDescriptor homeIconDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.home);
//        BitmapDescriptor schoolIconDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.school);
        driverMarker.setIcon(driverDefaultIconDescriptor);
        homeMarker.setIcon(homeDefaultIconDescriptor);
        schoolMarker.setIcon(schoolDefaultIconDescriptor);

        setHomeGroundOverlay();

        // Set rest of the Markers.
        for(int i=1;i<waypoints.size()-1;i++){
            LatLng latLng = waypoints.get(i);
            if(!latLng.equals(home)){
                map.addMarker( new MarkerOptions().position(latLng));
            }
        }
    }

    private void setHomeGroundOverlay(){

        GroundOverlayOptions homeGroundOverlayOptions = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.home)).
                position(home,2000f, 2000f);
        GroundOverlayOptions schoolGroundOverlayOptions = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.school)).
                position(end,2000f, 2000f);
        GroundOverlayOptions driverGroundOverlayOptions = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.driver)).
                position(start,2000f, 2000f);
        map.addGroundOverlay(homeGroundOverlayOptions);
        map.addGroundOverlay(schoolGroundOverlayOptions);
        map.addGroundOverlay(driverGroundOverlayOptions);
    }

    private void setAddresses(){

        waypoints.clear();

        FileHelper.setWayPoints(TabbedSimulationActivity.locDatFName,waypoints);
        start = waypoints.get(0);
        end = waypoints.get(waypoints.size()-1);
        home = waypoints.get(HOME_ADDR_NO);

        CameraUpdate center = CameraUpdateFactory.newLatLng(home);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(ZOOM_LEVEL);
        map.moveCamera(center);
        map.animateCamera(zoom);

        myGoogleMapInfoWindowAdapter = new MyGoogleMapInfoWindowAdapter();
        myGoogleMapInfoWindowAdapter.setBaseActivity(getActivity());
        myGoogleMapInfoWindowAdapter.setEndLocation(end);

//        driverAddress.setText(Util.getAddress(getActivity(),start));
//        homeAddress.setText(Util.getAddress(getActivity(),home));
//        schoolAddress.setText(Util.getAddress(getActivity(),end));
    }

    private void route(){

        Routing.Builder routingBuilder = new Routing.Builder();
        routingBuilder.travelMode(AbstractRouting.TravelMode.DRIVING);
        routingBuilder.withListener(this);
        routingBuilder.alternativeRoutes(false);
        routingBuilder.optimize(true);
        routingBuilder.stopover(true);
        routingBuilder.key(GlobalValues.DEVELOPER_KEY);
        routingBuilder.waypoints(waypoints);
        Routing routing = routingBuilder.build();
        routing.execute();
    }

    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(List<Route> route, int shortestRouteIndex) {

        int colorIndex = Util.getRandom(COLORS.length);
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(ContextCompat.getColor(getActivity(),COLORS[colorIndex]));
        polyOptions.width(10 + colorIndex * 3);
        routingPts = route.get(0).getPoints();
        polyOptions.addAll(routingPts);
        map.addPolyline(polyOptions);

        animateRoute();
    }

    @Override
    public void onRoutingCancelled() {
        Log.i(TAG, "Routing was cancelled.");
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getActivity(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void animateRoute(){
        startMrkr.setPosition(start);
        startMrkr.setVisible(true);
        MarkerAnimation.animateGivenRoutingPoints(startMrkr,routingPts);
    }
}
