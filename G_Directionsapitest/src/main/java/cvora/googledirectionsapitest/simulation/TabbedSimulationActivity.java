package cvora.googledirectionsapitest.simulation;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import cvora.googledirectionsapitest.R;

public class TabbedSimulationActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static final String TAG = "TabdSimonActivity";
    private static final String SIM1_FILE_NAME =  "Route1.csv";
    public static String locDatFName = SIM1_FILE_NAME;

    // This is our tablayout
    private TabLayout tabLayout;

    // This is our viewPager
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_simulation);

        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Initializing the tablayout
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Simulation 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Simulation 2"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager)findViewById(R.id.pager);

        // Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(),tabLayout.getTabCount());

        // Adding adapter to pager
        viewPager.setAdapter(adapter);

        // Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simulation1_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            return true;
        }else if(id == R.id.simulation1_route){
            locDatFName = SIM1_FILE_NAME;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d(TAG,"TabbedSimulationActivity::onTabSelected - pos = "+tab.getPosition());
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
