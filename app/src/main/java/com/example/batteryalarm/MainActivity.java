package com.example.batteryalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.batteryalarm.Broadcasting.PowerConnectionReceiver;
import com.example.batteryalarm.Services.BatteryStatusService;
import com.example.batteryalarm.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding activityMainBinding;

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 10011;
    PowerConnectionReceiver powerConnectionReceiver;

    private BroadcastReceiver batteryReceiverr;



    int percentage, status, current;
    boolean isCharging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());


        View view = activityMainBinding.getRoot();


        setContentView(view);


        methodInvoker();


        requestNotificationPermission();



        /*Intent serviceIntent = new Intent(this, BatteryStatusService.class);
        startService(serviceIntent);*/

        // powerConnectionReceiver = new PowerConnectionReceiver();

        //registerReceiver(powerConnectionReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        //https://github.com/kyleduo/SwitchButton
        //https://github.com/warkiz/IndicatorSeekBar


        batteryReceiverr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                percentage = intent.getIntExtra("bvPercentage",0);
                status = intent.getIntExtra("bvStatus",0);
                current = intent.getIntExtra("bvCurrent", 0);
                isCharging = intent.getBooleanExtra("bvIsCharging", false);

                Log.d("TAG", "onReceiveOnR: " + percentage);

                activityMainBinding.include.batteryProgressBar.setCurrentValues(percentage);


            }
        };

    }

    private void methodInvoker() {
        double battery1 = getBatteryCapacity(this);
    }

    public double getBatteryCapacity(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return batteryCapacity;

    }

    @Override
    protected void onStart() {
        // registerReceiver(powerConnectionReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
        registerReceiver(this.batteryReceiverr, new IntentFilter(BatteryStatusService.SENDMSG));
    }


    //Notification Request Code

    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
           // return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {

        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // Checking the request code of our request
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {

            // If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Displaying a toast
                Intent serviceIntent = new Intent(this, BatteryStatusService.class);
                startService(serviceIntent);
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                // Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
   /* private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a notification channel (required for Android 8.0 and above)
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            NotificationChannel channel = new NotificationChannel(
                    "DefaultChannelId",
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }
        Toast.makeText(this, ""+isNotificationPermissionGranted(), Toast.LENGTH_SHORT).show();
        // Check if the notification permission is granted
        if (!isNotificationPermissionGranted()) {
            Toast.makeText(this, "Called", Toast.LENGTH_SHORT).show();
            // Request the notification permission
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivityForResult(intent, NOTIFICATION_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean isNotificationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Toast.makeText(this, "GOogle", Toast.LENGTH_SHORT).show();
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            NotificationChannel channel = notificationManager.getNotificationChannel("DefaultChannelId");
            return channel != null && channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
        } else {
            // On earlier versions, the permission is always granted by default
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            // Check if the notification permission has been granted after the user's action
            if (isNotificationPermissionGranted()) {
                // Notification permission granted, you can proceed with your logic
                Intent serviceIntent = new Intent(this, BatteryStatusService.class);
                startService(serviceIntent);
            } else {
                // Notification permission not granted, handle the scenario accordingly
            }
        }
    }*/
}