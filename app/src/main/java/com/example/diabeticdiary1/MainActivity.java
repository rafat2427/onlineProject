package com.example.diabeticdiary1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Vibrator;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener {

    Button b_log,b_hist;
    private Sensor mySensor;
    private SensorManager SM;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SM=(SensorManager)getSystemService(SENSOR_SERVICE);

        mySensor=SM.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        ShakeDetector SD=new ShakeDetector(this);

        SD.start(SM);

        b_log=findViewById(R.id.log);
        b_hist=findViewById(R.id.history);

        b_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_data();
            }
        });
        b_hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert= new AlertDialog.Builder(MainActivity.this);

                alert.setTitle("View Your History");
                alert.setMessage("Which View do you want to show?");

                alert.setNegativeButton("ListView", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        view_list_data();
                    }
                });

                alert.setPositiveButton("GridView", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        view_grid_data();
                    }
                });

                alert.show();

            }
        });

    }

    public void view_list_data() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void view_grid_data() {
        Intent intent = new Intent(this, GridViewActivity.class);
        startActivity(intent);
    }

    public void new_data() {
        Intent intent = new Intent(this, input_activity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void hearShake() {
        // Vibrator needs permission. Add
        // <uses-permission android:name="android.permission.VIBRATE"/>
        // in the AndroidManifest
        vibrator.vibrate(1000);

        // Making a Phone Call.....
        // Requires Permission. Add
        // <uses-permission android:name="android.permission.CALL_PHONE"/>
        // in the AndroidManifest

        Intent intentCall= new Intent(Intent.ACTION_CALL);
        String emgNumber="12345";
        intentCall.setData(Uri.parse("tel:"+emgNumber));

        //Checks if Phone give permission to make a phone call
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE )!= PackageManager.PERMISSION_GRANTED) {
            //if permission is not granted, requests for permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            //After granting permission checks again and makes the call
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),"Calling the Emergency Number", Toast.LENGTH_LONG).show();
                startActivity(intentCall);
            }
        }
        else{
            //if permission is granted, then makes the call
            Toast.makeText(getApplicationContext(),"Calling the Emergency Number", Toast.LENGTH_LONG).show();
            startActivity(intentCall);
        }
    }
}
