package com.anmol.customerapp;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anmol.customerapp.Recycler.ItemClickListener;
import com.anmol.customerapp.Recycler.MyAdapter;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Track_order extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    List<Media> medias;
    Bitmap bmp;
    RecyclerView prv;
    MyAdapter madapter;
    TextView acc, duem;
    ImageButton call,chat;
    String dial,dialland;
    private static final int MY_PERMISSIONS_REQUEST = 123;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        auth = FirebaseAuth.getInstance();
        prv = (RecyclerView) findViewById(R.id.prv);
        prv.setHasFixedSize(true);
        prv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        medias = new ArrayList<>();
        acc = (TextView) findViewById(R.id.acc);
        duem = (TextView) findViewById(R.id.due);
        call = (ImageButton) findViewById(R.id.call);
        chat = (ImageButton) findViewById(R.id.chat);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("orders").child("customers").child(auth.getCurrentUser().getUid()).child(getIntent().getStringExtra("pos"));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String accepted = dataSnapshot.child("accepted_by").getValue().toString();
                String due = dataSnapshot.child("due").getValue().toString();
                dial = dataSnapshot.child("chemistphone").getValue().toString();
                dialland = dataSnapshot.child("chemistland").getValue().toString();
                acc.setText(accepted);
                duem.setText(due);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(Track_order.this);
                dialog.setContentView(R.layout.dial);
                TextView cont = (TextView)dialog.findViewById(R.id.mcont);
                TextView land = (TextView)dialog.findViewById(R.id.mland);
                cont.setText("+91"+dial);
                land.setText(dialland);
                dialog.show();

//                Intent intent = new Intent(Intent.ACTION_CALL);
//
//                intent.setData(Uri.parse("tel:" + dial));
//                if (ActivityCompat.checkSelfPermission(Track_order.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(Track_order.this,
//                            new String[]{Manifest.permission.CALL_PHONE},
//                            MY_PERMISSIONS_REQUEST);
//
//                }else{
//                    startActivity(intent);
//                }

            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(Track_order.this);
                dialog.setContentView(R.layout.chat);
                /*
                Your code here
                 */
                dialog.show();
            }
        });
        databaseReference.child("presuploaded").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Set<Media> set = new HashSet<Media>();
                while (iterator.hasNext()) {
                    set.add((Media) ((DataSnapshot) iterator.next()).getValue(Media.class));
                }
                medias.clear();
                medias.addAll(set);
                ItemClickListener itemClickListener = new ItemClickListener() {
                    @Override
                    public void onItemClick(int pos) {
                        if((medias.get(pos).getType()).contains("image")){
                            Dialog dialog = new Dialog(Track_order.this);
                            dialog.setContentView(R.layout.prev);
                            ImageView img = (ImageView)dialog.findViewById(R.id.previewf);
                            Glide.with(Track_order.this).load(medias.get(pos).getUrl()).into(img);
                            dialog.show();
                        }
                        else{
                            generateImageFromPdf(Uri.parse(medias.get(pos).getPresuri()));
                            Dialog dialog = new Dialog(Track_order.this);
                            dialog.setContentView(R.layout.prev);
                            ImageView img = (ImageView)dialog.findViewById(R.id.previewf);
                            img.setImageBitmap(bmp);
                            dialog.show();

                        }
                    }
                };
                madapter = new MyAdapter(Track_order.this,medias,itemClickListener);
                prv.setAdapter(madapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    void generateImageFromPdf(Uri pdfUri) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(this);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
            pdfiumCore.closeDocument(pdfDocument);// important!

        } catch(Exception e) {
            //todo with exception
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST && grantResults.length > 0) {
            Log.i("grantresults", grantResults.toString());
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }

        }




    }

}
