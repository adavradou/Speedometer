package com.example.ergasia2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class OverspeedingMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overspeeding_map);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        OverspeedingDbHelper mDbHelper = new OverspeedingDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        this.mMap = googleMap;
        LatLng markersList = null;

        Cursor cursor = db.rawQuery("Select * from overspeedingDetails", new String[]{});

        if (cursor.getCount() != 0) {

            while (cursor.moveToNext()) {

                markersList = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
                this.mMap.addMarker(new MarkerOptions().position(markersList));

            }
            this.mMap.moveCamera(CameraUpdateFactory.newLatLng(markersList));

        }

    }
}

