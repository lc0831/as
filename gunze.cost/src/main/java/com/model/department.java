package com.model;

/**
 * Created by 刘畅 on 2018/3/25.
 */

public class department {
    //表名
    public static final String TABLE = "department";

    //表的各域名
    public static final String KEY_CODE = "depCode";
    public static final String KEY_NAME = "depName";

    //属性
    public String cDepCode;
    public String cDepName;

    public String getcDepName() {
        return cDepName;
    }

    public void setcDepName(String cDepName) {
        this.cDepName = cDepName;
    }


    public String getcDepCode() {
        return cDepCode;
    }

    public void setcDepCode(String cDepCode) {
        this.cDepCode = cDepCode;
    }
}
