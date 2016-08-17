package com.example.angad.forecastio.UI;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.angad.forecastio.R;
import com.example.angad.forecastio.model.CurrentWeather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG =MainActivity.class.getSimpleName() ;
private CurrentWeather mCurrentWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apiKey="a65e6661daf2ae7d51c04026725ebd54";
        double longitude= 37.8267;
        double latitude=-122.423;
        String foreCast="https://api.forecast.io/forecast/" + apiKey + "/" + longitude + "," + latitude;
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(foreCast)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {

                        if (response.isSuccessful()) {
                            String JsonData=response.body().string();
                            Log.v(TAG, JsonData);
                            mCurrentWeather=getCurrentDetails(JsonData);
                        } else {
                            Log.v(TAG, response.body().string());
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "NETWORKING ERRROR",e);
                    } catch (JSONException e) {
                        Log.e(TAG,"NETWORKING ERROR",e);
                    }
                }
            });
        }
        else{
            Toast.makeText(this, R.string.network_error_toast_message,Toast.LENGTH_LONG).show();
        }
        Log.d(TAG,"Main UI is working");

    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast=new JSONObject(jsonData);
        JSONObject currently=forecast.getJSONObject("currently");
        CurrentWeather currentWeather=new CurrentWeather();
        currentWeather.setLocation(forecast.getString("timezone"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setPerciChange(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));

        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        boolean isActive=false;
        if(networkInfo!=null && networkInfo.isConnected()){
            isActive=true;
        }
        return  isActive;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog=new AlertDialogFragment();
        dialog.show(getFragmentManager(),"Alert Dialog Message");
    }
}
