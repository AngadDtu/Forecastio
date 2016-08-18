package com.example.angad.forecastio.UI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angad.forecastio.R;
import com.example.angad.forecastio.model.CurrentWeather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG =MainActivity.class.getSimpleName() ;
private CurrentWeather mCurrentWeather;
    private TextView mTimeZoneValue;
    private TextView mTemperatureValue;
    private TextView mHumidityValue;
    private TextView mPrecipValue;
    private TextView mSummaryLabel;
    private ImageView mIconImageView;
    private TextView mTimeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimeZoneValue=(TextView)findViewById(R.id.timeZoneLabel);
        mTemperatureValue=(TextView)findViewById(R.id.temperatureLabel);
        mHumidityValue=(TextView)findViewById(R.id.humidityLabel);
        mPrecipValue=(TextView)findViewById(R.id.percipLevel);
        mSummaryLabel=(TextView)findViewById(R.id.summaryLabel);
        mIconImageView=(ImageView)findViewById(R.id.iconLabel);
        mTimeValue=(TextView)findViewById(R.id.timeLabel);
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onUpdateDetails();
                                }
                            });
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

    private void onUpdateDetails() {
       mTimeZoneValue.setText(mCurrentWeather.getTimeZone());
        mTemperatureValue.setText(mCurrentWeather.getTemperature()+"");
        mHumidityValue.setText(mCurrentWeather.getHumidity()+"");
        mPrecipValue.setText(mCurrentWeather.getPerciChange()+"%");
        mSummaryLabel.setText(mCurrentWeather.getSummary());
        Drawable drawable = ContextCompat.getDrawable(this, mCurrentWeather.getIcon());
        mIconImageView.setImageDrawable(drawable);
        mTimeValue.setText("At "+mCurrentWeather.getFormattedTime()+" temp will be");
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast=new JSONObject(jsonData);
        JSONObject currently=forecast.getJSONObject("currently");
        CurrentWeather currentWeather=new CurrentWeather();
        currentWeather.setTimeZone(forecast.getString("timezone"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setPerciChange(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        String formattedTime=currentWeather.getFormattedTime();
        Log.v(TAG,"Time: "+formattedTime);

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
