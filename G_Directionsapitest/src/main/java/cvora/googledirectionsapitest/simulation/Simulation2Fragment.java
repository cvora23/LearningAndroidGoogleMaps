package cvora.googledirectionsapitest.simulation;


import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
public class Simulation2Fragment extends Fragment implements OnMapReadyCallback,RoutingListener,AdapterView.OnItemSelectedListener {

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
    LatLng addedWayPointLatLng = null;
    WayPoint addedWayPoint = null;
    Marker newAddedMarker = null;
    Spinner waypoint_spinner = null;

    private static final int[] COLORS = new int[]{R.color.primary_dark,R.color.primary,R.color.primary_light,R.color.accent,R.color.primary_dark_material_light};
    private static final String TAG = "Simulation2Fragment";
    private static final int HOME_ADDR_NO = 4;
    private static final int ZOOM_LEVEL = 10;
    private static int waypoint_spinner_posNo = -1;

    private static final String SIM1_FILE_NAME =  "Sim2Route1.csv";
    public static String locDatFName = SIM1_FILE_NAME;

    public Simulation2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"Simulation2Fragment::onCreateView");

        View view = inflater.inflate(R.layout.fragment_simulation2, container, false);

        initMap();

        startSim = (Button)view.findViewById(R.id.start_simulation_2);
        startSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Start Simulation 2");
                startSim.setEnabled(false);
                reset();
                route();
                waypoint_spinner.setEnabled(false);
                stopSimulation = false;
                keepUpdatingEta();
            }
        });

        stopSim = (Button)view.findViewById(R.id.stop_simulation_2);
        stopSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                MarkerAnimation.setShutdown(true);
                startMrkr.setVisible(false);
                startSim.setEnabled(true);
                waypoint_spinner.setEnabled(true);
            }
        });

        eta = (Button)view.findViewById(R.id.eta_button);

        waypoint_spinner = (Spinner)view.findViewById(R.id.waypoints_spinner);
        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(getActivity(),R.array.waypoints_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waypoint_spinner.setAdapter(adapter);
        waypoint_spinner.setOnItemSelectedListener(this);

        return view;
    }

    private void sendNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setSmallIcon(R.drawable.ic_star_black_24dp);
        mBuilder.setContentTitle("New Pickup time alert. Click Me !!!");
        mBuilder.setContentText(WayPointMgr.getInstance().getEta(home));
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager)getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private void reset(){
        stopSimulation = true;
        WayPointMgr.getInstance().clear();
        map.clear();
        clearNewAddedWayPoint();
        setAddresses();
        setWayPts();
        addWayPoint(waypoint_spinner_posNo);
        eta.setText(getString(R.string.eta_label));
    }

    private void keepUpdatingEta(){
        final int MULTIPLE = 10;
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(!stopSimulation){
                    eta.setText(getString(R.string.eta_label) + WayPointMgr.getInstance().getEta(home));
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

        FileHelper.setWayPoints(Simulation2Fragment.locDatFName,waypoints);
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

        sendNotification();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(parent.getContext(),"OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();
        waypoint_spinner_posNo = position;
        switch (waypoint_spinner_posNo){
            case 0:
            case 1:
            case 2:{
                addWayPoint(waypoint_spinner_posNo);
            }break;
            default:{

            }break;
        }
    }

    private void clearNewAddedWayPoint(){
        boolean ret = false;
        if(addedWayPointLatLng != null){
            ret = waypoints.remove(addedWayPointLatLng);
            if(ret != true){
                Log.e(TAG,"Error while removing WayPoint lat lng");
            }else{
                addedWayPointLatLng = null;
            }
        }
        if(addedWayPoint != null){
            ret = WayPointMgr.getInstance().removeWayPt(addedWayPoint);
            if(ret != true){
                Log.e(TAG,"Error while removing WayPoint");
            }else{
                addedWayPoint = null;
            }
        }
        if(newAddedMarker != null){
            newAddedMarker.remove();
            newAddedMarker = null;
        }
    }

    private void addWayPoint(int position){
        clearNewAddedWayPoint();
        if((position > 0) && (waypoints.size() > 2)){
            addedWayPointLatLng = new LatLng(GlobalValues.places.get(position-1).latitude,GlobalValues.places.get(position-1).longitude);
            waypoints.add(waypoints.size()-1,addedWayPointLatLng);
            addedWayPoint = new WayPoint();
            addedWayPoint.setLatLng(addedWayPointLatLng);
            WayPointMgr.getInstance().addWayPt(addedWayPoint);
            newAddedMarker = map.addMarker(new MarkerOptions().position(addedWayPointLatLng));
            BitmapDescriptor newAddedMarkerDefaultIconDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            newAddedMarker.setIcon(newAddedMarkerDefaultIconDescriptor);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
