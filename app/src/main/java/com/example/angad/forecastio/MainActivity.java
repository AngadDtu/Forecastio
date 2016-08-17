package com.example.angad.forecastio;

import android.app.AlertDialog;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG =MainActivity.class.getSimpleName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apiKey="a65e6661daf2ae7d51c04026725ebd54";
        double longitude=37.8267;
        double latitude=-122.423;
        String foreCast="https://api.forecast.io/forecast/" + apiKey + "/" + longitude + "," + latitude;
        OkHttpClient client=new OkHttpClient();
        Request request=new  Request.Builder()
                .url(foreCast)
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {

                    if(response.isSuccessful()){
                        Log.v(TAG,response.body().string());
                    }
                    else{
                        Log.v(TAG,response.body().string());
                        alertUserAboutError();
                    }
                } catch (IOException e) {
                    Log.e(TAG,"NETWORKING ERRROR");
                }
            }
        });
        Log.d(TAG,"Main UI is working");

    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog=new AlertDialogFragment();
        dialog.show(getFragmentManager(),"Aler Dialog Message");
    }
}
