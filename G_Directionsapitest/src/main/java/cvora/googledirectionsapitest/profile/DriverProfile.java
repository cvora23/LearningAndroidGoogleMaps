package cvora.googledirectionsapitest.profile;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import cvora.googledirectionsapitest.R;

public class DriverProfile extends AppCompatActivity {

    private final static int START_SIMULATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Replace with your own action",Snackbar.LENGTH_LONG).
                        setAction("Action",null).show();
                startSimulation();
            }
        });
    }

    private void startSimulation(){
        startActivityForResult(new Intent("cvora.googledirectionsapitest.simulation.SimulationActivity"),START_SIMULATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == START_SIMULATION){
            if(resultCode == RESULT_OK){
            }
        }
    }
}
