package cvora.googledirectionsapitest.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import cvora.googledirectionsapitest.R;
import cvora.googledirectionsapitest.common.MenuActivity;

public class DriverList extends MenuActivity {

    static final int START_DRIVER_PROFILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Driver List");
        setSupportActionBar(toolbar);

        ImageView driver1,driver2,driver3;
        driver1 = (ImageView)findViewById(R.id.driver1);
        driver2 = (ImageView)findViewById(R.id.driver2);
        driver3 = (ImageView)findViewById(R.id.driver3);

        View.OnClickListener DriverProfileClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Start DriverProfile Activity
                startActivityForResult(new Intent("cvora.googledirectionsapitest.profile.DriverProfile"),START_DRIVER_PROFILE);
            }
        };

        driver1.setOnClickListener(DriverProfileClickListener);
        driver2.setOnClickListener(DriverProfileClickListener);
        driver3.setOnClickListener(DriverProfileClickListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == START_DRIVER_PROFILE){
            if(resultCode == RESULT_OK){
            }
        }
    }

}
