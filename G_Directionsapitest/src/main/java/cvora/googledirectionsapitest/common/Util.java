package cvora.googledirectionsapitest.common;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Created by Admin on 7/17/2016.
 */
public class Util {

    private static final String TAG= "Util";

    public static double trimDouble2Places(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        String valueStr = df.format(value);
        double newVal = Double.valueOf(valueStr);
        return newVal;
    }

    public static int sampleFutureTime(){

        String str = "Jul 18 2016 18:15 CDT";
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm zzz");
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long epoch = date.getTime();
        Log.d(TAG,"Epoch Time Current : "+System.currentTimeMillis()/1000);
        Log.d(TAG,"Epoch Time in Seconds at 18:15 pm today : "+ epoch/1000); // 1055545912454
        Log.d(TAG,"Epoch Time in Seconds at 18:15 pm today : "+ (int)(epoch/1000)); // 1055545912454

        return (int)(epoch/1000);
    }

    public static String getAddress(Activity baseActivity,LatLng latLng){

        String addressText = null;
        if(Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(baseActivity, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude, 1);
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                addressText = String.format(
                        "%s, %s, %s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality(),
                        address.getCountryName(),
                        address.getPostalCode());

            }else{
                Log.e(TAG,"Couldn't find address for LatLng "+latLng);
                addressText = "UNDEFINED";
            }
        } else {
            Toast.makeText(baseActivity, "Geocoder could not implemented", Toast.LENGTH_LONG).show();
        }
        return addressText;

    }

    public static int getRandom(int max){
        return new Random().nextInt(max);
    }

    public static String convertToTime(int seconds){
        String time = "";
        int hours = seconds/3600;
        if(hours >0){
            time += hours + " hours ";
        }
        int minutes = (seconds/60) %60;
        if(minutes > 0){
            time += minutes + " minutes ";
        }
        int sec = seconds%60;
        if(sec > 0){
            time += sec + " seconds ";
        }

        return time;
    }

}
