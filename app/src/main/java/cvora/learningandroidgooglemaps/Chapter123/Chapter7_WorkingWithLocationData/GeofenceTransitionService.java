package cvora.learningandroidgooglemaps.Chapter123.Chapter7_WorkingWithLocationData;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class GeofenceTransitionService extends IntentService {

    public GeofenceTransitionService() {
        super("GEO");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {

            Log.e("GEO","Error"+ geofencingEvent.getErrorCode());
        }
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ) {
            Log.d("Update", "Entered Geofence");
        }else if(
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            Log.d("Update", "Exited Geofence");

        }


    }
}
