package com.Receivers;

import android.content.Context;
import android.content.Intent;

/**
 * Created by 刘畅 on 2018/3/25.
 */

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    public String sss = "android.intent.ACTION_DECODE_DATA";
    public String aa = "";
    private Message message;
    @Override
    public void onReceive(Context context, Intent intent) {
        aa = intent.getStringExtra("barcode_string");
        //调用Message接口的方法
        message.getMsg(aa);
    }

    interface Message {
        public void getMsg(String str);
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
