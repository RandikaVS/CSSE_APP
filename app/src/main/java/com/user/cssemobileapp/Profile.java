package com.user.cssemobileapp;

import static com.user.cssemobileapp.constants.Url.dbName;
import static com.user.cssemobileapp.constants.Url.rootUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.user.cssemobileapp.databinding.ActivityProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends DrawerBase {

    ActivityProfileBinding activityProfileBinding;

    private EditText Firstname,Lastname,Email,Designation,Eid;
    private String id;
    private Button Update,Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitle("Profile");

        SharedPreferences prefs = getSharedPreferences("mySharedPref", MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);

        Firstname = findViewById(R.id.profile_fname);
        Lastname = findViewById(R.id.profile_lname);
        Email = findViewById(R.id.profile_email);
        Designation = findViewById(R.id.profile_designation);
        Eid = findViewById(R.id.profile_eid);
        Update = findViewById(R.id.profile_update_btn);
        Delete = findViewById(R.id.profile_delete_btn);

        Email.setEnabled(false);
        Eid.setEnabled(false);

//        Bundle extras = getIntent().getExtras();
        id = prefs.getString("id","noIdDefined");
        String URL= rootUrl+"/"+dbName+"/GetManagerData.php?id="+id;

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fname = Firstname.getText().toString().trim();
                String lname = Lastname.getText().toString().trim();
                String designation = Designation.getText().toString().trim();

                String UpdateUrl = rootUrl+"/"+dbName+"/UpdateProfile.php?id="+id+"&fname="+fname+"&lname="+lname+"&designation="+designation;
                HttpsTrustManager.allowAllSSL();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, UpdateUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(Profile.this, Profile.class);
                                        startActivity(intent);
                                    }
                                }, 1000);
                            } else {
                                Toast.makeText(Profile.this, "Profile Not Updated", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Profile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println("eeeeeeeeeeeeefefevefefefeeff"+error.getMessage());
                    }
                }
                );
                RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
                requestQueue.add(stringRequest);
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setMessage("Do you want to exit ?");

                // Set Alert Title
                builder.setTitle("Alert !");

                builder.setCancelable(false);

                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

                    String DeleteUrl = rootUrl+"/"+dbName+"/DeleteProfile.php?id="+id;
                    HttpsTrustManager.allowAllSSL();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DeleteUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    Toast.makeText(Profile.this, "Profile Deleted", Toast.LENGTH_SHORT).show();

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(Profile.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 1000);
                                } else {
                                    Toast.makeText(Profile.this, "Profile Not Deleted", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Profile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("eeeeeeeeeeeeefefevefefefeeff"+error.getMessage());
                        }
                    }
                    );
                    RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
                    requestQueue.add(stringRequest);
                    finish();
                });

                // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();
                // Show the Alert Dialog box
                alertDialog.show();

            }
        });

        HttpsTrustManager.allowAllSSL();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try{

                    String fname = response.getString("fname");
                    String lname = response.getString("lname");
                    String email = response.getString("email");
                    String designation = response.getString("designation");
                    String eid =response.getString("id");

                    SharedPreferences.Editor editor = getSharedPreferences("mySharedPref", MODE_PRIVATE).edit();
                    editor.putString("userName",fname+" "+lname);
                    editor.apply();

                    Firstname.setText(fname);
                    Lastname.setText(lname);
                    Email.setText(email);
                    Designation.setText(designation);
                    Eid.setText(eid);

                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
        requestQueue.add(jsonObjectRequest);
    }
}