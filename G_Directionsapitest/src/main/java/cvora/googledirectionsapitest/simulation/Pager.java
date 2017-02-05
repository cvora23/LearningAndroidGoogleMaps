package cvora.googledirectionsapitest.simulation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by Admin on 7/20/2016.
 */
public class Pager extends FragmentStatePagerAdapter{

    private static final String TAG = "Pager";
    int tabCount;

    public Pager(FragmentManager fm,int tabCount){
        super(fm);
        this.tabCount = tabCount;
    }

    public Fragment getItem(int position) {

        Log.d(TAG,"Pager::getItem - Position = "+position);

        switch (position) {
            case 0:
                Simulation1Fragment simulation1Fragment = new Simulation1Fragment();
                return simulation1Fragment;
            case 1:
                Simulation2Fragment simulation2Fragment = new Simulation2Fragment();
                return simulation2Fragment;
            case 2:
                Simulation3Fragment simulation3Fragment = new Simulation3Fragment();
                return simulation3Fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
