package com.anmol.customerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Search_Doctor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__doctor);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_down);
    }
}
