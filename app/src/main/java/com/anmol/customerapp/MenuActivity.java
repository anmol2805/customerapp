package com.anmol.customerapp;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.anmol.customerapp.Fragments.ambulance;
import com.anmol.customerapp.Fragments.main;
import com.anmol.customerapp.Fragments.notifications;
import com.anmol.customerapp.Fragments.settings;
import com.anmol.customerapp.Fragments.yourorders;

public class MenuActivity extends AppCompatActivity {

    ImageButton home,ambulance,yourorders,notifications,settings;
    FragmentManager fm;
    View v1,v2,v3,v4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        home = (ImageButton)findViewById(R.id.home);
        ambulance = (ImageButton)findViewById(R.id.ambulance);
        yourorders = (ImageButton)findViewById(R.id.orders);
        notifications = (ImageButton)findViewById(R.id.notifications);
        settings = (ImageButton)findViewById(R.id.settings);
        v1 = (View)findViewById(R.id.v1);
        v2 = (View)findViewById(R.id.v2);
        v3 = (View)findViewById(R.id.v3);
        v4 = (View)findViewById(R.id.v4);
        final RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.rl1);
        final RelativeLayout rl2 = (RelativeLayout)findViewById(R.id.rl2);
        fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content,new main()).commit();
        home.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        v1.setVisibility(View.INVISIBLE);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().replace(R.id.content,new main()).commit();

                home.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                rl1.setBackgroundColor(getResources().getColor(R.color.transparent));
                yourorders.setBackgroundColor(getResources().getColor(R.color.transparent));
                rl2.setBackgroundColor(getResources().getColor(R.color.transparent));
                settings.setBackgroundColor(getResources().getColor(R.color.transparent));
                v1.setVisibility(View.INVISIBLE);
                v2.setVisibility(View.VISIBLE);
                v3.setVisibility(View.VISIBLE);
                v4.setVisibility(View.VISIBLE);
            }
        });
        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().replace(R.id.content,new ambulance()).commit();
                home.setBackgroundColor(getResources().getColor(R.color.transparent));
                rl1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                yourorders.setBackgroundColor(getResources().getColor(R.color.transparent));
                rl2.setBackgroundColor(getResources().getColor(R.color.transparent));
                settings.setBackgroundColor(getResources().getColor(R.color.transparent));
                v1.setVisibility(View.INVISIBLE);
                v2.setVisibility(View.INVISIBLE);
                v3.setVisibility(View.VISIBLE);
                v4.setVisibility(View.VISIBLE);
            }
        });
        yourorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().replace(R.id.content,new yourorders()).commit();
                home.setBackgroundColor(getResources().getColor(R.color.transparent));
                rl1.setBackgroundColor(getResources().getColor(R.color.transparent));
                yourorders.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                rl2.setBackgroundColor(getResources().getColor(R.color.transparent));
                settings.setBackgroundColor(getResources().getColor(R.color.transparent));
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.INVISIBLE);
                v3.setVisibility(View.INVISIBLE);
                v4.setVisibility(View.VISIBLE);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().replace(R.id.content,new notifications()).commit();
                home.setBackgroundColor(getResources().getColor(R.color.transparent));
                rl1.setBackgroundColor(getResources().getColor(R.color.transparent));
                yourorders.setBackgroundColor(getResources().getColor(R.color.transparent));
                rl2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                settings.setBackgroundColor(getResources().getColor(R.color.transparent));
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.VISIBLE);
                v3.setVisibility(View.INVISIBLE);
                v4.setVisibility(View.INVISIBLE);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().replace(R.id.content,new settings()).commit();
                home.setBackgroundColor(getResources().getColor(R.color.transparent));
                rl1.setBackgroundColor(getResources().getColor(R.color.transparent));
                yourorders.setBackgroundColor(getResources().getColor(R.color.transparent));
                rl2.setBackgroundColor(getResources().getColor(R.color.transparent));
                settings.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.VISIBLE);
                v3.setVisibility(View.VISIBLE);
                v4.setVisibility(View.INVISIBLE);
            }
        });
    }
}
