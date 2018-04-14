package com.model;

/**
 * Created by 刘畅 on 2018/3/25.
 */

public class deviceInfo {
    private Integer id;
    private String devID;
    private String devname;
    //表名
    public static final String TABLE = "devices";

    //表的各域名
    public static final String KEY_ID="id";
    public static final String KEY_CODE = "devID";
    public static final String KEY_NAME = "devname";





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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
