package cvora.firebasetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSubscribeToNewsClick(View view){
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d(TAG,"Subscribed to news topic");
        // [END subscribe_topics]
    }

    public void onLogTokenClick(View view){
        Log.d(TAG,"Instance ID Token: "+ FirebaseInstanceId.getInstance().getToken());
    }
}
