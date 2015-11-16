package com.cartrack.android.bartshouldirun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity implements LocationListener {

    LocationManager locationManager;
    ArrayList<Station> stations;
    String currentStationCode;
    String currentStationName;
    TextView responseText;
    Station CurrentStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        responseText = (TextView) findViewById(R.id.responseText);

        stations = new ArrayList<>();

        Intent intent = getIntent();
        currentStationName = intent.getExtras().getString("name");
        currentStationCode = intent.getExtras().getString("abbr");
        responseText.setText(currentStationName);
      //  locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
       // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new BartRetriver().execute("");
        } else {

        }
    }

    public void onMoveRichmond(View view){
        moveRichmond();
    }

    public void onMoveRichmond2(View view){
        //moveRichmond2();
    }

    private void addTrain(MainPageTrain train){
        int mins = 0;
        int platform = 0;
        try {
            mins = train.getMinutes();
        }catch (Exception ex){}
        try {
            platform = Integer.parseInt(train.XmlPlatform);
        }catch(Exception ex){}

        View trainView = LayoutInflater.from(this).inflate(R.layout.train, null);
        FrameLayout mainScroll = (FrameLayout)findViewById(R.id.main_scroll);
        TextView destination = (TextView)trainView.findViewById(R.id.destination);
        TextView timeLeft = (TextView)trainView.findViewById(R.id.time_left);
        destination.setText(train.XmlDestination);
        if(mins > 0)
        timeLeft.setText(train.getMinuteString() + " MIN");
        else
            timeLeft.setText(train.getMinuteString());
        destination.setTextColor(train.getColor());
        timeLeft.setTextColor(train.getColor());
        mainScroll.addView(trainView);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) trainView.getLayoutParams();
        params.topMargin = mins * 70;
        params.leftMargin = (platform - 1) * 300;

        startTrainAnimation(trainView, mins, params.topMargin);
    }

    private void refreshPageWithStationData(Station station){
        for(MainPageTrain train : station.Trains){
            addTrain(train);
        }
    }

    private void moveRichmond(){
        final int amountToMoveRight = 0;
        final int amountToMoveDown = -900;
        TranslateAnimation anim = new TranslateAnimation(0, amountToMoveRight, 0, amountToMoveDown);
        anim.setDuration(6 * 1000);

        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) richmond.getLayoutParams();
                // params.topMargin += amountToMoveDown;
                // params.leftMargin += amountToMoveRight;
                // richmond.setLayoutParams(params);
            }
        });

        //richmond.startAnimation(anim);
        //fremont.startAnimation(anim);
    }

    private void startTrainAnimation(final View trainLayout,int minutes, int topMargin){
        final int amountToMoveUp = topMargin;
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -amountToMoveUp);
        //ScaleAnimation scale = new ScaleAnimation(1f,1.5f,1f,1.5f);
        //scale.setDuration(minutes * 60 * 100);
        int duration = minutes * 60 *  1000;
        anim.setDuration(duration);

        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) trainLayout.getLayoutParams();
                params.topMargin -= amountToMoveUp ;
                trainLayout.setLayoutParams(params);
            }
        });
        trainLayout.startAnimation(anim);
    }

    @Override
    public void onLocationChanged(Location location) {
        Station nearest = getNearestStation(location);
        if(nearest != null && currentStationCode != nearest.getAbbr()){
            //currentStationCode = nearest.getAbbr();
            //webView.loadUrl("https://m.bart.gov/schedules/eta?stn=" + currentStationCode);
        }
    }

    private Station getNearestStation(Location location){
        double minDistance = Double.MAX_VALUE;
        Station result = null;
        for(int i = 0; i< stations.size();i++){
            double distance = stations.get(i).distanceTo(location);
            if(distance < minDistance) {
                result = stations.get(i);
                minDistance = distance;
            }
        }
        return result;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    class BartRetriver extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
            //responseView.setText("");
        }

        protected String doInBackground(String... urls) {
            try {
                //URL url = new URL("https://m.bart.gov/schedules/eta?stn=MONT");
                URL url = new URL("http://api.bart.gov/api/etd.aspx?cmd=etd&orig="+currentStationCode+"&key=MW9S-E7SL-26DU-VV8V");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();

                    RealTimeXmlParser myParser = new RealTimeXmlParser();
                    CurrentStation =  myParser.parse(in);

                  //  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                  //  StringBuilder stringBuilder = new StringBuilder();
                   // String line;
                   // while ((line = bufferedReader.readLine()) != null) {
                 //       stringBuilder.append(line).append("\n");
               //     }
                    //bufferedReader.close();
                    return "";//stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            //progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            refreshPageWithStationData(CurrentStation);
        }
    }
}



