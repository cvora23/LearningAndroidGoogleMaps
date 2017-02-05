package cvora.googledirectionsapitest.common;

/**
 * Created by Admin on 5/24/2016.
 */
public class LocationData {

    private String dateTime;
    private String address;
    private String district;
    private String crimedescr;
    private String ncicCode;
    private String latitude;
    private String longitude;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCrimedescr() {
        return crimedescr;
    }

    public void setCrimedescr(String crimedescr) {
        this.crimedescr = crimedescr;
    }

    public String getNcicCode() {
        return ncicCode;
    }

    public void setNcicCode(String ncicCode) {
        this.ncicCode = ncicCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
