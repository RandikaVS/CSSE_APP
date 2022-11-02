package com.user.cssemobileapp;

import static com.user.cssemobileapp.constants.Url.rootUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.user.cssemobileapp.databinding.ActivityProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends DrawerBase {

    ActivityProfileBinding activityProfileBinding;

    private EditText Firstname,Lastname,Email,Designation,Eid;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitle("Profile");

        SharedPreferences prefs = getSharedPreferences("mySharedPref", MODE_PRIVATE);

        Firstname = findViewById(R.id.profile_fname);
        Lastname = findViewById(R.id.profile_lname);
        Email = findViewById(R.id.profile_email);
        Designation = findViewById(R.id.profile_designation);
        Eid = findViewById(R.id.profile_eid);

        Email.setEnabled(false);
        Eid.setEnabled(false);

//        Bundle extras = getIntent().getExtras();
        id = prefs.getString("id","noIdDefined");
        String URL= rootUrl+"/csse/GetManagerData.php?id="+id;

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