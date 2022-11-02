package com.user.cssemobileapp;

import static com.user.cssemobileapp.constants.Url.rootUrl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;


public class RegisterUser extends AppCompatActivity {

    private EditText FirstName,LastName,Designation,Password,ConfirmPassword,Email;
    private Button RegisterBtn;
    private LinearLayout LoginBackBtn;

    private ProgressDialog processDialog;
    private int success = 0;


    String URL= rootUrl+"/csse/ManagerRegister.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        processDialog = new ProgressDialog(this);

        FirstName = findViewById(R.id.register_fname);
        LastName = findViewById(R.id.register_lname);
        Email = findViewById(R.id.register_email);
        Designation = findViewById(R.id.register_designation);
        Password = findViewById(R.id.register_password);
        ConfirmPassword = findViewById(R.id.register_confirm_password);
        RegisterBtn = findViewById(R.id.user_register_btn);
        LoginBackBtn = findViewById(R.id.back_login_layout);


        LoginBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterUser.this,MainActivity.class);
                startActivity(intent);
            }
        });


        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                processDialog.setMessage("Please wait...");
                processDialog.show();

                String fname = FirstName.getText().toString().trim();
                String lname = LastName.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String designation = Designation.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String re_password = ConfirmPassword.getText().toString().trim();

                boolean isSuccess = true;

                if(fname.isEmpty()){
                    FirstName.setError("Field required");
                    FirstName.requestFocus();
                    isSuccess=false;
                }
                if(lname.isEmpty()){
                    LastName.setError("Field required");
                    LastName.requestFocus();
                    isSuccess=false;
                }
                if(email.isEmpty()){
                    Email.setError("Field required");
                    Email.requestFocus();
                    isSuccess=false;
                }
                if(designation.isEmpty()){
                    Designation.setError("Field required");
                    Designation.requestFocus();
                    isSuccess=false;
                }
                if(password.isEmpty()){
                    Password.setError("Field required");
                    Password.requestFocus();
                    isSuccess=false;
                }
                if(password != re_password){
                    Password.setError("Passwords didn't match");
                    Password.requestFocus();
                    ConfirmPassword.setError("Passwords didn't match");
                    ConfirmPassword.requestFocus();
                    isSuccess=false;
                }

                if(isSuccess){

                    HttpsTrustManager.allowAllSSL();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.equalsIgnoreCase("success")){
                                Toast.makeText(RegisterUser.this,"Registered Successfully",Toast.LENGTH_SHORT).show();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(RegisterUser.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                }, 2000);
                            }
                            else{
                                Toast.makeText(RegisterUser.this,"Registration Failed...",Toast.LENGTH_SHORT).show();
                            }
                            processDialog.dismiss();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterUser.this,error.getMessage(),Toast.LENGTH_LONG).show();
                            processDialog.dismiss();
                        }
                    }
                    ){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String ,String> params = new HashMap<String,String>();

                            params.put("fname",fname);
                            params.put("lname",lname);
                            params.put("email",email);
                            params.put("designation",designation);
                            params.put("password",password);

                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterUser.this);
                    requestQueue.add(stringRequest);

                }



            }
        });

    }

}