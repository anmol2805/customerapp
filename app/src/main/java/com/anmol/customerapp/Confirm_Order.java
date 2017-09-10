package com.anmol.customerapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anmol.customerapp.Recycler.ItemClickListener;
import com.anmol.customerapp.Recycler.MyAdapter;
import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Confirm_Order extends AppCompatActivity {
    FirebaseAuth auth;
    TextView name,address,phone,phone2;
    RecyclerView rv;
    List<Media>medias;
    DatabaseReference databaseReference;
    MyAdapter madapter;
    Bitmap bmp;
    Button addcon,co;
    DatabaseReference ordersdata,chemistdata;
    GeoFire geoFirec,geoFired;
    int radius = 50;
    boolean status;
    GeoLocation geoLocation;
    double lat=28.642317361385285,log=77.21725802868605;
    double latitudec,longitudec;
    DatabaseReference to,authdata;
    int size = 0;
    String add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm__order);
        auth = FirebaseAuth.getInstance();
        name = (TextView)findViewById(R.id.name);
        address = (TextView)findViewById(R.id.addressdel);
        phone = (TextView)findViewById(R.id.phone);
        phone2 = (TextView)findViewById(R.id.phone2);
        addcon = (Button)findViewById(R.id.addphone);
        co = (Button)findViewById(R.id.co);
        name.setText(auth.getCurrentUser().getDisplayName());
        phone.setText(auth.getCurrentUser().getPhoneNumber());
        Bundle b = getIntent().getExtras();
        add= b.getString("loc");
        address.setText(add);
        latitudec = (double) b.get("lat");
        longitudec = (double)b.get("log");


        rv = (RecyclerView)findViewById(R.id.prescript);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        medias = new ArrayList<>();
        ordersdata = FirebaseDatabase.getInstance().getReference().child("orders").child("customers");
        authdata = ordersdata.child(auth.getCurrentUser().getUid());
        authdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size=0;
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    size++;
                }
                to = ordersdata.child(auth.getCurrentUser().getUid()).child(String.valueOf(size)).child("presuploaded");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chemistdata = FirebaseDatabase.getInstance().getReference().child("orders").child("chemist");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("prescription").child(auth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
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
                            Dialog dialog = new Dialog(Confirm_Order.this);
                            dialog.setContentView(R.layout.prev);
                            ImageView img = (ImageView)dialog.findViewById(R.id.previewf);
                            Glide.with(Confirm_Order.this).load(medias.get(pos).getUrl()).into(img);
                            dialog.show();
                        }
                        else{
                            generateImageFromPdf(Uri.parse(medias.get(pos).getPresuri()));
                            Dialog dialog = new Dialog(Confirm_Order.this);
                            dialog.setContentView(R.layout.prev);
                            ImageView img = (ImageView)dialog.findViewById(R.id.previewf);
                            img.setImageBitmap(bmp);
                            dialog.show();

                        }
                    }
                };
                madapter = new MyAdapter(Confirm_Order.this,medias,itemClickListener);
                if(madapter.getItemCount()==0){

                }
                else {
                    rv.setAdapter(madapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        addcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = new Dialog(Confirm_Order.this);
                d.setContentView(R.layout.editcon);
                final EditText edit = (EditText)d.findViewById(R.id.enter);
                Button ok = (Button)d.findViewById(R.id.ok);
                d.show();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String num = edit.getText().toString();
                        if(!TextUtils.isEmpty(num) && num.length()==10){
                            phone2.setText("+91"+num);
                            phone2.setVisibility(View.VISIBLE);
                            d.dismiss();
                        }
                        else {
                            edit.setError("Please enter a valid Contact number");
                        }
                    }
                });

            }
        });
        co.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geoFirec = new GeoFire(ordersdata.child(auth.getCurrentUser().getUid()).child(String.valueOf(size)));
                geoFirec.setLocation("location",new GeoLocation(latitudec,longitudec));
                geoFired = new GeoFire(chemistdata);
                geoFired.setLocation(auth.getCurrentUser().getUid()+"_"+radius,new GeoLocation(lat,log));
                DatabaseReference tokens = FirebaseDatabase.getInstance().getReference().child("tokens").child(auth.getCurrentUser().getUid());
                String token = FirebaseInstanceId.getInstance().getToken();
                Map<String,Object> map = new HashMap<>();
                map.put("token",token);
                tokens.updateChildren(map);
                geoFired.getLocation(auth.getCurrentUser().getUid() + "_" + radius, new LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        geoLocation = location;
                        GeoQuery geoQuery = geoFirec.queryAtLocation(geoLocation,radius);
                        geoQuery.removeAllListeners();
                        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                            @Override
                            public void onKeyEntered(String key, GeoLocation location) {
                                if(!status){
                                    Toast.makeText(Confirm_Order.this,key+"location found", Toast.LENGTH_SHORT).show();
                                    status = true;
                                }
                            }

                            @Override
                            public void onKeyExited(String key) {

                            }

                            @Override
                            public void onKeyMoved(String key, GeoLocation location) {

                            }

                            @Override
                            public void onGeoQueryReady() {
                                if(!status){
                                    Toast.makeText(Confirm_Order.this,"Not found", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onGeoQueryError(DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                movePres(databaseReference,to);
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());
                Map<String,Object> m = new HashMap<>();
                m.put("date",formattedDate);
                m.put("name",auth.getCurrentUser().getDisplayName());
                m.put("address",add);
                m.put("phone",auth.getCurrentUser().getPhoneNumber());
                m.put("phone2",phone2.getText().toString());
                m.put("status",false);
                ordersdata.child(auth.getCurrentUser().getUid()).child(String.valueOf(size)).updateChildren(m);
                databaseReference.removeValue();
                Intent intent = new Intent(Confirm_Order.this,Pending_Orders.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

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
    private void movePres(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            System.out.println("Success");

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
