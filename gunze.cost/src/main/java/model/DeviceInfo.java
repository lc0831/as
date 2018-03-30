package model;

/**
 * Created by 刘畅 on 2018/3/24.
 */

public class DeviceInfo {
    private String devID;
    private String devname;

    public DeviceInfo() {
    }

    public DeviceInfo(String devID, String devname) {
        this.devID = devID;
        this.devname = devname;
    }

    public String getDevID() {
        return devID;
    }

    public void setDevID(String devID) {
        this.devID = devID;
    }

    public String getDevname() {
        return devname;
    }

    public void setDevname(String devname) {
        this.devname = devname;
    }
}
