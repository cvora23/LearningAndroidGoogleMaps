package cvora.googledirectionsapitest.common;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Admin on 7/15/2016.
 */
public class WayPoint {

    private LatLng latLng;
    private int duration;
    private int wayptOrderNo;
    private boolean crossed;

    private static final String UNDEFINED = "undefined";

    public WayPoint(){
        latLng = null;
        duration = -1;
        wayptOrderNo = -1;
        crossed = false;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWayptOrderNo() {
        return wayptOrderNo;
    }

    public void setWayptOrderNo(int wayptOrderNo) {
        this.wayptOrderNo = wayptOrderNo;
    }

    public boolean isCrossed() {
        return crossed;
    }

    public void setCrossed(boolean crossed) {
        this.crossed = crossed;
    }

}
