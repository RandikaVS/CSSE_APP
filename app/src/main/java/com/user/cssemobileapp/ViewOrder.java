package com.user.cssemobileapp;

import static com.user.cssemobileapp.constants.Url.rootUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewOrder extends AppCompatActivity {

    private TextView OrderId,SiteId,CompanyName,Material,Quantity,DeliveryAddress,RequisitionerName,SupplierId,Date,Total,Status;
    private Button MarkAsReceivedBtn;
    private ImageView BackOrders;

    String URL;
    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        OrderId = findViewById(R.id.order_details_orderId);
        SiteId = findViewById(R.id.order_details_siteId);
        CompanyName = findViewById(R.id.order_details_company);
        Material = findViewById(R.id.order_details_material);
        Quantity = findViewById(R.id.order_details_quantity);
        DeliveryAddress = findViewById(R.id.order_details_address);
        RequisitionerName = findViewById(R.id.order_details_rqName);
        SupplierId = findViewById(R.id.order_details_supplierId);
        Date = findViewById(R.id.order_details_date);
        Total = findViewById(R.id.order_details_total);
        Status = findViewById(R.id.order_details_status);
        MarkAsReceivedBtn = findViewById(R.id.mark_as_received_btn);
        BackOrders = findViewById(R.id.back_orders);

        BackOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewOrder.this,RequisiteOrders.class);
                startActivity(intent);
            }
        });


        Bundle extra = getIntent().getExtras();

        orderId = extra.getString("orderId");

        URL= rootUrl+"/csse/GetOrderDetails.php?id="+orderId;

        getOrderDetails();

        MarkAsReceivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                markOrderReceived();
            }
        });

    }

    public void markOrderReceived(){

        String UpdateUrl = rootUrl+"/csse/UpdateOrderReceived.php?id="+orderId;

        HttpsTrustManager.allowAllSSL();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UpdateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");

                    if(success.equals("1")){
                        Toast.makeText(ViewOrder.this,"Order Updated",Toast.LENGTH_SHORT).show();

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ViewOrder.this,RequisiteOrders.class);
                                startActivity(intent);
                            }
                        }, 1000);
                    }
                    else{
                        Toast.makeText(ViewOrder.this,"Order Not Updated",Toast.LENGTH_LONG).show();
                    }


                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ViewOrder.this, error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("eeeeeeeeeeeeefefevefefefeeff"+error.getMessage());

            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(ViewOrder.this);
        requestQueue.add(stringRequest);
    }


    public void getOrderDetails(){

        HttpsTrustManager.allowAllSSL();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try{
                    String orderId = response.getString("orderId");
                    String siteId = response.getString("siteId");
                    String companyName = response.getString("companyName");
                    String material = response.getString("material");
                    String quantity = response.getString("quantity");
                    String deliveryAddress =response.getString("deliveryAddress");
                    String requisitionerName = response.getString("requisitionerName");
                    String supplierId = response.getString("supplierId");
                    String date = response.getString("date");
                    String total = response.getString("total");
                    String status =response.getString("status");

                    OrderId.setText(orderId);
                    SiteId.setText(siteId);
                    CompanyName.setText(companyName);
                    Material.setText(material);
                    Quantity.setText(quantity);
                    DeliveryAddress.setText(deliveryAddress);
                    RequisitionerName.setText(requisitionerName);
                    SupplierId.setText(supplierId);
                    Date.setText(date);
                    Total.setText(total);
                    Status.setText(status);

                    if(status.equals("Received") || status.equals("Rejected") ||status.equals("Pending")){
                        MarkAsReceivedBtn.setVisibility(View.GONE);
                    }


                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewOrder.this, error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("eeeeeeeeeeeeefefevefefefeeff"+error.getMessage());
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(ViewOrder.this);
        requestQueue.add(jsonObjectRequest);
    }
}