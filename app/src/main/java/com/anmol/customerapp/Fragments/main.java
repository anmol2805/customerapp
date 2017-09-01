package com.anmol.customerapp.Fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anmol.customerapp.Book_Blood_Scans;
import com.anmol.customerapp.Book_Blood_Tests;
import com.anmol.customerapp.Hospitals;

import com.anmol.customerapp.MapsActivity;
import com.anmol.customerapp.Order_Medicines;
import com.anmol.customerapp.R;
import com.anmol.customerapp.Search_Doctor;
import com.anmol.customerapp.Surgical;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by anmol on 2017-07-15.
 */

public class main extends Fragment implements Animation.AnimationListener{
    ImageView img,img2,img3;
    ViewPager viewPager;
    LinearLayout slidedots;
    private int dotscount;
    private ImageView[] dots;
    TextView sd,hosp,med,dl,bt,sg;
    Toolbar toolbar;
    private static final int MY_PERMISSIONS_REQUEST = 123;
    Animation animationfadeout,animationfadein;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

            final View v = inflater.inflate(R.layout.main,container,false);


            img = (ImageView)v.findViewById(R.id.imageView4);
            img2 = (ImageView)v.findViewById(R.id.imageView5);
            img3 = (ImageView)v.findViewById(R.id.imageView6);
        sd = (TextView)v.findViewById(R.id.sd);
        med = (TextView)v.findViewById(R.id.med);
        hosp = (TextView)v.findViewById(R.id.hosp);
        bt = (TextView)v.findViewById(R.id.bt);
        dl = (TextView)v.findViewById(R.id.dl);
        sg = (TextView)v.findViewById(R.id.sg);
            viewPager = (ViewPager)v.findViewById(R.id.viewpager);
            animationfadeout = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_out);
            animationfadein = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in);
        ImageButton searchdoctor = (ImageButton)v.findViewById(R.id.search_dr);
        ImageButton ordermed = (ImageButton)v.findViewById(R.id.order_med);
        ImageButton bloodtest = (ImageButton)v.findViewById(R.id.bookbt);
        final ImageButton bloodscan = (ImageButton)v.findViewById(R.id.booksc);
        ImageButton hospital = (ImageButton)v.findViewById(R.id.hospitals);
        ImageButton surgical = (ImageButton)v.findViewById(R.id.surgery);
        slidedots = (LinearLayout)v.findViewById(R.id.sliderdots);
        searchdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent i = new Intent(getActivity(), Search_Doctor.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

            }
        });

        sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Search_Doctor.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        ordermed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                permissionRequest();
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Order_Medicines.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        bloodtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent i = new Intent(getActivity(), Book_Blood_Tests.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Book_Blood_Tests.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        bloodscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent i = new Intent(getActivity(), Book_Blood_Scans.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), Book_Blood_Scans.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent i = new Intent(getActivity(), Hospitals.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        hosp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Hospitals.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        surgical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                Intent i = new Intent(getActivity(), Surgical.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        sg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Surgical.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        Viewpageadapter viewpageadapter = new Viewpageadapter(getActivity());
        viewPager.setAdapter(viewpageadapter);
        dotscount = viewpageadapter.getCount();
        dots = new ImageView[dotscount];
        for (int i = 0;i<dotscount;i++){
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.non_active_dots));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);
            slidedots.addView(dots[i],params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.active_dots));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0 ; i< dotscount;i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.non_active_dots));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.active_dots));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),2000,4000);




        return v;
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

                if(getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(viewPager.getCurrentItem()==0){
                                viewPager.setCurrentItem(1);
                            }else if(viewPager.getCurrentItem()==1){
                                viewPager.setCurrentItem(2);
                            }else {
                                viewPager.setCurrentItem(0);
                            }
                        }
                    });

                }




        }
    }
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private void permissionRequest() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            startActivity(new Intent(getActivity(),MapsActivity.class));

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST && grantResults.length > 0) {
            Log.i("grantresults", grantResults.toString());
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(),"Granted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),MapsActivity.class));
            }

        }




    }



// some code


 }
