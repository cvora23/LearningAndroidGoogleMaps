package cvora.googledirectionsapitest.start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cvora.googledirectionsapitest.R;
import cvora.googledirectionsapitest.common.FileHelper;
import cvora.googledirectionsapitest.common.MenuActivity;
import cvora.googledirectionsapitest.common.MyPreferenceFrag;

public class StartActivity extends MenuActivity {

    private static final String TAG = "MainActivity";
    private static int INTRO_VIDEO_REQUEST_CODE = 1;
    private static int LOGIN_REQUEST_CODE = 2;
    private static int FIND_NEARBY_DRIVER_REQUEST_CODE = 3;
    private static int INTRO_REQUEST_CODE = 4;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        checkForSkipIntro();
        //startIntroVideoActivity();
        FileHelper.setApplicationContext(this);
    }

    private void checkForSkipIntro(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean skipIntro = sharedPreferences.getBoolean("skipIntroPref",false);
        Log.d(TAG,"Skip Intro = "+skipIntro);
        if(!skipIntro){
            startActivityForResult(new Intent("cvora.googledirectionsapitest.introduction.MyIntroduction"),INTRO_REQUEST_CODE);
        }else {
            startActivityForResult(new Intent("cvora.googledirectionsapitest.login.LoginActivity"),LOGIN_REQUEST_CODE);
        }
    }

    private void startIntroVideoActivity(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean result = sharedPreferences.getBoolean(getResources().getString(R.string.Skip_Intro),false);
        if(!result){
            startActivityForResult(new Intent("cvora.googledirectionsapitest.login.IntroVideoActivity"),INTRO_VIDEO_REQUEST_CODE);
        }else{
            startActivityForResult(new Intent("cvora.googledirectionsapitest.login.LoginActivity"),LOGIN_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == INTRO_VIDEO_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                startActivityForResult(new Intent("cvora.googledirectionsapitest.login.LoginActivity"),LOGIN_REQUEST_CODE);
            }
        }else if(requestCode == INTRO_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                startActivityForResult(new Intent("cvora.googledirectionsapitest.login.LoginActivity"),LOGIN_REQUEST_CODE);
            }
        }else if(requestCode == LOGIN_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                startActivityForResult(new Intent("cvora.googledirectionsapitest.location_lookup.NearByDrivers"),FIND_NEARBY_DRIVER_REQUEST_CODE);
            }
        }else if(requestCode == FIND_NEARBY_DRIVER_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Log.d(TAG,"NearByDriver Activity Success");
            }
        }
    }

}
