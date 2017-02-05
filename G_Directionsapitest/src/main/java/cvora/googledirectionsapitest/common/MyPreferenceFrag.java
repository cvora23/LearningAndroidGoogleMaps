package cvora.googledirectionsapitest.common;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cvora.googledirectionsapitest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPreferenceFrag extends PreferenceFragment {


    public MyPreferenceFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load the prefences from an XML file
        addPreferencesFromResource(R.xml.preferences);
    }
}
