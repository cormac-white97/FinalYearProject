package com.example.finalyearproject.ViewDetails;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.finalyearproject.CreationClasses.CreateNewEvent;
import com.example.finalyearproject.R;
import com.example.finalyearproject.ui.home.HomeFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class viewLocations extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final String locationKey = "com.example.eventmanager.locationKey";
    public static final String dateKey = "com.example.eventmanager.dateKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_locations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent showMap = getIntent();
        final String date = showMap.getStringExtra(HomeFragment.dateval);

        try {
            KmlLayer layer = new KmlLayer(mMap, R.raw.scouting_campsites, getApplicationContext());
            layer.addLayerToMap();

            layer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {

                @Override
                public void onFeatureClick(com.google.maps.android.data.Feature feature) {

                }

            });

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    LatLng position = marker.getPosition();
                    Bundle b = new Bundle();
                    b.putParcelable("location", new LatLng(position.longitude, position.latitude));
                    Intent intent = new Intent(viewLocations.this, CreateNewEvent.class);
                    intent.putExtra(dateKey, date);
                    intent.putExtra(locationKey, marker.getTitle());
                    intent.putExtras(b);
                    intent.putExtra("type", "createNew");
                    startActivity(intent);
                    finish();
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng defaultLocation = new LatLng(53.3385398, -7.5);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));

        mMap.setBuildingsEnabled(false);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(6.7f));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                TextView mapMsg = findViewById(R.id.txtMapMsg);
                mapMsg.setText("Tap to create an event.");

                return false;
            }
        });
    }
}