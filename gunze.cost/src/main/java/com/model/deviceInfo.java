package com.model;

import net.sourceforge.jtds.jdbc.DateTime;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by 刘畅 on 2018/3/25.
 */

public class deviceInfo extends DataSupport {
    private Integer id;
    private String devID;
    private String devName;
    private String depCode;
    private String depName;
    private Date recordDate;
    //表名
    public static final String TABLE = "devices";

    //表的各域名
    public static final String KEY_ID = "id";
    public static final String KEY_CODE = "devID";
    public static final String KEY_NAME = "devname";


    public String getDevID() {
        return devID;
    }

    public void setDevID(String devID) {
        this.devID = devID;
    }

    public String getDevname() {
        return devName;
    }

    public void setDevname(String devname) {
        this.devName = devname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getDepcode() {
        return depCode;
    }

    public void setDepcode(String depcode) {
        this.depCode = depcode;
    }
}
