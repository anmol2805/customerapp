package com.anmol.customerapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anmol.customerapp.Recycler.ItemClickListener;
import com.anmol.customerapp.Recycler.MyAdapter;
import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class ListActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private List<Media> medias;
    private ListView lv;
    private RecyclerView rv;
    private MediaAdapter adapter;

    private ViewPager viewPager;
    private ProgressDialog progressDialog;
    StorageReference storageReference;
    ImageButton browse;
    ImageView image,img,previewimg;
    Uri mediauri;
    ImageButton edit,crop,rotate;
    private static final int REQUEST_CODE = 1234;
    protected static final String EXTRA_RES_ID = "POS";
    private RecyclerView.Adapter madapter;
    String mediatype;
    Bitmap bmp;
    EditText description;
    String source;
    Button confirm;
    Uri edituri,resulturi;
    String edittype;
    ImageView editprev;
    String uid;
    TextView deladd;
    private static final int MY_PERMISSIONS_REQUEST = 123;
    private static final int PICK_IMAGE_REQUEST_CODE = 213;
    int s = 0, c = 0;
    private Uri outputFileUri;
    Uri imageuri,pdfuri;
    EditText pdfdesr,imgdesr;
    String imagedescription,pdfdescription;
    TextView pdf;
    CardView menu;
    Animation slideup,slidedown;
    ImageButton gallery,docs,files;
    ArrayList<String> filepaths;
    CardView address;
    double latitudec,longitudec;
    DatabaseReference ordersdata,chemistdata;
    FirebaseAuth auth;
    GeoFire geoFirec,geoFired;
    boolean status;
    int radius = 50;
    double lat=28.642317361385285,log=77.21725802868605;
    GeoLocation geoLocation;
    Location geolocation;
    double la,lo;
    DatabaseReference tokendatabase;
    ImageButton fa;
    LinearLayout lm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST);
        }
        getSupportActionBar().hide();
        medias = new ArrayList<>();
        address = (CardView)findViewById(R.id.address);
        lm = (LinearLayout)findViewById(R.id.listimg);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this,MapsActivity.class));
            }
        });
        fa = (ImageButton)findViewById(R.id.firstadd);
        fa.setVisibility(View.GONE);
        deladd = (TextView)findViewById(R.id.deladd);
        deladd.setText(getIntent().getStringExtra("address"));
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        latitudec = (double) bundle.get("latitude");
        longitudec = (double) bundle.get("longitude");
        status = false;
        auth = FirebaseAuth.getInstance();
        ordersdata = FirebaseDatabase.getInstance().getReference().child("orders").child("customers");
        chemistdata = FirebaseDatabase.getInstance().getReference().child("orders").child("chemist");
        lv = (ListView) findViewById(R.id.listview);
        rv = (RecyclerView)findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        edit = (ImageButton)findViewById(R.id.edit);
        confirm = (Button)findViewById(R.id.confirmorder);
        description = (EditText)findViewById(R.id.card);
        menu = (CardView)findViewById(R.id.menu);
        menu.setVisibility(View.GONE);
        slidedown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slidedown);
        slideup = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slideup);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        storageReference = FirebaseStorage.getInstance().getReference();
        previewimg = (ImageView)findViewById(R.id.previewimg);
        browse = (ImageButton)findViewById(R.id.browse);
        gallery = (ImageButton)findViewById(R.id.gallery);
        docs = (ImageButton)findViewById(R.id.document);
        files = (ImageButton)findViewById(R.id.files);
        filepaths = new ArrayList<>();
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filepaths.clear();
                FilePickerBuilder.getInstance().setMaxCount(5).setSelectedFiles(filepaths).setActivityTheme(R.style.AppTheme).pickPhoto(ListActivity.this);
            }
        });
        docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filepaths.clear();
                FilePickerBuilder.getInstance().setMaxCount(3).setSelectedFiles(filepaths).setActivityTheme(R.style.AppTheme).pickFile(ListActivity.this);
            }
        });
        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this,GridActivity.class));
            }
        });
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //permissionRequest();
                menu.setVisibility(View.VISIBLE);

            }
        });
        fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.setVisibility(View.VISIBLE);
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference().child("prescription").child(auth.getCurrentUser().getUid());
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ListActivity.this,Confirm_Order.class);
                Bundle b = new Bundle();
                b.putString("des",description.getText().toString());
                b.putString("loc",getIntent().getStringExtra("address"));
                b.putDouble("lat",latitudec);
                b.putDouble("log",longitudec);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                Iterator iterator = dataSnapshot.getChildren().iterator();


                Set<Media> set = new HashSet<Media>();
                while (iterator.hasNext()) {
                    set.add((Media) ((DataSnapshot) iterator.next()).getValue(Media.class));
                }
                medias.clear();
                medias.addAll(set);
                adapter = new MediaAdapter(ListActivity.this, R.layout.item, medias);
                ItemClickListener itemClickListener = new ItemClickListener() {
                    @Override
                    public void onItemClick(int pos) {
                        if((medias.get(pos).getType()).contains("image")){
                            Glide.with(ListActivity.this).load(medias.get(pos).getUrl()).into(previewimg);
                            source = medias.get(pos).getPresuri();
                            edituri = Uri.parse(medias.get(pos).getPresuri());
                            edittype = medias.get(pos).getType();
                            edit.setVisibility(View.VISIBLE);
                            uid = medias.get(pos).getUploadid();

                        }
                        else{
                            generateImageFromPdf(Uri.parse(medias.get(pos).getPresuri()));
                            previewimg.setImageBitmap(bmp);
                            edit.setVisibility(View.GONE);
                        }
                    }
                };
                madapter = new MyAdapter(ListActivity.this,medias,itemClickListener);
                if(madapter.getItemCount()==0){
                    fa.setVisibility(View.VISIBLE);

                }
                else {
                    rv.setAdapter(madapter);
                    fa.setVisibility(View.GONE);
                }

                lv.setAdapter(adapter);


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                        if((medias.get(i).getType()).contains("image")){
                            Glide.with(ListActivity.this).load(medias.get(i).getUrl()).into(previewimg);
                            source = medias.get(i).getPresuri();
                            edituri = Uri.parse(medias.get(i).getPresuri());
                            edittype = medias.get(i).getType();
                            edit.setVisibility(View.VISIBLE);
                            uid = medias.get(i).getUploadid();

                        }
                        else{
                            generateImageFromPdf(Uri.parse(medias.get(i).getPresuri()));
                            previewimg.setImageBitmap(bmp);
                            edit.setVisibility(View.GONE);
                        }


                    }
                });


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ListActivity.this);
                dialog.setContentView(R.layout.editdialog);
                dialog.setTitle("Edit");
                dialog.show();
                ImageButton cancel = (ImageButton)dialog.findViewById(R.id.cancel);
                ImageButton crop = (ImageButton)dialog.findViewById(R.id.crop);
                ImageButton rotate = (ImageButton)dialog.findViewById(R.id.rotate);
                ImageButton draw = (ImageButton)dialog.findViewById(R.id.draw);
                ImageButton undo = (ImageButton)dialog.findViewById(R.id.undo);
                ImageButton save = (ImageButton)dialog.findViewById(R.id.save);
                imgdesr = (EditText)dialog.findViewById(R.id.editText);
                editprev = (ImageView) dialog.findViewById(R.id.editprev);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                editprev.setImageURI(edituri);
                resulturi = edituri;
                crop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startCropImageActivity(edituri);

                    }
                });
                undo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editprev.setImageURI(edituri);
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        imagedescription = imgdesr.getText().toString().trim();
                        final ProgressDialog progressDialog = new ProgressDialog(ListActivity.this);
                        progressDialog.setTitle("Uploading..");
                        progressDialog.show();
                        StorageReference reference = storageReference.child("prescription").child(resulturi.getLastPathSegment());
                        reference.putFile(resulturi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                progressDialog.dismiss();
                                Toast.makeText(ListActivity.this, "Task done", Toast.LENGTH_SHORT).show();
                                Media media = new Media(taskSnapshot.getDownloadUrl().toString(),resulturi.toString(),taskSnapshot.getMetadata().getContentType(),imagedescription,uid);
                                //String uploadId = databaseReference.push().getKey();
                                databaseReference.child(uid).setValue(media);

                                dialog.dismiss();


                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ListActivity.this.getContentResolver(), resulturi);
                                    File file = Environment.getExternalStorageDirectory();
                                    File dir = new File(file.getAbsolutePath() + "/ChemistApp");
                                    dir.mkdirs();
                                    File f = new File(dir,uid + ".jpg");
                                    OutputStream outputStream = new FileOutputStream(f);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
                                    outputStream.flush();
                                    outputStream.close();
//                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
//                                    byte[] bytesBitmap = byteArrayOutputStream.toByteArray();
//                                    File temp = File.createTempFile("prescription", uid + ".jpg");
//                                    FileOutputStream fileOutputStream = new FileOutputStream(temp);
//                                    fileOutputStream.write(bytesBitmap);
//                                    fileOutputStream.flush();
//                                    fileOutputStream.close();
//                                    resulturi = Uri.fromFile(temp);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });



            }
        });


    }



    public class ImageFileFilter implements FileFilter
    {
        File file;
        private final String[] okFileExtensions =  new String[] {"doc", "docx", "pdf","txt"};

        /**
         *
         */
        public ImageFileFilter(File newfile)
        {
            this.file=newfile;
        }

        public boolean accept(File file)
        {
            for (String extension : okFileExtensions)
            {
                if (file.getName().toLowerCase().endsWith(extension))
                {
                    return true;
                }
            }
            return false;
        }

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
            mediauri = getImageUri(this,bmp);
            saveImage(bmp);
            pdfiumCore.closeDocument(pdfDocument);// important!

        } catch(Exception e) {
            //todo with exception
        }
    }

    public final static String FOLDER = Environment.getExternalStorageDirectory() + "/PDF";
    private void saveImage(Bitmap bmp) {
        FileOutputStream out = null;
        try {
            File folder = new File(FOLDER);
            if(!folder.exists())
                folder.mkdirs();
            File file = new File(folder, "PDF.png");
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            //todo with exception
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
                //todo with exception
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    @Override
    public void onBackPressed() {

        if(menu.getVisibility() == View.VISIBLE){
            menu.setVisibility(View.GONE);
        }
        else{
            super.onBackPressed();
            DatabaseReference dref = FirebaseDatabase.getInstance().getReference().getRoot();
            dref.child("prescription").removeValue();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().getRoot();
        dref.child("prescription").removeValue();
    }
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    private void permissionRequest() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
            imageChooser();

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    //IMAGE PICKER WHEN CHOOSE IMAGE BUTTON IS CLICKED
    private void imageChooser() {

        File root = new File(Environment.getExternalStorageDirectory() + File.separator + "ChemistApp" + File.separator + "Prescription" + File.separator);
        root.mkdirs();
        final String fname = "prescription" + System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        //Camera
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final String localPackageName = res.activityInfo.loadLabel(packageManager).toString();
            if (localPackageName.toLowerCase().equals("camera")) {
                final Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(packageName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                cameraIntents.add(intent);
            }
        }
        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == REQUEST_CODE){
                pdfuri = data.getData();
                pdf.setText(String.valueOf(pdfuri));
            }
            if(requestCode == FilePickerConst.REQUEST_CODE_PHOTO && data!=null){
                menu.setVisibility(View.GONE);
                filepaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
                final ProgressDialog progressDialog = new ProgressDialog(ListActivity.this);
                progressDialog.setTitle("Uploading..");
                progressDialog.show();
                for(String path : filepaths){
                    final Uri uri = Uri.fromFile(new File(path));
                    StorageReference reference = storageReference.child("prescription").child(uri.getLastPathSegment());
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(ListActivity.this, "Task done", Toast.LENGTH_SHORT).show();
                            String uploadid = databaseReference.push().getKey();
                            Media media = new Media(taskSnapshot.getDownloadUrl().toString(), uri.toString(), taskSnapshot.getMetadata().getContentType(), "",uploadid);
                            databaseReference.child(uploadid).setValue(media);

                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(ListActivity.this.getContentResolver(), uri);
                                File file = Environment.getExternalStorageDirectory();
                                File dir = new File(file.getAbsolutePath() + "/ChemistApp");
                                dir.mkdirs();
                                File f = new File(dir,uploadid + ".jpg");
                                OutputStream outputStream = new FileOutputStream(f);
                                bitmap.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
                                outputStream.flush();
                                outputStream.close();
//                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
//                                    byte[] bytesBitmap = byteArrayOutputStream.toByteArray();
//                                    File temp = File.createTempFile("prescription", uid + ".jpg");
//                                    FileOutputStream fileOutputStream = new FileOutputStream(temp);
//                                    fileOutputStream.write(bytesBitmap);
//                                    fileOutputStream.flush();
//                                    fileOutputStream.close();
//                                    resulturi = Uri.fromFile(temp);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }
                progressDialog.dismiss();

            }
            if(requestCode == FilePickerConst.REQUEST_CODE_DOC && data!=null){
                menu.setVisibility(View.GONE);
                filepaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS);
                final ProgressDialog progressDialog = new ProgressDialog(ListActivity.this);
                progressDialog.setTitle("Uploading..");
                progressDialog.show();
                for(String path : filepaths){
                    final Uri uri = Uri.fromFile(new File(path));
                    StorageReference reference = storageReference.child("prescription").child(uri.getLastPathSegment());
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(ListActivity.this, "Task done", Toast.LENGTH_SHORT).show();
                            String uploadid = databaseReference.push().getKey();
                            Media media = new Media(taskSnapshot.getDownloadUrl().toString(), uri.toString(), taskSnapshot.getMetadata().getContentType(), "",uploadid);
                            databaseReference.child(uploadid).setValue(media);



                        }
                    });
                }
                progressDialog.dismiss();
            }
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                final Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }
                final ProgressDialog progressDialog = new ProgressDialog(ListActivity.this);
                progressDialog.setTitle("Uploading..");
                progressDialog.show();


                //imageuri = getImageUri(Addprescription.this,bitmapMaster);
                StorageReference reference = storageReference.child("prescription").child(selectedImageUri.getLastPathSegment());
                reference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        progressDialog.dismiss();
                        Toast.makeText(ListActivity.this, "Task done", Toast.LENGTH_SHORT).show();
                        String uploadid = databaseReference.push().getKey();
                        Media media = new Media(taskSnapshot.getDownloadUrl().toString(), selectedImageUri.toString(), taskSnapshot.getMetadata().getContentType(), imagedescription,uploadid);

                        databaseReference.child(uploadid).setValue(media);



                    }
                });
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    editprev.setImageURI(result.getUri());
                    resulturi = result.getUri();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                }
            }

//            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if (resultCode == RESULT_OK) {
//                    source = result.getUri();
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), source);
//                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
//                        byte[] bytesBitmap = byteArrayOutputStream.toByteArray();
//                        File temp = File.createTempFile("store", "pic.jpg");
//                        FileOutputStream fileOutputStream = new FileOutputStream(temp);
//                        fileOutputStream.write(bytesBitmap);
//                        fileOutputStream.flush();
//                        fileOutputStream.close();
//                        source = Uri.fromFile(temp);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        tempBitmap = BitmapFactory.decodeStream(
//                                getContentResolver().openInputStream(source));
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//                    Bitmap.Config config;
//                    if(tempBitmap.getConfig() != null){
//                        config = tempBitmap.getConfig();
//                    }else{
//                        config = Bitmap.Config.ARGB_8888;
//                    }
//
//                    //bitmapMaster is Mutable bitmap
//                    bitmapMaster = Bitmap.createBitmap(
//                            tempBitmap.getWidth(),
//                            tempBitmap.getHeight(),
//                            config);
//
//                    canvasMaster = new Canvas(bitmapMaster);
//                    canvasMaster.drawBitmap(tempBitmap, 0, 0, null);
//
//                    imageResult.setImageBitmap(bitmapMaster);
//                    Toast.makeText(Addprescription.this,"Please make sure that you don't have any type of contact details on your prescription",Toast.LENGTH_LONG).show();
//                    Toast.makeText(Addprescription.this,"You can scratch if any contact details present on the image",Toast.LENGTH_LONG).show();
//
//                    //imageResult.setImageURI(source);
//                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Exception error = result.getError();
//                }
//
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST && grantResults.length > 0) {
            Log.i("grantresults", grantResults.toString());
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Cannot use external storage!!", Toast.LENGTH_LONG).show();
                    return;
                }
                s = 1;
            }
            if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Cannot use Camera!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                c = 1;
            }

        }




    }


}
