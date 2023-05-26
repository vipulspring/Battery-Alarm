package com.example.batteryalarm.Broadcasting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import com.example.batteryalarm.MainActivity;

public class PowerConnectionReceiver extends BroadcastReceiver {
    int others = 0;
    private static final String TAG = "PowerConnectionReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {




         int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);


            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

           /* int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
*/
        if(!isCharging){
            Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
        }

            Log.i(TAG, "onReceive: " + "Is Charging: " + status /*+ " usbCharge: " + usbCharge + " acCharge: " + acCharge*/);

            //Toast.makeText(context, "Is Charging: " + status + " " + isCharging /*+ " usbCharge: " + usbCharge + " acCharge: " + acCharge*/, Toast.LENGTH_SHORT).show();
        }

   }

