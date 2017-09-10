package com.anmol.customerapp;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by anmol on 2017-08-04.
 */

public class MediaAdapter extends ArrayAdapter<Media> {
    private Activity context;
    private int resource;
    private List<Media> medias;

    public MediaAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Media> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
        medias = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);
        ImageView img = (ImageView)v.findViewById(R.id.imageview);
        TextView txt = (TextView)v.findViewById(R.id.iurl);
        txt.setText(medias.get(position).getUrl());
        if(medias.get(position).getType().contains("image")){
            Glide.with(context).load(medias.get(position).getUrl()).into(img);
        }

        return v;
    }
}
