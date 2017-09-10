package com.anmol.customerapp;



/**
 * Created by Rakhi on 12-08-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter implements CompoundButton.OnCheckedChangeListener
{
    // Declare variables
    private Activity activity;
    private String[] filepath;
    private String[] filename;
    private SparseBooleanArray mCheckStates;
    ArrayList<String> imagePath=new ArrayList<>();
    CheckBox checkbox;


    private static LayoutInflater inflater = null;

    // Constructor
    public ImageAdapter(Activity a, String[] fpath, String[] fname){
        super(a,0);
        mCheckStates = new SparseBooleanArray(20);
        activity = a;
        filepath = fpath;
        filename = fname;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }





    @Override
    public int getCount() {
        return filepath.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.gridd_item, parent,false);

        checkbox=(CheckBox)vi.findViewById(R.id.checkbox);
        // Locate the ImageView in grid_item.xml
        ImageView image = (ImageView) vi.findViewById(R.id.image);
        RequestOptions opt=new RequestOptions();
        opt.centerCrop();

        Glide.with(activity).load(filepath[position]).apply(opt).into(image);
        checkbox.setTag(position);
        checkbox.setChecked(mCheckStates.get(position,false));
        checkbox.setOnCheckedChangeListener(this);
        return vi;
    }
    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);

    }

    public void setChecked(int position) {
        boolean val=!isChecked(position);
        mCheckStates.put(position, val);
        if(val){
            imagePath.add(filepath[position]);
            Toast.makeText(activity, "add"+filepath[position], Toast.LENGTH_SHORT).show();
        }
        else{
            imagePath.remove(filepath[position]);
            Toast.makeText(activity, "remove"+filepath[position], Toast.LENGTH_SHORT).show();

        }

        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor edit=shre.edit();
        edit.putString("Arrayimagepath", String.valueOf(imagePath));

        Log.i("sharedpref", String.valueOf(imagePath));
        Log.i("imagepath",edit.toString());
        edit.commit();
    }

    /*
        public void toggle(int position) {
            setChecked(position, !isChecked(position));
        }
    */
    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {
        // TODO Auto-generated method stub
        mCheckStates.put((Integer) buttonView.getTag(), isChecked);

    }
    public  ArrayList<String> getArraList(){
        return imagePath;
    }


}

