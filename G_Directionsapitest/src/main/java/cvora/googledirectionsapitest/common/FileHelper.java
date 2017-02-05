package cvora.googledirectionsapitest.common;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Created by Admin on 7/13/2016.
 */
public class FileHelper {

    private static Context mContext;
    private static String mFileName = "test.txt";
    private static int fileMode = Context.MODE_PRIVATE;
    private static FileOutputStream fos = null;
    private final static String TAG = "FileHelper";
    public static void setApplicationContext(Context context){
        mContext = context;
    }

    public static Context getContext(){
        return mContext;
    }

    public static void setFileMode(int fileMode) {
        fileMode = fileMode;
    }

    public static int getFileMode() {
        return fileMode;
    }

    public static void setFileName(String fileName) {
        mFileName = fileName;
    }

    public static String getFileName() {
        return mFileName;
    }

    public static FileOutputStream getFileOpStream(){
        if(fos == null){
            try {
                fos = mContext.openFileOutput(mFileName,fileMode);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return fos;
    }


    public static void setWayPoints(String locDatFName,List<LatLng> waypoints){

        InputStream inputStream = null;
        try{
            inputStream = mContext.getAssets().open(locDatFName);
            CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));
            List content = csvReader.readAll();
            Iterator objectIterator = content.iterator();
            String[] row = null;
            while (objectIterator.hasNext()){
                // Create connection to send GCM Message request.
                row = (String[])objectIterator.next();
                System.out.println("Latitude : " + row[6] + " Longitude :  " + row[7]);
                LatLng location = new LatLng(Double.parseDouble(row[6]),Double.parseDouble(row[7]));
                waypoints.add(location);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
