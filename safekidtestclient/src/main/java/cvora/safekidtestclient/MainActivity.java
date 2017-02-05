package cvora.safekidtestclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends FragmentActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private static int LOGIN_REQUEST_CODE = 1;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInformationTextView = (TextView) findViewById(R.id.informationTextView);
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);

        createBroadcastReceiver();
        registerReceiver();

        startIntroVideoActivity();
    }

    private void createBroadcastReceiver(){

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String actionName = intent.getAction();
                switch (actionName){
                    case QuickstartPreferences.REGISTRATION_COMPLETE:{
                        mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                        SharedPreferences sharedPreferences =
                                PreferenceManager.getDefaultSharedPreferences(context);
                        boolean sentToken = sharedPreferences
                                .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                        if (sentToken) {
                            mInformationTextView.setText(getString(R.string.gcm_send_message));
                        } else {
                            mInformationTextView.setText(getString(R.string.token_error_message));
                        }
                    }
                    break;
                }
            }
        };
    }

    private void startIntroVideoActivity(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean result = sharedPreferences.getBoolean(getResources().getString(R.string.Skip_Intro),false);
        if(!result){
            startActivity(new Intent("cvora.safekidtestclient.IntroVideoActivity"));
            startActivityForResult(new Intent("cvora.safekidtestclient.LoginActivity"),LOGIN_REQUEST_CODE);
        }else{
            startActivityForResult(new Intent("cvora.safekidtestclient.LoginActivity"),LOGIN_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                // Once Login is completed start the GCM registration
                if (checkPlayServices()) {
                    // Start IntentService to register this application with GCM.
                    Intent mIntent = new Intent(getBaseContext(), RegistrationIntentService.class);
                    startService(mIntent);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(QuickstartPreferences.LOGIN_COMPLETE);
            intentFilter.addAction(QuickstartPreferences.REGISTRATION_COMPLETE);
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,intentFilter);
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
