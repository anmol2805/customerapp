package com.anmol.customerapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anmol.customerapp.Recycler.ItemClickListener;
import com.anmol.customerapp.Recycler.MyAdapter;

import java.util.List;

/**
 * Created by anmol on 9/10/2017.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    Context c;
    List<OrdModel> ordModels;
    private ItemClickListener mitemClickListener;

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
    public void onBindViewHolder(OrdersAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public ViewHolder(View v, ItemClickListener mitemClickListener) {
            super(v);
        }
    }
}
