package com.anmol.customerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Order_Medicines extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__medicines);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_down);
    }
}
