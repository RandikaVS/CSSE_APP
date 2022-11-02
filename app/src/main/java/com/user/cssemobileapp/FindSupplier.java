package com.user.cssemobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.user.cssemobileapp.databinding.ActivityFindSupplierBinding;

public class FindSupplier extends DrawerBase implements AdapterView.OnItemSelectedListener {

    ActivityFindSupplierBinding activityFindSupplierBinding;

    private Button FindSupplier;
    private Spinner Spinner;

    String selectedMaterial;

    String[] material = { "Steel_Nails", "Cement",
            "Sand", "Tiles",
            "Steel_Bars", "Block_Stone","Interlock","Polycarbonate","Asbestos_Sheets","Working_Jumpers" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFindSupplierBinding = ActivityFindSupplierBinding.inflate(getLayoutInflater());
        setContentView(activityFindSupplierBinding.getRoot());
        allocateActivityTitle("Find Supplier");

        FindSupplier = findViewById(R.id.find_supplier_btn);
        Spinner = findViewById(R.id.material_select_spinner);

        FindSupplier.setEnabled(false);

        Spinner.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,material);

        // set simple layout resource file
        // for each item of spinner
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        Spinner.setAdapter(arrayAdapter);

        FindSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FindSupplier.this,SelectSupplier.class);
                intent.putExtra("material",selectedMaterial);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(getApplicationContext(),material[i], Toast.LENGTH_SHORT).show();
        FindSupplier.setEnabled(true);
        selectedMaterial = material[i];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}