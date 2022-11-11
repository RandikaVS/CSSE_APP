package com.user.cssemobileapp;

import static com.user.cssemobileapp.constants.Url.dbName;
import static com.user.cssemobileapp.constants.Url.rootUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.user.cssemobileapp.constants.Url;
import com.user.cssemobileapp.models.Supplier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectSupplier extends AppCompatActivity {

    private ListView ListView;
    private TextView SearchTag;
    private ImageView BackFindSupplier;
    SupplierAdapter supplierAdapter;
    public static ArrayList<Supplier> supplierArrayList = new ArrayList<>();

    String item;
    String URL;
    Supplier supplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_supplier);

        ListView = findViewById(R.id.supplier_list);
        SearchTag = findViewById(R.id.search_tag);
        BackFindSupplier =findViewById(R.id.back_find_supplier);

        Bundle extras = getIntent().getExtras();
        item = extras.getString("material");
        URL= rootUrl+"/"+dbName+"/FindSupplier.php?item="+item;
        SearchTag.setText(item);

        BackFindSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectSupplier.this,FindSupplier.class);
                startActivity(intent);
            }
        });

        supplierAdapter = new SupplierAdapter(this,supplierArrayList);

        ListView.setAdapter(supplierAdapter);

        retrieveSuppliers();

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String supplierId = supplierArrayList.get(i).getId();
                String supplies = supplierArrayList.get(i).getSupplies();

                Intent intent = new Intent(SelectSupplier.this,CreatePurchaseRequest.class);
                intent.putExtra("supplierId",supplierId);
                intent.putExtra("material",supplies);
                startActivity(intent);
            }
        });
    }




    public void retrieveSuppliers(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        supplierArrayList.clear();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if(success.equals("1")){
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String address = object.getString("address");
                                    String supplies = object.getString("supplies");

                                    supplier = new Supplier(id,name,address,supplies);
                                    supplierArrayList.add(supplier);
                                    supplierAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectSupplier.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}