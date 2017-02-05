package cvora.safekidtestserver;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import au.com.bytecode.opencsv.CSVReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "MainActivity";
    private static final String stringUrl = "https://android.googleapis.com/gcm/send";
    public static final String API_KEY = "AIzaSyBz_h5IdaFuw9wUJvJnx--UXxevRglskqI";
    private static final String ROUTE1_FILE_NAME =  "Route1.csv";
    private static final String ROUTE2_FILE_NAME =  "Route2.csv";
    private static final String ROUTE3_FILE_NAME =  "Route3.csv";

    private String locDatFName = ROUTE1_FILE_NAME;
    private HttpURLConnection conn;
    private SendLocationDataTask sendLocationDataTask;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
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

    public void onSendLocationDataClick(View view){
        sendLocationDataTask = new SendLocationDataTask();
        sendLocationDataTask.execute();
    }

    public void onStopSendLocationDataClick(View view){
        sendLocationDataTask.cancel(true);
    }

    private class SendLocationDataTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... params) {
            return readSendLocationData(locDatFName,stringUrl);
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
        }
    }

    private String readSendLocationData(String locDatFName,String myUrl){
        InputStream inputStream = null;
        try{
            inputStream = getAssets().open(locDatFName);
            CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));
            List content = csvReader.readAll();
            Iterator<Object> objectIterator = content.iterator();
            objectIterator.next();
            String[] row = null;
            while (objectIterator.hasNext()){
                // Create connection to send GCM Message request.
                URL url = new URL(myUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", "key=" + API_KEY);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                row = (String[])objectIterator.next();
                System.out.println("Latitude : " + row[6] + " Longitude :  " + row[7]);
                Location location = new Location("location");
                location.setLatitude(Double.parseDouble(row[6]));
                location.setLongitude(Double.parseDouble(row[7]));
                sendLocationData(location);
                Thread.sleep(2000,0);
            }
            return "Success";
        }catch (IOException e){
            e.printStackTrace();
            return "Error while reading CSV file";
        }catch (InterruptedException e){
            e.printStackTrace();
            return "Error Thread Sleep Interrupted";
        }
    }

    private String sendLocationData(Location latLng) throws IOException{

        Log.d(DEBUG_TAG,"sendLocationData");

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try{
            JSONObject jGcmData = new JSONObject();
            jGcmData.put("to", "/topics/global");
            JSONObject jData = new JSONObject();
            jData.put("latitude",Double.toString(latLng.getLatitude()));
            jData.put("longitude",Double.toString(latLng.getLongitude()));
            jGcmData.put("data", jData);

            // Send GCM message content.
            outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            // Read GCM response.
            inputStream = conn.getInputStream();
            return inputStream.toString();
        }catch (IOException e){
            Log.e(DEBUG_TAG,"Unable to send GCM message");
            System.out.println("Please ensure that API_KEY has been replaced by the server " +
                    "API key, and that the device's registration token is correct (if specified).");
            e.printStackTrace();
            return "Unable to send GCM message";
        } catch (JSONException e) {
            Log.e(DEBUG_TAG,"Invalid JSON Data");
            e.printStackTrace();
            return "Invalid JSON Data";
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
        }
    }
}
