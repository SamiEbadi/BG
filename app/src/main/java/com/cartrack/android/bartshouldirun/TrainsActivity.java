package com.cartrack.android.bartshouldirun;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TrainsActivity extends AppCompatActivity {

    String mAbbr;
    Station mStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trains);

        Intent intent = getIntent();
        setTitle(intent.getExtras().getString("name"));
        mAbbr = intent.getExtras().getString("abbr");

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DeparturesAsyncTask().execute("");
        } else {

        }

//        final ArrayList<MainPageTrain> trains = new ArrayList<>();
//        trains.add(new MainPageTrain("Richmond", "4 MIN", Color.RED));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Dublin", "5 MIN", Color.BLUE));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.rgb(255,117,53)));
//        trains.add(new MainPageTrain("Richmond", "4 MIN", Color.RED));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Dublin", "5 MIN", Color.BLUE));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.rgb(255,117,53)));
//        trains.add(new MainPageTrain("Millbrae", "5 MIN", Color.YELLOW));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("SFO Int'l", "5 MIN", Color.RED));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Millbrae", "5 MIN", Color.YELLOW));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("SFO Int'l", "5 MIN", Color.RED));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Richmond", "5 MIN", Color.RED));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.rgb(255,117,53)));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Pittsburg", "5 MIN", Color.YELLOW));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("SFO Int'l", "5 MIN", Color.RED));
//        trains.add(new MainPageTrain("Richmond", "5 MIN", Color.RED));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));
//        trains.add(new MainPageTrain());trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Pittsburg", "5 MIN", Color.YELLOW));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("SFO Int'l", "5 MIN", Color.RED));
//        trains.add(new MainPageTrain("Richmond", "5 MIN", Color.RED));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));
//        trains.add(new MainPageTrain("Pittsburg", "5 MIN", Color.YELLOW));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("SFO Int'l", "5 MIN", Color.RED));trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Richmond", "5 MIN", Color.RED));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));
//        trains.add(new MainPageTrain("Pittsburg", "5 MIN", Color.YELLOW));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Dublin", "5 MIN", Color.BLUE));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Richmond", "5 MIN", Color.RED));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Pittsburg", "5 MIN", Color.YELLOW));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Dublin", "5 MIN", Color.BLUE));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Dublin", "5 MIN", Color.BLUE));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Richmond", "5 MIN", Color.RED));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));
//        trains.add(new MainPageTrain("Pittsburg", "5 MIN", Color.YELLOW));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Dublin", "5 MIN", Color.BLUE));
//        trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Richmond", "5 MIN", Color.RED));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));
//        trains.add(new MainPageTrain("Pittsburg", "5 MIN", Color.YELLOW));
//        trains.add(new MainPageTrain("Fremont", "5 MIN", Color.GREEN));trains.add(new MainPageTrain());
//        trains.add(new MainPageTrain("Dublin", "5 MIN", Color.BLUE));

        //recView.setAdapter(adapter);


      //  SamiView theSamiView = (SamiView)findViewById(R.id.sami_view);
      //  theSamiView.initSamiView(createSamiItems(trains), 0.5f);
    }

    private ArrayList<SamiItem> createSamiItems(ArrayList<MainPageTrain> allTrains, String platform){
        ArrayList<SamiItem> result = new ArrayList<>();
        int index = 0;
        while(allTrains.size() > 0) {
            boolean aTrainAdded = false;
            for (int i =0; i< allTrains.size(); i++) {
                if(allTrains.get(i).getMinutes() <= index ) {
                    if(!allTrains.get(i).XmlPlatform.toLowerCase().contains(platform)){
                        allTrains.remove(i);
                        break;
                    }
                    result.add(new SamiItem(allTrains.remove(i), null, ""));
                    aTrainAdded = true;
                    break;
                }
            }
            if(!aTrainAdded){
                result.add(new SamiItem(new MainPageTrain(),null,""));
            }
            index++;
        }
        return result;
    }

    private void refreshPageWithStationData(Station station){
        SamiView theSamiView = (SamiView)findViewById(R.id.sami_view);
        SamiView theSamiView2 = (SamiView)findViewById(R.id.sami_view_2);

        theSamiView.initSamiView(createSamiItems((ArrayList<MainPageTrain>)station.Trains.clone(),"1"), 0.5f);
        theSamiView2.initSamiView(createSamiItems((ArrayList<MainPageTrain>)station.Trains.clone(),"2"), 0.5f);



    }

    class DeparturesAsyncTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
            //responseView.setText("");
        }

        protected String doInBackground(String... urls) {
            try {
                URL url = new URL("http://api.bart.gov/api/etd.aspx?cmd=etd&orig="+mAbbr+"&key=MW9S-E7SL-26DU-VV8V");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
                    RealTimeXmlParser myParser = new RealTimeXmlParser();
                    mStation =  myParser.parse(in);
                    return "";
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
            refreshPageWithStationData(mStation);
        }
    }
}
