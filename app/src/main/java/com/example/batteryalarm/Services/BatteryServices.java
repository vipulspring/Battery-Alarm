package com.example.batteryalarm.Services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.batteryalarm.Broadcasting.PowerConnectionReceiver;

public class BatteryServices extends Service {

    PowerConnectionReceiver powerConnectionReceiver;
    public static final String SENDMSG = "developer_vipul_kumar_singh_send";
    public static final String RECEIVEMSG = "developer_vipul_kumar_singh_receive";

    Intent send = new Intent(SENDMSG);
    Intent receive = new Intent(RECEIVEMSG);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        powerConnectionReceiver = new PowerConnectionReceiver();

        registerReceiver(powerConnectionReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(powerConnectionReceiver!=null){
            unregisterReceiver(powerConnectionReceiver);
        }
    }
}
