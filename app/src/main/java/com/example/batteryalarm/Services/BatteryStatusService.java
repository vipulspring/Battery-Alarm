package com.example.batteryalarm.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.batteryalarm.R;

public class BatteryStatusService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "BatteryStatusChannelVipul";
    public static final String SENDMSG = "developer_vipul_kumar_singh_send";

    private BroadcastReceiver batteryStatusReceiver;

    PowerManager.WakeLock wakeLock;

    Intent send = new Intent(SENDMSG);

    int percentage, status, current,value;
    boolean isCharging;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();




        // Create a broadcast receiver to listen for battery status changes
        batteryStatusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                //Toast.makeText(context, ""+ value + " " + "Vipul", Toast.LENGTH_SHORT).show();
                percentage = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0); // battery percentage


                status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;


                // Update the notification with the current charging status
                updateNotification(isCharging);
                sendDataToActivity();
            }
        };

        // Register the broadcast receiver to listen for battery status changes
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryStatusReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start the service in the foreground to keep it running even when the app is in the background

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();
        
        startForeground(NOTIFICATION_ID, buildNotification(false));



        return START_STICKY;
    }

    private void sendDataToActivity() {



        send.putExtra("bvPercentage", percentage);
        send.putExtra("bvStatus",status);
        send.putExtra("bvCurrent",current);
        send.putExtra("bvIsCharging",isCharging);


        sendBroadcast(send);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister the broadcast receiver when the service is destroyed
        if(batteryStatusReceiver !=null) {
            unregisterReceiver(batteryStatusReceiver);

        }
        if(wakeLock!=null) {
            wakeLock.release();
        }

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Battery Status",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void updateNotification(boolean isCharging) {
        Notification notification = buildNotification(isCharging);

        NotificationManager manager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            manager = getSystemService(NotificationManager.class);
        }
        manager.notify(NOTIFICATION_ID, notification);
    }

    private Notification buildNotification(boolean isCharging) {
        String title = isCharging ? "Charging" : "Not Charging";
        String content = isCharging ? "The device is currently charging." : "The device is not charging.";
        String value2 = ""+value;


        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setUsesChronometer(true)
                .setContentTitle(value2)
                .setContentText(content)
                .setSmallIcon(R.drawable.charging_animation_giff)
                .build();
    }
}
