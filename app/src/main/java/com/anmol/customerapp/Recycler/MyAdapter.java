package com.anmol.customerapp.Recycler;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.anmol.customerapp.Media;
import com.anmol.customerapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anmol on 9/3/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context c;
    List<Media>medias;
    private ItemClickListener mitemClickListener;


    ItemClickListener itemClickListener;
    public MyAdapter(Context c, List<Media> objects,ItemClickListener itemClickListener) {
        this.c = c;
        medias = objects;
        mitemClickListener = itemClickListener;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v,mitemClickListener);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position){
        if(medias.get(position).getType().contains("image")){
            Glide.with(c).load(medias.get(position).getUrl()).into(holder.img);
        }


    }

    @Override
    public int getItemCount() {
        return medias.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        private ItemClickListener mitemClickListener;
        public MyViewHolder(View itemView,ItemClickListener itemClickListener) {
            super(itemView);
            mitemClickListener = itemClickListener;
            img = (ImageView)itemView.findViewById(R.id.imageview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mitemClickListener.onItemClick(this.getAdapterPosition());
        }
    }


}
