package com.user.cssemobileapp;

import static com.user.cssemobileapp.constants.Url.dbName;
import static com.user.cssemobileapp.constants.Url.rootUrl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText Email,Password;
    private Button LoginBtn;
    private LinearLayout RegisterBtn;
    private ProgressDialog processDialog;

    String URL= rootUrl+"/"+dbName+"/ManagerLogin.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processDialog = new ProgressDialog(MainActivity.this);

        Email = findViewById(R.id.login_email);
        Password = findViewById(R.id.login_password);
        LoginBtn = findViewById(R.id.login_btn);
        RegisterBtn = findViewById(R.id.register_layout);


        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterUser.class);
                startActivity(intent);
            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                processDialog.setMessage("Please wait...");
                processDialog.show();

                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

                boolean isSuccess = true;

                if(email.isEmpty()){
                    Email.setError("Field required");
                    Email.requestFocus();
                    isSuccess=false;
                }
                if(password.isEmpty()){
                    Password.setError("Field required");
                    Password.requestFocus();
                    isSuccess=false;
                }

                if(isSuccess){
                    HttpsTrustManager.allowAllSSL();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                String id = jsonObject.getString("id");

                                if(success.equals("1")){
                                    Toast.makeText(MainActivity.this, "Logging Successfully", Toast.LENGTH_SHORT).show();

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            SharedPreferences.Editor editor = getSharedPreferences("mySharedPref", MODE_PRIVATE).edit();
                                            editor.putString("id", id);
                                            editor.apply();
                                            Intent intent = new Intent(MainActivity.this, DashBoard.class);
//                                            intent.putExtra("id",id);
                                            startActivity(intent);
                                        }
                                    }, 1000);
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Logging Failed...", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }

                            processDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                            Toast.makeText(MainActivity.this,"Login Failed..",Toast.LENGTH_LONG).show();
                            processDialog.dismiss();
                        }
                    }
                    ){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String ,String> params = new HashMap<String,String>();

                            params.put("email",email);
                            params.put("password",password);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);
                }
                else{
                    processDialog.dismiss();
                }

            }
        });
    }
}