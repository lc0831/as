package com.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class commonHelper {
    public static Date string2Date(String strDate) {

        //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //必须捕获异常
        try {
            return simpleDateFormat.parse(strDate);

        } catch (ParseException px) {
            px.printStackTrace();
            return null;
        }
    }

    public static String date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //获取String类型的时间
        return sdf.format(date);
    }
}
