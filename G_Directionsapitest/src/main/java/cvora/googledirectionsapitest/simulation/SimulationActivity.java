package cvora.googledirectionsapitest.simulation;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import cvora.googledirectionsapitest.R;
import cvora.googledirectionsapitest.common.FileHelper;
import cvora.googledirectionsapitest.common.MenuActivity;

public class SimulationActivity extends MenuActivity {

    private static final String TAG = "SimulationActivity";
    private final static int START_TABBED_SIMULATION = 1;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String title = "Navigation Drawer";
    private String[] items;
    private int selectedPosition;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);

        FileHelper.setApplicationContext(this);

        items = getResources().getStringArray(R.array.menus);
        // Getting reference to DrawerLayout
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.drawer_list);

        // Creating Arrayadapter to add items to drawer list.
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,R.layout.drawer_list_item,items);
        drawerList.setAdapter(adapter);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        // Enabling Home Button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting item click listener for listview drawerlist
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;

                selectDrawerItem(selectedPosition);

                // closing drawer
                drawerLayout.closeDrawer(drawerList);
            }
        });

        // setting default fragment
        selectedPosition = 0;

    }

    private void selectDrawerItem(int position){

        Log.d(TAG,"Drawer Item Selected : "+items[position]);

        switch (position){

            case 1:{
                startActivityForResult(new Intent("cvora.googledirectionsapitest.simulation.TabbedSimulationActivity"),START_TABBED_SIMULATION);
            }
            break;
            default:{
                Log.e(TAG,"Error in item selection");
            }
            break;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == START_TABBED_SIMULATION){
            if(resultCode == RESULT_OK){
            }
        }
    }
}
