package com.user.cssemobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.user.cssemobileapp.models.Orders;

import java.util.List;

public class RequisiteOrdersAdapter extends ArrayAdapter<Orders> {

    Context context;
    List<Orders> ordersList;

    public RequisiteOrdersAdapter(@NonNull Context context,List<Orders> ordersList) {
        super(context, R.layout.orders_list,ordersList);

        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list,null,true);

        TextView OrderId = view.findViewById(R.id.order_id);
        TextView Material = view.findViewById(R.id.material);
        TextView Status = view.findViewById(R.id.order_status);

        OrderId.setText(ordersList.get(position).getSiteId());
        Material.setText(ordersList.get(position).getMaterial());
        Status.setText(ordersList.get(position).getStatus());

        return view;
    }
}
