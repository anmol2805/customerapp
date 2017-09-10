package com.anmol.customerapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.anmol.customerapp.Services.ChangeService;
import com.firebase.geofire.GeoFire;
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
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    GeoFire geoFire;
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
        auth = FirebaseAuth.getInstance();
        //ActivityCompat.requestPermissions(this, permissions, 100);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        set = (Button) findViewById(R.id.confirm);
        myloc = (FloatingActionButton) findViewById(R.id.setmyloc);
        //setmyloc = (Button)findViewById(R.id.setmyloc);
        img = (ImageView) findViewById(R.id.confirm_address_map_custom_marker);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("location");
        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().getRoot());
        mGeoDatabase = FirebaseDatabase.getInstance().getReference().getRoot();

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
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addressList = geocoder.getFromLocation(Double.parseDouble(placelat),Double.parseDouble(placelog), 1);
                    String add = addressList.get(0).getAddressLine(0);
                    String loc = addressList.get(0).getLocality();
                    String state = addressList.get(0).getAdminArea();
                    String post = addressList.get(0).getPostalCode();
                    String kn = addressList.get(0).getFeatureName();
                    String str = add + "," + loc + "," + state + "," + post;
                    placename = str;
                    } catch (IOException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MapsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MapsActivity.this);
                }
                builder.setCancelable(false);
                builder.setTitle("Confirm location")
                        .setMessage(placename)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle bundle = new Bundle();
                                bundle.putDouble("latitude", Double.parseDouble(placelat));
                                bundle.putDouble("longitude", Double.parseDouble(placelog));
                                Intent intent = new Intent(MapsActivity.this,ListActivity.class);
                                intent.putExtra("address",placename);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("Change Location", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

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
        final CameraPosition INIT =
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
                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000,0, new LocationListener() {
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
                            Intent intent = new Intent(MapsActivity.this,ChangeService.class);

                            Log.i("centerLat", String.valueOf(cameraPosition.target.latitude));

                            Log.i("centerLong", String.valueOf(cameraPosition.target.longitude));
                            lattitude = cameraPosition.target.latitude;
                            longitude = cameraPosition.target.longitude;
                            placelat = String.valueOf(cameraPosition.target.latitude);
                            placelog = String.valueOf(cameraPosition.target.longitude);
//                            Geocoder geocoder = new Geocoder(getApplicationContext());
//                            try {
//                                List<Address> addressList = geocoder.getFromLocation(cameraPosition.target.latitude, cameraPosition.target.longitude, 1);
//                                String add = addressList.get(0).getAddressLine(0);
//                                String loc = addressList.get(0).getLocality();
//                                String state = addressList.get(0).getAdminArea();
//                                String post = addressList.get(0).getPostalCode();
//                                String kn = addressList.get(0).getFeatureName();
//                                String str = add + "," + loc + "," + state + "," + post;
//                                placename = str;
//                                placelat = String.valueOf(cameraPosition.target.latitude);
//                                placelog = String.valueOf(cameraPosition.target.longitude);
//                                //Toast.makeText(MapsActivity.this, placename, Toast.LENGTH_SHORT).show();
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

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

    private void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
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

    private void buildAlertMessageNoInternet() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Please ensure that your internet connection is enabled")
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                }).setNegativeButton("Already Enabled", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final android.app.AlertDialog alert = builder.create();
        alert.show();
    }


}

