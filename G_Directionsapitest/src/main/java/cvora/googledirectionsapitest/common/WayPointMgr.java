package cvora.googledirectionsapitest.common;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 7/15/2016.
 */
public class WayPointMgr{

    private static final String TAG = "WayPointMgr";

    private List<WayPoint> wayPoints;
    private static WayPointMgr instance;

    private WayPointMgr(){
        wayPoints = new ArrayList<WayPoint>();
    }

    public static synchronized WayPointMgr getInstance(){
        if(instance == null){
            instance = new WayPointMgr();
        }
        return instance;
    }

    public void addWayPt(WayPoint wayPoint){
        wayPoints.add(wayPoint);
    }

    public boolean removeWayPt(WayPoint wayPoint){
        return wayPoints.remove(wayPoint);
    }

    public void rearrangeWayPtOrder(List<Integer>waypointOrder){
        List<WayPoint>newWayPoints = new ArrayList<WayPoint>();
        // insert the first element
        newWayPoints.add(wayPoints.get(0));
        // rearrange the elements based on new waypoint order - Travelling Salesman Algo
        for(int i = 0;i < waypointOrder.size();i++){
            newWayPoints.add(wayPoints.get(waypointOrder.get(i) + 1));
        }
        //insert the last element
        newWayPoints.add(wayPoints.get(wayPoints.size()-1));
        // clear the old list
        wayPoints.clear();
        // copy the new list
        wayPoints = newWayPoints;
    }

    public void setDuration(int index,int duration){
        if(index >=0 && index < wayPoints.size()-2){
            wayPoints.get(index+1).setDuration(duration);
        }
    }

    public void setDuration(LatLng latLng,int duration){
        for(int i=0;i<wayPoints.size();i++){
            if(wayPoints.get(i).getLatLng().equals(latLng)){
                wayPoints.get(i).setDuration(duration);
            }
        }
    }

    public void setCrossed(LatLng latLng){
        LatLng newLatLng = new LatLng(Util.trimDouble2Places(latLng.latitude),Util.trimDouble2Places(latLng.longitude));
        for(int i=0;i<wayPoints.size();i++){
            LatLng wayPtLatLng = new LatLng(Util.trimDouble2Places(wayPoints.get(i).getLatLng().latitude),Util.trimDouble2Places(wayPoints.get(i).getLatLng().longitude));
            if(!wayPoints.get(i).isCrossed()){
                if(wayPtLatLng.equals(newLatLng)){
                    Log.d(TAG,"Crossed the waypoint");
                    wayPoints.get(i).setCrossed(true);
                }
            }
        }
    }

    public boolean isCrossed(LatLng latLng){
        for(int i=0;i<wayPoints.size();i++){
            if(wayPoints.get(i).getLatLng().equals(latLng)){
                return wayPoints.get(i).isCrossed();
            }
        }
        return false;
    }

    public void clear(){
        wayPoints.clear();
    }

    public String getEta(LatLng position){

        //\TODO: Need Mathematical calculations for duration text to calculate exact ETA

        int eta = 0;
        for(int i=0;i<wayPoints.size();i++){
            WayPoint wayPoint = wayPoints.get(i);
            LatLng latLng = wayPoint.getLatLng();
            if( ! wayPoint.isCrossed()){
                eta += wayPoint.getDuration();
            }
            if(latLng.equals(position)){
                break;
            }
        }
        return Util.convertToTime(eta);
    }

}
