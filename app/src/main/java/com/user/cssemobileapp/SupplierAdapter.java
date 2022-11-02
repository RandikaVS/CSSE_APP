package com.user.cssemobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.user.cssemobileapp.models.Supplier;

import java.util.List;


public class SupplierAdapter extends ArrayAdapter<Supplier> {

    Context context;
    List<Supplier> employeeList;


    public SupplierAdapter(@NonNull Context context, List<Supplier> employeeList) {
        super(context,R.layout.supplier_list_item,employeeList);

        this.context = context;
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supplier_list_item,null,true);

        TextView supplierName = view.findViewById(R.id.supplier_name);
        TextView supplierAddress = view.findViewById(R.id.supplier_address);
        TextView supplierSupplies = view.findViewById(R.id.supplier_supplies);

        supplierName.setText(employeeList.get(position).getName());
        supplierAddress.setText(employeeList.get(position).getAddress());
        supplierSupplies.setText(employeeList.get(position).getSupplies());

        return view;
    }
}














