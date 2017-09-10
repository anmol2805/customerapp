package com.anmol.customerapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anmol.customerapp.Recycler.ItemClickListener;
import com.anmol.customerapp.Recycler.MyAdapter;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Created by anmol on 9/10/2017.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    Context c;
    List<OrdModel> ordModels;
    private ItemClickListener mitemClickListener;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public OrdersAdapter(Context c, List<OrdModel> ordModels, ItemClickListener mitemClickListener) {
        this.c = c;
        this.ordModels = ordModels;
        this.mitemClickListener = mitemClickListener;
    }

    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.order,parent,false);
        return new OrdersAdapter.ViewHolder(v,mitemClickListener);
    }

    @Override
    public void onBindViewHolder(OrdersAdapter.ViewHolder holder, final int position) {
        holder.name.setText(ordModels.get(position).getName());
        holder.add.setText(ordModels.get(position).getAddress());
        holder.date.setText(ordModels.get(position).getDate());
        holder.oid.setText(auth.getCurrentUser().getUid() + "_" + ordModels.get(position).getOid());
        if(ordModels.get(position).isStatus()){
            holder.track.setVisibility(View.VISIBLE);
        }
        else {
            holder.track.setVisibility(View.GONE);
        }
        holder.track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c,Track_order.class);
                intent.putExtra("pos",ordModels.get(position).getOid());
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView oid,add,date,name;
        Button track;
        private ItemClickListener mitemClickListener;
        public ViewHolder(View v, ItemClickListener itemClickListener) {
            super(v);
            mitemClickListener = itemClickListener;
            oid = (TextView)v.findViewById(R.id.oid);
            add = (TextView)v.findViewById(R.id.address);
            date = (TextView)v.findViewById(R.id.date);
            name = (TextView)v.findViewById(R.id.name);
            track = (Button)v.findViewById(R.id.track);
            v.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            mitemClickListener.onItemClick(this.getAdapterPosition());
        }
    }
}
