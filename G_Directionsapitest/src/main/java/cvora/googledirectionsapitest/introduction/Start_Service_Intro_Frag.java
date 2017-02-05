package cvora.googledirectionsapitest.introduction;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cvora.googledirectionsapitest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Start_Service_Intro_Frag extends Fragment {


    public Start_Service_Intro_Frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start__service__intro_, container, false);
    }

}
