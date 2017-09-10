package com.anmol.customerapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anmol on 9/5/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerViewClickListener mListener;
    private List<Media> medias = new ArrayList<>();

    RecyclerViewAdapter(RecyclerViewClickListener listener) {
        mListener = listener;
    }

    public void updateData(List<Media> dataset) {
        medias.clear();
        medias.addAll(dataset);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new RowViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(medias.get(position).getType().contains("image")){
            //Glide.with().load(medias.get(position).getUrl()).into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return medias.size();
    }


    public class RowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener mListener;
        public ImageView img;
        public RowViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            mListener = listener;
            img = (ImageView)v.findViewById(R.id.imageview);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }
}
