package com.anmol.customerapp;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static boolean mMapIsTouched = false;
    private GoogleMap mMap;
    LocationManager locationManager;
    Double lat, log;
    LatLng latLng;
    Marker marker;
    MarkerOptions markerOptions;
    Button set, setmyloc, search;
    EditText inputadd;
    String add;
    private GestureDetectorCompat mDetector;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private String[] permissions = {
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.CHANGE_NETWORK_STATE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,

    };
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private int permissionRecord;
    String placename = null, placelat = null, placelog = null;
    DatabaseReference databaseReference, mGeoDatabase;
    ImageView img;
    long time = 1000 * 60 * 1;

    double lattitude, longitude;
    FloatingActionButton myloc;
    GestureDetector gestureDetector;
    MotionEvent event;
    DatabaseReference ordersdata;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        myloc = (FloatingActionButton)findViewById(R.id.setmyloc);
        PlaceAutocompleteFragment places = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.textauto);
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                CameraPosition INIT =
                        new CameraPosition.Builder()
                                .target(place.getLatLng())
                                .zoom(17.5f)
                                .build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT), 2000, null);
                placename = String.valueOf(place.getAddress());
                placelat = String.valueOf(place.getLatLng().latitude);
                lattitude = place.getLatLng().latitude;
                placelog = String.valueOf(place.getLatLng().longitude);
                longitude = place.getLatLng().longitude;
                Toast.makeText(MapsActivity.this, placename, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(Status status) {

                Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();

            }
        });
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
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        latLng = new LatLng(28.632907, 77.21957);
        CameraPosition INIT =
                new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(13)
                        .build();
        mMap.setMinZoomPreference(10);
        mMap.setMaxZoomPreference(20);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
        myloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(MapsActivity.this, "Please wait while we are finding your location", Toast.LENGTH_LONG).show();
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            lat = location.getLatitude();
                            log = location.getLongitude();
                            lattitude = lat;
                            longitude = log;
                            LatLng mlatLng = new LatLng(lat, log);
                            CameraPosition INIT =
                                    new CameraPosition.Builder()
                                            .target(mlatLng)
                                            .zoom(17.5F)
                                            .build();

                            // use map to move camera into position
                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {
                                List<Address> addressList = geocoder.getFromLocation(lat, log, 1);
                                String add = addressList.get(0).getAddressLine(0);
                                String loc = addressList.get(0).getLocality();
                                String state = addressList.get(0).getAdminArea();
                                String post = addressList.get(0).getPostalCode();
                                String kn = addressList.get(0).getFeatureName();
                                String str = add + "," + loc + "," + state + "," + post;
                                placename = str;
                                placelat = String.valueOf(lat);
                                placelog = String.valueOf(log);
                                lattitude = lat;
                                longitude = log;
                                Toast.makeText(MapsActivity.this, placename, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                    });
                }

            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(final CameraPosition cameraPosition) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!mMapIsTouched){
                            Log.i("centerLat", String.valueOf(cameraPosition.target.latitude));

                            Log.i("centerLong", String.valueOf(cameraPosition.target.longitude));
                            lattitude = cameraPosition.target.latitude;
                            longitude = cameraPosition.target.longitude;
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {
                                List<Address> addressList = geocoder.getFromLocation(cameraPosition.target.latitude, cameraPosition.target.longitude, 1);
                                String add = addressList.get(0).getAddressLine(0);
                                String loc = addressList.get(0).getLocality();
                                String state = addressList.get(0).getAdminArea();
                                String post = addressList.get(0).getPostalCode();
                                String kn = addressList.get(0).getFeatureName();
                                String str = add + "," + loc + "," + state + "," + post;
                                placename = str;
                                placelat = String.valueOf(cameraPosition.target.latitude);
                                placelog = String.valueOf(cameraPosition.target.longitude);
                                Toast.makeText(MapsActivity.this, placename, Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }



                    }
                },500);

                //Toast.makeText(getApplicationContext(),String.valueOf(cameraPosition.target.latitude),Toast.LENGTH_SHORT).show();

            }

        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            permissionRecord = grantResults[0] = PackageManager.PERMISSION_GRANTED;
            int length = grantResults.length;
        }
    }
    private void buildAlertMessageNoGps() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final android.app.AlertDialog alert = builder.create();
        alert.show();
    }


}
