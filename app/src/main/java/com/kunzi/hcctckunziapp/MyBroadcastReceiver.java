package com.kunzi.hcctckunziapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by 刘畅 on 2018/3/23.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    public String sss="android.intent.ACTION_DECODE_DATA";
public String aa ="";
    @Override
    public void onReceive(Context context, Intent intent) {
         aa=intent.getStringExtra("barcode_string");
    }
}
