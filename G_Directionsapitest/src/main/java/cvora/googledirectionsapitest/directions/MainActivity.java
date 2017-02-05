package cvora.googledirectionsapitest.directions;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVReader;
import cvora.googledirectionsapitest.R;
import cvora.googledirectionsapitest.animation.LatLngInterpolator;
import cvora.googledirectionsapitest.animation.MarkerAnimation;
import cvora.googledirectionsapitest.common.FileHelper;
import cvora.googledirectionsapitest.common.GlobalValues;
import cvora.googledirectionsapitest.common.Util;
import cvora.googledirectionsapitest.common.WayPoint;
import cvora.googledirectionsapitest.common.WayPointMgr;
import cvora.googledirectionsapitest.routing.AbstractRouting;
import cvora.googledirectionsapitest.routing.Route;
import cvora.googledirectionsapitest.routing.RouteException;
import cvora.googledirectionsapitest.routing.Routing;
import cvora.googledirectionsapitest.routing.RoutingListener;

public class MainActivity extends AppCompatActivity implements
        RoutingListener,GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,OnMapReadyCallback{

    private static final String TAG = "MainActivity";

    protected GoogleApiClient googleApiClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private AutoCompleteTextView srcAutoCompleteTextView,destAutoCompleteTextView;
    private TextView placeDetailsText;
    private TextView placeDetailsAttribution;
    private LatLng start,end;
    private Marker startMrkr,endMrkr;
    List<LatLng> waypoints;
    private GoogleMap map;
    private ProgressDialog progressDialog;
    private List<Polyline> polylines;
    private List<LatLng> routingPts;
    private static final int[] COLORS = new int[]{R.color.primary_dark,R.color.primary,R.color.primary_light,R.color.accent,R.color.primary_dark_material_light};

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private static final LatLng MUMBAI = new LatLng(19.0760,72.8777);
    private MyGoogleMapInfoWindowAdapter myGoogleMapInfoWindowAdapter;

    private static final String ROUTE1_FILE_NAME =  "Route1.csv";
    private static final String ROUTE2_FILE_NAME =  "Route2.csv";
    private static final String ROUTE3_FILE_NAME =  "Route3.csv";
    private Toolbar toolbar;
    private String locDatFName = ROUTE1_FILE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        MapsInitializer.initialize(this);
        googleApiClient.connect();

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        if(mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map,mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        polylines = new ArrayList<>();

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        FileHelper.setApplicationContext(this);

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        srcAutoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.src_autocomplete_places);
        destAutoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.dest_autocomplete_places);

        // Register a listener that receives callbacks when a suggestion has been selected
        srcAutoCompleteTextView.setOnItemClickListener(srcAutocompleteClickListener);
        destAutoCompleteTextView.setOnItemClickListener(destAutocompleteClickListener);

        // Retrieve the TextViews that will display details and attributions of the selected place.
        // \\TODO: Might need it for future
        //placeDetailsText = (TextView) findViewById(R.id.place_details);
        //placeDetailsAttribution = (TextView) findViewById(R.id.place_attribution);

        // create the place auto complete adapter
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this,googleApiClient,BOUNDS_GREATER_SYDNEY,null);

        // Set up the 'clear text' button that clears the text in the autocomplete view
        Button clearButton = (Button) findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                srcAutoCompleteTextView.setText("");
                destAutoCompleteTextView.setText("");
            }
        });

        Button getDirectionButton = (Button)findViewById(R.id.get_directions);
        getDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                route();
            }
        });

        Button startAnimButton = (Button)findViewById(R.id.start_anim);
        startAnimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animateRoute();

            }
        });

        Button loadSampleRouteButton = (Button)findViewById(R.id.load_map);
        loadSampleRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readSendLocationData(locDatFName);
            }
        });

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        srcAutoCompleteTextView.setAdapter(placeAutocompleteAdapter);
        destAutoCompleteTextView.setAdapter(placeAutocompleteAdapter);

        myGoogleMapInfoWindowAdapter = new MyGoogleMapInfoWindowAdapter();
        myGoogleMapInfoWindowAdapter.setBaseActivity(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.route1_simulation){
            locDatFName = ROUTE1_FILE_NAME;
        }else if(id == R.id.route2_simulation){
            locDatFName = ROUTE2_FILE_NAME;
        }else if(id == R.id.route3_simulation){
            locDatFName = ROUTE3_FILE_NAME;
        }

        return super.onOptionsItemSelected(item);
    }

    private String readSendLocationData(String locDatFName){

        progressDialog = ProgressDialog.show(this, "Please wait.","Fetching route information.", true);

        InputStream inputStream = null;
        try{
            inputStream = getAssets().open(locDatFName);
            CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));
            List content = csvReader.readAll();
            Iterator<Object> objectIterator = content.iterator();
            String[] row = null;
            waypoints  = new ArrayList<>();
            while (objectIterator.hasNext()){
                // Create connection to send GCM Message request.
                row = (String[])objectIterator.next();
                System.out.println("Latitude : " + row[6] + " Longitude :  " + row[7]);
                LatLng location = new LatLng(Double.parseDouble(row[6]),Double.parseDouble(row[7]));
                waypoints.add(location);
            }

            // add all other markers on the route
            for(int i=1;i<waypoints.size()-1;i++){
                LatLng latLng = waypoints.get(i);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                map.addMarker(markerOptions);
            }

            start = waypoints.get(0);
            end = waypoints.get(waypoints.size()-1);


            // Start marker
            MarkerOptions options = new MarkerOptions();
            options.position(start);
            startMrkr = map.addMarker(options);

            // End marker
            options = new MarkerOptions();
            options.position(end);
            endMrkr = map.addMarker(options);

            myGoogleMapInfoWindowAdapter.setEndLocation(end);

            // Add all waypoints using waypoint manager.
            for(int i=0;i<waypoints.size();i++){
                WayPoint wayPoint = new WayPoint();
                wayPoint.setLatLng(waypoints.get(i));
                WayPointMgr.getInstance().addWayPt(wayPoint);
            }

            Routing.Builder routingBuilder = new Routing.Builder();
            routingBuilder.travelMode(AbstractRouting.TravelMode.DRIVING);
            routingBuilder.withListener(this);
            routingBuilder.alternativeRoutes(false);
            routingBuilder.optimize(true);
            routingBuilder.stopover(true);
            routingBuilder.key(GlobalValues.DEVELOPER_KEY);
            routingBuilder.waypoints(waypoints);
            routingBuilder.departureTime(Util.sampleFutureTime());
            Routing routing = routingBuilder.build();
            routing.execute();
            return "Success";
        }catch (IOException e){
            e.printStackTrace();
            return "Error while reading CSV file";
        }
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener srcAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(mSrcUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mSrcUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            start = place.getLatLng();
            // TODO: Might need it for future. Not sure
//
//
//            // Format details of the place for display and show it in a TextView.
//            placeDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
//                    place.getId(), place.getAddress(), place.getPhoneNumber(),
//                    place.getWebsiteUri()));
//
//            // Display the third party attributions if set.
//            final CharSequence thirdPartyAttribution = places.getAttributions();
//            if (thirdPartyAttribution == null) {
//                placeDetailsAttribution.setVisibility(View.GONE);
//            } else {
//                placeDetailsAttribution.setVisibility(View.VISIBLE);
//                placeDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
//            }
//
            Log.i(TAG, "Place details received - Start LatLng : " + start);

            places.release();
        }
    };

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener destAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(mDestUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mDestUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            end = place.getLatLng();
            // TODO: Might need it for future. Not sure
//
//
//            // Format details of the place for display and show it in a TextView.
//            placeDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
//                    place.getId(), place.getAddress(), place.getPhoneNumber(),
//                    place.getWebsiteUri()));
//
//            // Display the third party attributions if set.
//            final CharSequence thirdPartyAttribution = places.getAttributions();
//            if (thirdPartyAttribution == null) {
//                placeDetailsAttribution.setVisibility(View.GONE);
//            } else {
//                placeDetailsAttribution.setVisibility(View.VISIBLE);
//                placeDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
//            }
//
            Log.i(TAG, "Place details received - End LatLng : " + end);

            myGoogleMapInfoWindowAdapter.setEndLocation(end);
            places.release();
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

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
                placeAutocompleteAdapter.setBounds(bounds);
            }
        });

        CameraUpdate center = CameraUpdateFactory.newLatLng(MUMBAI);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(4);

        map.moveCamera(center);
        map.animateCamera(zoom);
        map.setInfoWindowAdapter(myGoogleMapInfoWindowAdapter);
    }

    public void route()
    {
        if(start==null || end==null)
        {
            if(start==null)
            {
                if(srcAutoCompleteTextView.getText().length()>0)
                {
                    srcAutoCompleteTextView.setError("Choose location from dropdown.");
                }
                else
                {
                    Toast.makeText(this,"Please choose a starting point.",Toast.LENGTH_SHORT).show();
                }
            }
            if(end==null)
            {
                if(destAutoCompleteTextView.getText().length()>0)
                {
                    destAutoCompleteTextView.setError("Choose location from dropdown.");
                }
                else
                {
                    Toast.makeText(this,"Please choose a destination.",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            progressDialog = ProgressDialog.show(this, "Please wait.",
                    "Fetching route information.", true);
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(start, end)
                    .key(GlobalValues.DEVELOPER_KEY)
                    .optimize(true)
                    .stopover(true)
                    .build();
            routing.execute();
        }
    }

    private void animateRoute(){
        LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Spherical();
        MarkerAnimation.animateGivenRoutingPoints(startMrkr,routingPts);
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        // The Routing request failed
        progressDialog.dismiss();
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }

    private void printRoutingPts(List<LatLng>routingPts){

        Iterator<LatLng> objectIterator = routingPts.iterator();
        while(objectIterator.hasNext()){
            LatLng latLng = objectIterator.next();
            //System.out.println("Latitude : " + latLng.latitude + " Longitude :  " + latLng.longitude);
        }

    }

    @Override
    public void onRoutingSuccess(List<Route> route, int shortestRouteIndex)
    {
        progressDialog.dismiss();
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        map.moveCamera(center);
        map.animateCamera(zoom);

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        // \\TODO: Change 1 to route.size to get all direction options available from directions api.
        for (int i = 0; i <1; i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(ContextCompat.getColor(this,COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            routingPts = route.get(i).getPoints();
            printRoutingPts(routingPts);
            polyOptions.addAll(routingPts);
            Polyline polyline = map.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance : "+ route.get(i).getDistanceText() + " " +
                    "duration : "+ route.get(i).getDurationText(),Toast.LENGTH_LONG).show();
        }

        Log.d(TAG,"Number of waypoints = "+waypoints.size());
    }

    @Override
    public void onRoutingCancelled() {
        Log.i(TAG, "Routing was cancelled.");
    }
}
