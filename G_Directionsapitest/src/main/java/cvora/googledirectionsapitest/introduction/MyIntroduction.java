package cvora.googledirectionsapitest.introduction;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import cvora.googledirectionsapitest.R;

public class MyIntroduction extends AppIntro {

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(new Login_Register_Intro_Frag());
        addSlide(new Search_Nearby_Drivers_Intro_Frag());
        addSlide(new Select_Driver_Intro_Frag());
        addSlide(new Start_Service_Intro_Frag());

//        addSlide(AppIntroFragment.newInstance("Login/Register", "Login using your credentials.", R.drawable.android_login_and_registration, R.color.colorAccent));
//        addSlide(AppIntroFragment.newInstance("Search Nearby Drivers", "Find drivers fitting your criteria such as school timing, location.", R.drawable.search_nearby_drivers, R.color.colorAccent));
//        addSlide(AppIntroFragment.newInstance("Select Driver for service", "Select best, affordable and safe driver for your kid.", R.drawable.select_driver, R.color.colorAccent));
//        addSlide(AppIntroFragment.newInstance("Start your service", "Enjoy the service. Get real-time location updates, pick-up/drop-off notifications", R.drawable.start_service, R.color.colorAccent));

        // OPTIONAL METHODS

        // Override bar/separator color
        int color = Color.parseColor("#3F51B5");
        setBarColor(color);
        color = Color.parseColor("#2196F3");
        setSeparatorColor(color);

        // SHOW or HIDE the statusbar
        showStatusBar(true);

        // Edit the color of the nav bar on Lollipop+ devices
        //setNavBarColor(R.color.colorAccent);

        // Hide Skip/Done button
        setSkipText("Skip Intro");
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest
        // setVibrate(true);
        // setVibrateIntensity(30);

        // Animations -- use only one of the below. Using both could cause errors.
        setFadeAnimation(); // OR
        //        setZoomAnimation(); // OR
        //        setFlowAnimation(); // OR
        //        setSlideOverAnimation(); // OR
        //        setDepthAnimation(); // OR
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("skipIntroPref",true);
        editor.apply();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

}
