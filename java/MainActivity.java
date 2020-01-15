package com.example.ergasia2;
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.lang.Float.parseFloat;

public class MainActivity extends AppCompatActivity implements LocationListener {

    final static int REQUESTCODE = 324;
    LocationManager locationManager;
    TextView currentSpeedTextView;
    float currentSpeed;
    String currentSpeedLimit;
    double currentLatitude, currentLongitude;
    OverspeedingDbHelper mDbHelper;
    boolean writeToDb = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        currentSpeedTextView = findViewById(R.id.speedTextView);

        mDbHelper = new OverspeedingDbHelper(getApplicationContext());
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this,"GPS is already on.",Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    this);
        } else
            Toast.makeText(this,"Permission needed before starting the app.", Toast.LENGTH_SHORT).show();
    }

    public void startButtonMethod(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUESTCODE);
        }else{
            Toast.makeText(this,"Permission already Granted!",Toast.LENGTH_SHORT).show();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
            this.onLocationChanged(null);
        }
    }

    @Override
    public void onLocationChanged(Location location) {



        if (location == null){
            currentSpeedTextView.setText("-.- km/h");
        }
        else{
            currentSpeed = location.getSpeed() * 1.85f; //Knots to kmh conversion.
            currentSpeedTextView.setText(currentSpeed + "km/h");


            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            //Cursor cursor = db.rawQuery("Select * from overspeedingLimit", new String[]{currentSpeedLimit});

            Cursor cursor = db.rawQuery("Select * from overspeedingLimit", new String[]{});

            while (cursor.moveToNext()) { currentSpeedLimit = cursor.getString(1);}


            if (currentSpeed > parseFloat(currentSpeedLimit) && writeToDb == true){ //The speedLimit is converted from String to float.
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                //Toast.makeText(this,"Please move slower! Current speed: "
                //        + String.valueOf(currentSpeed),Toast.LENGTH_SHORT).show();
                OverspeedingPoint overspeedingPoint = new OverspeedingPoint(String.valueOf(currentLatitude)
                        , String.valueOf(currentLongitude), String.valueOf(currentSpeed)
                        , String.valueOf(Calendar.getInstance().getTime()));
                addNewEntry(overspeedingPoint);
                ConstraintLayout rl = (ConstraintLayout)findViewById(R.id.appLayout);
                rl.setBackgroundColor(Color.RED);
                writeToDb = false;
            }

            else if(currentSpeed < parseFloat(currentSpeedLimit) && currentSpeed>0){
                ConstraintLayout rl = (ConstraintLayout)findViewById(R.id.appLayout);
                rl.setBackgroundColor(Color.WHITE);
                writeToDb = true;
            }
        }
    }

    public void addNewEntry(OverspeedingPoint overspeedingPoint){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_LATITUDE, overspeedingPoint.getoverspeedingPoint_latitude());
        values.put(OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_LONGITUDE, overspeedingPoint.getoverspeedingPoint_longitude());
        values.put(OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_SPEED, overspeedingPoint.getOverspeedingPoint_speed());
        values.put(OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_TIMESTAMP, overspeedingPoint.getOverspeedingPoint_timestamp());

        //Inserting Row
        db.insert(OverspeedingDbContract.OverspeedingEntry.TABLE_NAME, null, values);
        db.close();
    }


    public void overspeedingLogButton(View view){
        List<OverspeedingPoint> overspeedingList = getAllOverspeedingPoints();
        if (overspeedingList != null){
            StringBuffer buffer = new StringBuffer();
            for (OverspeedingPoint overspeedingPoint : overspeedingList) {
                buffer.append(overspeedingPoint.getOverspeedingPoint_id()+"\n");
                buffer.append(overspeedingPoint.getoverspeedingPoint_latitude()+"\n");
                buffer.append(overspeedingPoint.getoverspeedingPoint_longitude()+"\n");
                buffer.append(overspeedingPoint.getOverspeedingPoint_speed()+"\n");
                buffer.append(overspeedingPoint.getOverspeedingPoint_timestamp()+"\n\n");
            }
            messageShow(buffer.toString());
        }
    }

    public void messageShow(String message){
        AlertDialog.Builder abuilder;
        abuilder = new AlertDialog.Builder(this);
        abuilder.setTitle("Overspeeding points");
        abuilder.setMessage(message);
        abuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = abuilder.create();
        dialog.show();
    }

    // Getting All Contacts
    public List<OverspeedingPoint> getAllOverspeedingPoints() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        List<OverspeedingPoint> overspeedingList = new ArrayList<OverspeedingPoint>();
        String[] projection = {
                OverspeedingDbContract.OverspeedingEntry._ID,
                OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_LATITUDE,
                OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_LONGITUDE,
                OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_SPEED,
                OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_TIMESTAMP
        };
        Cursor c = db.query(
                OverspeedingDbContract.OverspeedingEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                     // null columns means all
                null,                                     // null values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );
        while (c.moveToNext()) {
            OverspeedingPoint overspeedingPoint = new OverspeedingPoint(c.getInt(c.getColumnIndex(OverspeedingDbContract.OverspeedingEntry._ID)),
                    c.getString(c.getColumnIndex(OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_LATITUDE)),
                    c.getString(c.getColumnIndex(OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_LONGITUDE)),
                    c.getString(c.getColumnIndex(OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_SPEED)),
                    c.getString(c.getColumnIndex(OverspeedingDbContract.OverspeedingEntry.COLUMN_NAME_TIMESTAMP)));
            overspeedingList.add(overspeedingPoint);
        }
        db.close();
        return overspeedingList;
    }

    public void mapButtonMethod(View view){ //Login button is clicked.
        List<OverspeedingPoint> overspeedingList = getAllOverspeedingPoints();

        secondActivity(); //Go to OverspeedingMap activity.
    }

    public void secondActivity() {
        Intent intent = new Intent(getApplicationContext(), OverspeedingMap.class);
        startActivity(intent);
    }


    public void changeLanguage(View view) {

        showChangeLanguageDialog();

    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"Ελληνικά", "English", "Deutsch"};

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle(getString(R.string.changeLanguage));
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0) {

                    setLocale("el");
                    recreate();
                }
                else if (i == 1) {

                    setLocale("en");
                    recreate();
                }
                else if (i == 2) {

                    setLocale("de");
                    recreate();
                }
            }

        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    private void setLocale(String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang", language);
        editor .apply();

    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}