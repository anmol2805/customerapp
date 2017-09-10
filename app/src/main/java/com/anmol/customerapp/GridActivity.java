package com.anmol.customerapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by anmol on 2017-08-29.
 */

public class GridActivity extends AppCompatActivity {
    private File file;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    GridView grid;
    ImageAdapter adapter;
    Toolbar toolbar;
    private ArrayList<String> imguri = new ArrayList<>();
    Button done;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/
        setContentView(R.layout.grid_layout);



        GridView gridView = (GridView) findViewById(R.id.grid_view);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("prescription");
        done=(Button)findViewById(R.id.done);
        file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "ChemistApp");
        // Create a new folder if no folder named SDImageTutorial exist
        file.mkdirs();


        if (file.isDirectory())

        {
            listFile = file.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];


            for (int i = 1; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }
        }

        // Pass String arrays to LazyAdapter Class
        adapter = new ImageAdapter(this, FilePathStrings, FileNameStrings);
        // Set the LazyAdapter to the GridView
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                CheckBox checkBox=(CheckBox)view.findViewById(R.id.checkbox);
                checkBox.setVisibility(View.VISIBLE);
                adapter.setChecked(position);
                adapter.notifyDataSetChanged();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(GridActivity.this,ListActivity.class);
//                startActivity(i);
                imguri = adapter.getArraList();
                for(String path : imguri){
                    final Uri uri = Uri.fromFile(new File(path));
                    StorageReference reference = storageReference.child("prescription").child(uri.getLastPathSegment());
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(GridActivity.this, "Task done", Toast.LENGTH_SHORT).show();
                            String uploadid = databaseReference.push().getKey();
                            Media media = new Media(taskSnapshot.getDownloadUrl().toString(), uri.toString(), taskSnapshot.getMetadata().getContentType(), "",uploadid);
                            databaseReference.child(uploadid).setValue(media);




                        }
                    });
                }
                finish();
            }
        });


    }
}
