package Database;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ManagerRegister extends AsyncTask<String,Void,String> {

    Context context;
    AlertDialog alertDialog;
    ManagerRegister(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        String login_url = params[0];
        String type = params[1];
        String fname = params[2];
        String lname = params[3];
        String email = params[4];
        String designation = params[5];
        String password = params[6];

        try{

            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutPut);

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
