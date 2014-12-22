package com.harold.knumarket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Gan on 2014-12-21.
 * 부팅시 back alarm service 동작 호출
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, AlarmService.class);
            context.startService(i);
        }
    }
}