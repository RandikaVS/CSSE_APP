package com.user.cssemobileapp;

import static com.user.cssemobileapp.constants.Url.rootUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.user.cssemobileapp.databinding.ActivityRequisiteOrdersBinding;
import com.user.cssemobileapp.models.Orders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequisiteOrders extends DrawerBase implements AdapterView.OnItemSelectedListener{

    ActivityRequisiteOrdersBinding activityRequisiteOrdersBinding;

    private ListView ListView;
    private Spinner Spinner;
    private Button FilterBtn;
    RequisiteOrdersAdapter requisiteOrdersAdapter;
    public static ArrayList<Orders> ordersArrayList = new ArrayList<>();

    String URL= rootUrl+"/csse/GetOrders.php?filter=All";
    String selectedFilter;

    Orders orders;

    String[] filter = { "All", "Approved",
            "Rejected","Pending","Received"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRequisiteOrdersBinding = ActivityRequisiteOrdersBinding.inflate(getLayoutInflater());
        setContentView(activityRequisiteOrdersBinding.getRoot());
        allocateActivityTitle("Requisite Orders");

        ListView = findViewById(R.id.requisite_orders);
        Spinner = findViewById(R.id.order_filter_spinner);
        FilterBtn = findViewById(R.id.filter_btn);

        Spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,filter);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner.setAdapter(arrayAdapter);

        requisiteOrdersAdapter = new RequisiteOrdersAdapter(this,ordersArrayList);

        retrieveOrders();
        ListView.setAdapter(requisiteOrdersAdapter);

        FilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL= rootUrl+"/csse/GetOrders.php?filter="+selectedFilter;
                retrieveOrders();
                ListView.setAdapter(requisiteOrdersAdapter);
            }
        });

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String orderId = ordersArrayList.get(i).getOrderId();

                Intent intent = new Intent(RequisiteOrders.this,ViewOrder.class);
                intent.putExtra("orderId",orderId);
                startActivity(intent);
            }
        });
    }

    public void retrieveOrders(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ordersArrayList.clear();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if(success.equals("1")){
                                for(int i=0;i<jsonArray.length();i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String orderId = object.getString("orderId");
                                    String material = object.getString("material");
                                    String status = object.getString("status");

                                    orders = new Orders(object.getString("orderId"),orderId,object.getString("companyName")
                                            ,material,object.getString("quantity"),object.getString("deliveryAddress")
                                            ,object.getString("requisitionerName"),object.getString("supplierId")
                                            ,object.getString("date"),object.getString("total"),status);

                                    ordersArrayList.add(orders);
                                    requisiteOrdersAdapter.notifyDataSetChanged();

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
                Toast.makeText(RequisiteOrders.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(getApplicationContext(),filter[i], Toast.LENGTH_SHORT).show();
        selectedFilter = filter[i];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}