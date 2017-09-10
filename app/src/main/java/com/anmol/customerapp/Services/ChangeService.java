package com.anmol.customerapp.Services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.anmol.customerapp.MapsActivity;

import java.io.IOException;
import java.util.List;

/**
 * Created by anmol on 9/6/2017.
 */

public class ChangeService extends IntentService {
    public ChangeService() {
        super("ChangeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String placename,placelat,placelog;
        Bundle b = intent.getExtras();
        double latitude = b.getDouble("lat");
        double longitude = b.getDouble("log");
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            String add = addressList.get(0).getAddressLine(0);
            Toast.makeText(getApplicationContext(), add, Toast.LENGTH_SHORT).show();
            String loc = addressList.get(0).getLocality();
            String state = addressList.get(0).getAdminArea();
            String post = addressList.get(0).getPostalCode();
            String kn = addressList.get(0).getFeatureName();
            String str = add + "," + loc + "," + state + "," + post;
            placename = str;
            placelat = String.valueOf(latitude);
            placelog = String.valueOf(longitude);
            //Toast.makeText(getApplicationContext(), placename, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
