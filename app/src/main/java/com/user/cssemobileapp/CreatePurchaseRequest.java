package com.user.cssemobileapp;

import static com.user.cssemobileapp.constants.Url.dbName;
import static com.user.cssemobileapp.constants.Url.rootUrl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.user.cssemobileapp.databinding.ActivityCreatePurchaseRequestBinding;
import com.user.cssemobileapp.models.Material;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreatePurchaseRequest extends AppCompatActivity {

    private ImageView BackFindSupplier;
    private EditText SideId,CompanyName,RqMaterial,Quantity,DeliveryAddress,RequisitionerName,TotalAmount,SupplierId;
    private Button CalculateBtn,CreatePrBtn;
    private TextView Date;
    private ProgressDialog processDialog;

    String materialName,materialId,materialDescription,materialUnitPrice,status;
    double totalPrice;
    private String URL= rootUrl+"/"+dbName+"/CreatePr.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_purchase_request);

        SharedPreferences prefs = getSharedPreferences("mySharedPref", MODE_PRIVATE);

        processDialog = new ProgressDialog(this);

        BackFindSupplier = findViewById(R.id.back_find_supplier);
        SideId = findViewById(R.id.pr_site_id);
        CompanyName = findViewById(R.id.pr_company_name);
        RqMaterial = findViewById(R.id.pr_req_meterial);
        Quantity = findViewById(R.id.pr_qty);
        DeliveryAddress = findViewById(R.id.pr_delivery_address);
        RequisitionerName = findViewById(R.id.pr_requisitioner_name);
        Date = findViewById(R.id.pr_due_date);
        TotalAmount = findViewById(R.id.pr_total_amount);
        SupplierId = findViewById(R.id.pr_supplier_id);
        CalculateBtn = findViewById(R.id.pr_calculate_btn);
        CreatePrBtn = findViewById(R.id.pr_create_btn);

        SupplierId.setEnabled(false);
        RqMaterial.setEnabled(false);

        Bundle extra = getIntent().getExtras();

        materialName = extra.getString("material");

        SupplierId.setText("Supplier Id - "+extra.getString("supplierId"));
        RqMaterial.setText("Material - "+extra.getString("material"));
        RequisitionerName.setText(prefs.getString("userName","noUserNameDefined"));

        getMaterialDetails();

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        CreatePurchaseRequest.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                Date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });


        CalculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Material material = new Material(materialId,materialName,materialUnitPrice,materialDescription);

                String quantity = Quantity.getText().toString().trim();

                if(!quantity.isEmpty()){

                    int qty = Integer.parseInt(quantity);

                    int unitPrice = Integer.parseInt(material.getUnitPrice());

                    totalPrice = qty*unitPrice;

                    TotalAmount.setText(String.valueOf(totalPrice));
                }
                else{
                    Quantity.setError("please enter quantity");
                    Quantity.requestFocus();
                }

            }
        });

        CreatePrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                processDialog.setMessage("Please wait...");
                processDialog.show();

                String siteId = SideId.getText().toString().trim();
                String companyName = CompanyName.getText().toString().trim();
                String material = extra.getString("material");
                String quantity = Quantity.getText().toString().trim();
                String deliveryAddress = DeliveryAddress.getText().toString().trim();
                String requisitionerName = RequisitionerName.getText().toString().trim();
                String supplierId = extra.getString("supplierId");
                String date = Date.getText().toString().trim();
                String total =TotalAmount.getText().toString().trim();

                if(total.length()>=8) {
                    status = "Pending";
                }
                else{
                    status = "Approved";
                }
                boolean isSuccess = true;

                if(siteId.isEmpty()){
                    SideId.setError("Field Required");
                    SideId.requestFocus();
                    isSuccess=false;
                }
                if(companyName.isEmpty()){
                    CompanyName.setError("Field Required");
                    CompanyName.requestFocus();
                    isSuccess=false;
                }
                if(material.isEmpty()){
                    RqMaterial.setError("Field Required");
                    RqMaterial.requestFocus();
                    isSuccess=false;
                }
                if(quantity.isEmpty()){
                    Quantity.setError("Field Required");
                    Quantity.requestFocus();
                    isSuccess=false;
                }
                if(deliveryAddress.isEmpty()){
                    DeliveryAddress.setError("Field Required");
                    DeliveryAddress.requestFocus();
                    isSuccess=false;
                }
                if(requisitionerName.isEmpty()){
                    RequisitionerName.setError("Field Required");
                    RequisitionerName.requestFocus();
                    isSuccess=false;
                }
                if(supplierId.isEmpty()){
                    SupplierId.setError("Field Required");
                    SupplierId.requestFocus();
                    isSuccess=false;
                }
                if(date.isEmpty()){
                    Date.setError("Pick a Date");
                    Date.requestFocus();
                    isSuccess=false;
                }
                if(total.isEmpty()){
                    TotalAmount.requestFocus();
                    Toast.makeText(CreatePurchaseRequest.this,"Please calculate before create PR",Toast.LENGTH_SHORT).show();
                    isSuccess=false;
                }

                if(isSuccess){
                    HttpsTrustManager.allowAllSSL();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    if(response.equalsIgnoreCase("success")){
                                        processDialog.dismiss();
                                        Toast.makeText(CreatePurchaseRequest.this,"PR Created Successfully",Toast.LENGTH_SHORT).show();

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(CreatePurchaseRequest.this,DashBoard.class);
                                                startActivity(intent);
                                            }
                                        }, 2000);
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            processDialog.dismiss();
                            Toast.makeText(CreatePurchaseRequest.this,"Registration Failed...",Toast.LENGTH_SHORT).show();
                        }
                    }
                    ){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String ,String> params = new HashMap<String,String>();

                            params.put("siteId",siteId);
                            params.put("companyName",companyName);
                            params.put("material",material);
                            params.put("quantity",quantity);
                            params.put("deliveryAddress",deliveryAddress);
                            params.put("requisitionerName",requisitionerName);
                            params.put("supplierId",supplierId);
                            params.put("date",date);
                            params.put("total",String.valueOf(total));
                            params.put("status",status);


                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(CreatePurchaseRequest.this);
                    requestQueue.add(stringRequest);
                }
                processDialog.dismiss();

            }
        });



        BackFindSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreatePurchaseRequest.this,FindSupplier.class);
                startActivity(intent);
            }
        });
    }

    public void getMaterialDetails(){

        String URL= rootUrl+"/"+dbName+"/GetMaterialDetails.php?material="+materialName;

        HttpsTrustManager.allowAllSSL();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            Material materialDetail = new Material(response.getString("id"),
                                    response.getString("name"),
                                    response.getString("price"),
                                    response.getString("description"));

                            materialName = materialDetail.getName();
                            materialId = materialDetail.getMaterialId();
                            materialDescription = materialDetail.getDescription();
                            materialUnitPrice = materialDetail.getUnitPrice();


                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreatePurchaseRequest.this, error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CreatePurchaseRequest.this);
        requestQueue.add(jsonObjectRequest);

    }
}