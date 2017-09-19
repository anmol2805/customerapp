package com.anmol.customerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.anmol.customerapp.Recycler.ItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Pending_Orders extends AppCompatActivity {
    RecyclerView rv;
    OrdersAdapter ordersAdapter;
    List<OrdModel>ordModels;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    int size = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending__orders);
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ordModels = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("orders").child("customers").child(auth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ordModels.clear();

                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String date = data.child("date").getValue().toString();
                    String add = data.child("address").getValue().toString();
                    String name = data.child("name").getValue().toString();
                    Boolean status = (Boolean) data.child("status").getValue();
                    String oid = data.getKey();
                    OrdModel ordModel = new OrdModel(date,add,name,oid,status);
                    ordModels.add(ordModel);
                }



                ItemClickListener itemClickListener = new ItemClickListener() {
                    @Override
                    public void onItemClick(int pos) {

                    }
                };
                ordersAdapter = new OrdersAdapter(Pending_Orders.this,ordModels,itemClickListener);
                ordersAdapter.notifyDataSetChanged();
                rv.setAdapter(ordersAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MenuActivity.class));
        finish();
    }
}
