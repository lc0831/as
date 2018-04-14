package com.model;

/**
 * Created by 刘畅 on 2018/3/25.
 */

public class deviceInfo {
    private String devID;
    private String devname;

    public deviceInfo() {
    }

    public deviceInfo(String devID, String devname) {
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
