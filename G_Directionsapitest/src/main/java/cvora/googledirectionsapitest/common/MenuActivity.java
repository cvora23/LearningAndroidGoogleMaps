package cvora.googledirectionsapitest.common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import cvora.googledirectionsapitest.R;

public class MenuActivity extends AppCompatActivity {


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
            startActivity(new Intent("cvora.googledirectionsapitest.common.MyPreferenceActivity"));
            return true;
        }else if(id == R.id.route1_simulation){
        }else if(id == R.id.route2_simulation){
        }else if(id == R.id.route3_simulation){
        }

        return super.onOptionsItemSelected(item);
    }

}
