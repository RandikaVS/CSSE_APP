package com.user.cssemobileapp;

import static com.user.cssemobileapp.constants.Url.rootUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.user.cssemobileapp.databinding.ActivityDashBoardBinding;
import com.user.cssemobileapp.databinding.ActivityProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class DashBoard extends DrawerBase {

    ActivityDashBoardBinding activityDashBoardBinding;

    private TextView Requests,Pending,Approved,Received;

    String URL= rootUrl+"/csse/GetOrdersCount.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashBoardBinding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(activityDashBoardBinding.getRoot());
        allocateActivityTitle("Dashboard");

        Requests = findViewById(R.id.requests);
        Pending = findViewById(R.id.pending);
        Approved = findViewById(R.id.approved);
        Received = findViewById(R.id.received);

        HttpsTrustManager.allowAllSSL();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String request = response.getString("Requests");
                    String pending = response.getString("Pending");
                    String approved = response.getString("Approved");
                    String received = response.getString("Received");

                    Requests.setText(request);
                    Pending.setText(pending);
                    Approved.setText(approved);
                    Received.setText(received);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashBoard.this, error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(DashBoard.this);
        requestQueue.add(jsonObjectRequest);
    }
}