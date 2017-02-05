package cvora.googledirectionsapitest.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFrag()).commit();
    }
}
