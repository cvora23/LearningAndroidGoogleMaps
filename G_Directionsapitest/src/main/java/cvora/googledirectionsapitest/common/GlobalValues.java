package cvora.googledirectionsapitest.common;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Admin on 6/1/2016.
 */
public class GlobalValues {
    public static final String DEVELOPER_KEY = "AIzaSyArpYbEMI7l6jcthRjUCmkEpHctHvyAEgw";
    public static final String YOUTUBE_VIDEO_CODE = "InY3pQ3yJ2U";

//    1/1/2006 0:00,2217 16TH AVE,4,957,459 PC  BURGLARY VEHICLE,2299,38.537173,-121.4875774
//    1/1/2006 0:00,3547 P ST,3,853,484 PC   PETTY THEFT/INSIDE,2308,38.56433456,-121.4618826

    public static ArrayList<LatLng> places = new ArrayList<LatLng>();
    static{
        places.add(new LatLng(38.537173,-121.4875774));
        places.add(new LatLng(38.56433456,-121.4618826));
    }



}
