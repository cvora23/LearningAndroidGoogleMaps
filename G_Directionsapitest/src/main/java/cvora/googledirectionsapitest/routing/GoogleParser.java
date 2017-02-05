package cvora.googledirectionsapitest.routing;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cvora.googledirectionsapitest.common.FileHelper;
import cvora.googledirectionsapitest.common.WayPointMgr;
import cvora.googledirectionsapitest.directions.MainActivity;

public class GoogleParser extends XMLParser implements Parser {

    private static final String TAG = "GoogleParser";

    private static final String ROUTES = "routes";
    private static final String VALUE = "value";
    private static final String TEXT = "text";
    private static final String DISTANCE = "distance";
    private static final String DURATION = "duration";
    private static final String STEPS = "steps";
    private static final String LEGS = "legs";
    private static final String START_ADDR = "start_address";
    private static final String END_ADDR = "end_address";
    private static final String WARNINGS = "warnings";
    private static final String COPYRIGHTS = "copyrights";
    private static final String START_LOC = "start_location";
    private static final String END_LOC = "end_location";
    private static final String LAT = "lat";
    private static final String LNG = "lng";
    private static final String WAYPOINT_ORDER = "waypoint_order";

    /**
     * Distance covered. *
     */
    private int distance;

    /* Status code returned when the request succeeded */
    private static final String OK = "OK";

    public GoogleParser(String feedUrl) {
        super(feedUrl);
    }

    /**
     * Parses a url pointing to a Google JSON object to a Route object.
     *
     * @return a Route object based on the JSON object by Haseem Saheed
     */

    public final List<Route> parse() throws RouteException {
        List<Route> routes = new ArrayList<>();

        // turn the stream into a string
        final String result = convertStreamToString(this.getInputStream());

        FileHelper.setFileName("GDirectionResultFile.json");

        try {
            FileHelper.getFileOpStream().write(result.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (result == null) {
            throw new RouteException("Result is null");
        }

        try {
            //Transform the string into a json object
            final JSONObject json = new JSONObject(result);
            //Get the route object

            if(!json.getString("status").equals(OK)){
                throw new RouteException(json);
            }

            JSONArray jsonRoutes = json.getJSONArray(ROUTES);
            JSONArray jsonLegs = null;
            for (int i = 0; i < jsonRoutes.length(); i++) {
                Route route = new Route();
                //Create an empty segment
                Segment segment = new Segment();

                JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
                //Get the bounds - northeast and southwest
                final JSONObject jsonBounds = jsonRoute.getJSONObject("bounds");
                final JSONObject jsonNortheast = jsonBounds.getJSONObject("northeast");
                final JSONObject jsonSouthwest = jsonBounds.getJSONObject("southwest");

                route.setLatLgnBounds(new LatLng(jsonNortheast.getDouble(LAT), jsonNortheast.getDouble(LNG)),
                        new LatLng(jsonSouthwest.getDouble(LAT), jsonSouthwest.getDouble(LNG)));
                //Get google's copyright notice (tos requirement)
                route.setCopyright(jsonRoute.getString(COPYRIGHTS));
                //Get any warnings provided (tos requirement)
                if (!jsonRoute.getJSONArray(WARNINGS).isNull(0)) {
                    route.setWarning(jsonRoute.getJSONArray(WARNINGS).getString(0));
                }
                // Get any waypoint_order provided
                List<Integer>waypoint_order = new ArrayList<Integer>();
                if(!jsonRoute.getJSONArray(WAYPOINT_ORDER).isNull(0)){
                    JSONArray jsonWayPtArray = jsonRoute.getJSONArray(WAYPOINT_ORDER);
                    for(int j=0;j<jsonWayPtArray.length();j++){
                        Log.d(TAG,"WayPoint Order No :" + jsonWayPtArray.getInt(j));
                        waypoint_order.add(jsonWayPtArray.getInt(j));
                    }
                }
                // optimize the waypoints based on result returned
                WayPointMgr.getInstance().rearrangeWayPtOrder(waypoint_order);

                //TODO: Remove this comment - Get the leg, only one leg as we don't support waypoints
                jsonLegs = jsonRoute.getJSONArray(LEGS);

                JSONArray steps = null;

                for(int j =0;j<jsonLegs.length();j++){

                    JSONObject jsonLeg = jsonLegs.getJSONObject(j);

                    //Get the steps for this leg
                    steps = jsonLeg.getJSONArray(STEPS);
                    //Number of steps for use in for loop
                    int numSteps = steps.length();

                    RouteLeg routeLeg = new RouteLeg();

                    //Set the name of this route using the start & end addresses
                    routeLeg.setName(jsonLeg.getString(START_ADDR) + " to " + jsonLeg.getString(END_ADDR));

                    //Get distance and time estimation
                    routeLeg.setDurationText(jsonLeg.getJSONObject(DURATION).getString(TEXT));
                    routeLeg.setDurationValue(jsonLeg.getJSONObject(DURATION).getInt(VALUE));
                    routeLeg.setDistanceText(jsonLeg.getJSONObject(DISTANCE).getString(TEXT));
                    routeLeg.setDistanceValue(jsonLeg.getJSONObject(DISTANCE).getInt(VALUE));
                    routeLeg.setStartAddressText(jsonLeg.getString(START_ADDR));
                    routeLeg.setEndAddressText(jsonLeg.getString(END_ADDR));

                    route.addRouteLeg(routeLeg);

//                    JSONObject jsonEndLocation;
//                    jsonEndLocation = jsonLeg.getJSONObject(END_LOC);
//                    Double endLat,endLng;
//                    endLat = jsonEndLocation.getDouble(LAT);
//                    endLng = jsonEndLocation.getDouble(LNG);

                    // update the way point - duration value
                    WayPointMgr.getInstance().setDuration(j,jsonLeg.getJSONObject(DURATION).getInt(VALUE));

                /* Loop through the steps, creating a segment for each one and
                 * decoding any polylines found as we go to add to the route object's
                 * map array. Using an explicit for loop because it is faster!
                 */
                    for (int y = 0; y < numSteps; y++) {
                        //Get the individual step
                        final JSONObject step = steps.getJSONObject(y);
                        //Get the start position for this step and set it on the segment
                        final JSONObject start = step.getJSONObject("start_location");
                        final LatLng position = new LatLng(start.getDouble("lat"),
                                start.getDouble("lng"));
                        segment.setPoint(position);
                        //Set the length of this segment in metres
                        final int length = step.getJSONObject(DISTANCE).getInt(VALUE);
                        distance += length;
                        segment.setLength(length);
                        segment.setDistance((double)distance / (double)1000);
                        //Strip html from google directions and set as turn instruction
                        segment.setInstruction(step.getString("html_instructions").replaceAll("<(.*?)*>", ""));

                        if(step.has("maneuver"))
                            segment.setManeuver(step.getString("maneuver"));

                        //Retrieve & decode this segment's polyline and add it to the route.
                        route.addPoints(decodePolyLine(step.getJSONObject("polyline").getString("points")));
                        //Push a copy of the segment to the route
                        route.addSegment(segment.copy());
                    }
                }

                JSONObject startJsonLeg = jsonLegs.getJSONObject(0);
                JSONObject endJsonLeg = jsonLegs.getJSONObject(jsonLegs.length()-1);

                //Set the name of this route using the start & end addresses
                route.setName(startJsonLeg.getString(START_ADDR) + " to " + endJsonLeg.getString(END_ADDR));
                // Set the start and end address for the current route
                route.setStartAddressText(startJsonLeg.getString(START_ADDR));
                route.setEndAddressText(endJsonLeg.getString(END_ADDR));
                // \TODO:Set duration and distance values
//                route.setDurationText(jsonLeg.getJSONObject(DURATION).getString(TEXT));
//                route.setDurationValue(jsonLeg.getJSONObject(DURATION).getInt(VALUE));
//                route.setDistanceText(jsonLeg.getJSONObject(DISTANCE).getString(TEXT));
//                route.setDistanceValue(jsonLeg.getJSONObject(DISTANCE).getInt(VALUE));
                // adding route to list of routes
                routes.add(route);
            }

        } catch (JSONException e) {
            throw new RouteException("JSONException. Msg: "+e.getMessage());
        }
        return routes;
    }

    /**
     * Convert an inputstream to a string.
     *
     * @param input inputstream to convert.
     * @return a String of the inputstream.
     */

    private static String convertStreamToString(final InputStream input) {
        if (input == null) return null;

        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        final StringBuilder sBuf = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sBuf.append(line);
            }
        } catch (IOException e) {
            Log.e("Routing Error", e.getMessage());
        } finally {
            try {
                input.close();
                reader.close();
            } catch (IOException e) {
                Log.e("Routing Error", e.getMessage());
            }
        }
        return sBuf.toString();
    }

    /**
     * Decode a polyline string into a list of GeoPoints.
     *
     * @param poly polyline encoded string to decode.
     * @return the list of GeoPoints represented by this polystring.
     */

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}
