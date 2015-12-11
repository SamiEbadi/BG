package com.cartrack.android.bartshouldirun;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StationActivity extends AppCompatActivity  implements RecyclerView.OnItemTouchListener{

    RelativeLayout mRoot;
    private String mAbbr;
    Station mStation;
    AppCompatTextView mStationSignText;
    HashMap<RecyclerView,Integer> mAllRecyclerViews; // Integer is the number of OnItemTouchListeners
    private int mMaxMin;
    private final int AFTER_MAX_MIN = 10;
    private ArrayList<Integer> mMinsWithTrain;
    private View mShadow;
    private boolean isShadowVisible = false;
    private int mGlobalScrollY;
    private int mShadowThresholdInPixel = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        mRoot = (RelativeLayout) findViewById(R.id.station_root);
        Intent intent = getIntent();
        int position = intent.getExtras().getInt(IntentHelper.POSITION);
        mAbbr = intent.getExtras().getString(IntentHelper.STATION_ABBR);
        makeStationSign(position);
        makeChangeStationText();
        // Get data
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new GetStationDataAsync().execute("");
        }
       // StationActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
         mShadow = findViewById(R.id.station_header_shadow);

    }

    private void makeStationSign(int position) {
        Station station = Utils.getStation(StationActivity.this, position);
        mStationSignText = new AppCompatTextView(this);
        mStationSignText.setText(station.getName());
        mStationSignText.setTextSize(15);
        mStationSignText.setTextColor(StationActivity.this.getResources().getColor(R.color.lead_cream));
        mStationSignText.setTypeface(null, Typeface.BOLD);
        mStationSignText.setBackground(ContextCompat.getDrawable(StationActivity.this, R.drawable.sign));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.topMargin = 10;
        mStationSignText.setLayoutParams(params);
        mStationSignText.setId(View.generateViewId());
        mStationSignText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mRoot.addView(mStationSignText);


    }

    private void makeChangeStationText(){
        final AppCompatTextView changeStationText = new AppCompatTextView(this);
        SpannableString content = new SpannableString(getString(R.string.change_station));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        changeStationText.setText(content);
        changeStationText.setTextSize(12);
        changeStationText.setTextColor(getResources().getColor(R.color.lead_cream));
        changeStationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params2.addRule(RelativeLayout.RIGHT_OF, mStationSignText.getId());
        changeStationText.setLayoutParams(params2);
        params2.topMargin = 20;
        params2.rightMargin = 5;


        mRoot.addView(changeStationText);
        changeStationText.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Log.d("change text", "");
            }
        });
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    int someCounter = 0;
    private void refreshPageWithStationData(Station station) {
        if (station == null || station.Trains == null) {
            return;
        }
        HashMap<Integer, ArrayList<Train>> platformTrains = new HashMap<>();
        mMinsWithTrain = new ArrayList<>();
        for (Train train : station.Trains) {
            if (platformTrains.containsKey(train.getPlatform())) {
                platformTrains.get(train.getPlatform()).add(train);
            } else {
                ArrayList<Train> tempList = new ArrayList<>();
                tempList.add(train);
                platformTrains.put(train.getPlatform(), tempList);
            }
            int min = train.getMinutes();
            if(min > mMaxMin){
                mMaxMin = train.getMinutes();
            }
            if(!mMinsWithTrain.contains(min)){
                mMinsWithTrain.add(min);
            }
        }
        addPlatformRecyclerViews(platformTrains);
    }

    private void addTimeLine(LinearLayout parent){
        View timeLineView = LayoutInflater.from(StationActivity.this).inflate(R.layout.time_line, parent, false);
        // Set title
        TextView titleText = (TextView)timeLineView.findViewById(R.id.time_line_title);
        titleText.setText(getString(R.string.arrive_at));
        // Set recycler view
        LinearLayout timeLineLayout = (LinearLayout)timeLineView.findViewById(R.id.time_line_layout);
        ArrayList<String> timesList = new ArrayList<>();
        timesList.add("now");
        Date now = new Date();
        for(int i =1; i<= mMaxMin + AFTER_MAX_MIN -1;i++){
            timesList.add(Utils.toShortTimeString(Utils.addMinutesToDate(i, now)));
        }
        TimeLineRecyclerViewAdapter theAdapter = new TimeLineRecyclerViewAdapter(this, timesList,mMinsWithTrain);
        RecyclerView timeLineRecyclerView = getTimeLineRecyclerView(timeLineLayout);
        mAllRecyclerViews.put(timeLineRecyclerView, 0);
        timeLineRecyclerView.setAdapter(theAdapter);
        parent.addView(timeLineView);
    }

    private void addPlatformRecyclerViews(HashMap<Integer, ArrayList<Train>> platformTrains){
        LinearLayout allPlatformLayout = (LinearLayout) findViewById(R.id.all_platforms_layout);

        Log.d("width", " of page " + allPlatformLayout.getWidth() );
        mAllRecyclerViews = new HashMap<>();
        boolean timeLineAdded = false;
        for (int platformNumber : platformTrains.keySet()) {
            View platformView = LayoutInflater.from(StationActivity.this).inflate(R.layout.platform, allPlatformLayout, false);
            TextView sign = (TextView) platformView.findViewById(R.id.platform_sign);
            String signText = getString(R.string.platform) + " " + platformNumber;
            sign.setText(signText);

            LinearLayout platformLayout = (LinearLayout) platformView.findViewById(R.id.platform_layout);

            RecyclerViewAdapter theAdapter = new RecyclerViewAdapter(this, platformTrains.get(platformNumber), mMaxMin + AFTER_MAX_MIN);
            RecyclerView platformRecyclerView = getPlatformRecyclerView(platformLayout);
            mAllRecyclerViews.put(platformRecyclerView, 0);
            platformRecyclerView.setAdapter(theAdapter);

            allPlatformLayout.addView(platformView);
            if(!timeLineAdded) {
                addTimeLine(allPlatformLayout);
                timeLineAdded = true;
            }
        }
        syncScrolls();
    }

    private int getShadowThreshold(){
        if(mShadowThresholdInPixel < 0){
            mShadowThresholdInPixel = (int) getResources().getDimension(R.dimen.shadow_threshold);
        }
        return mShadowThresholdInPixel;
    }

    private void syncScrolls(){
        // Add sync scroll listeners
        for(RecyclerView recyclerView : mAllRecyclerViews.keySet()){
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    switch (newState) {
                        case RecyclerView.SCROLL_STATE_DRAGGING:
                            for (RecyclerView rv : mAllRecyclerViews.keySet()) {
                                if (!recyclerView.equals(rv)) {
                                    rv.setOnScrollListener(null);
                                    if (mAllRecyclerViews.get(rv) == 0) {
                                        rv.addOnItemTouchListener(StationActivity.this);
                                        mAllRecyclerViews.put(rv, mAllRecyclerViews.get(rv) + 1);
                                        someCounter++;
                                        Log.d("scroll counter ", "" + someCounter);
                                    }
                                }
                            }
                            break;
                        case RecyclerView.SCROLL_STATE_IDLE:
                            for (RecyclerView rv : mAllRecyclerViews.keySet()) {
                                if (!recyclerView.equals(rv)) {
                                    rv.setOnScrollListener(this);
                                    rv.removeOnItemTouchListener(StationActivity.this);
                                    mAllRecyclerViews.put(rv, mAllRecyclerViews.get(rv) - 1);
                                    someCounter--;
                                    Log.d("scroll counter ", "" + someCounter);
                                }
                            }
                            break;
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mGlobalScrollY += dy;
                    for (RecyclerView rv : mAllRecyclerViews.keySet()) {
                        if (!recyclerView.equals(rv)) {
                            rv.scrollBy(dx, dy);
                        }
                    }
                    if(mGlobalScrollY < getShadowThreshold()){
                        showHideShadow(false);
                    }else if(!isShadowVisible){
                        showHideShadow(true);
                    }
                }
            });
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return true;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    private RecyclerView getPlatformRecyclerView(LinearLayout platformLayout) {
        RecyclerView result = (RecyclerView) platformLayout.findViewById(R.id.platform_recycler_view);
        result.setHasFixedSize(true);
        LinearLayoutManager aLayoutManager = new LinearLayoutManager(this);
        result.setLayoutManager(aLayoutManager);
        return result;
    }

    private RecyclerView getTimeLineRecyclerView(LinearLayout timeLineLayout) {
        RecyclerView result = (RecyclerView) timeLineLayout.findViewById(R.id.time_line_recycler_view);
        result.setHasFixedSize(true);
        LinearLayoutManager aLayoutManager = new LinearLayoutManager(this);
        result.setLayoutManager(aLayoutManager);
        return result;
    }

    private void showHideShadow(boolean show){
        isShadowVisible = show;
        float alpha = (show)?1f:0f;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mShadow,"alpha",alpha);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    class GetStationDataAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... urls) {
            try {
                URL url = new URL("http://api.bart.gov/api/etd.aspx?cmd=etd&orig=" + mAbbr + "&key=MW9S-E7SL-26DU-VV8V");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
            //        InputStream in = new ByteArrayInputStream(testXml.getBytes(StandardCharsets.UTF_8));
                    RealTimeXmlParser myParser = new RealTimeXmlParser();
                    mStation = myParser.parse(in);
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
//            if (response == null) { }
            refreshPageWithStationData(mStation);
        }
    }

    String testXml = "<root>\n" +
            "<uri>\n" +
            "<![CDATA[ http://api.bart.gov/api/etd.aspx?cmd=etd&orig=plza ]]>\n" +
            "</uri>\n" +
            "<date>12/05/2015</date>\n" +
            "<time>09:23:22 PM PST</time>\n" +
            "<station>\n" +
            "<name>El Cerrito Plaza</name>\n" +
            "<abbr>PLZA</abbr>\n" +
            "<etd>\n" +
            "<destination>Fremont</destination>\n" +
            "<abbreviation>FRMT</abbreviation>\n" +
            "<limited>0</limited>\n" +
            "<estimate>\n" +
            "<minutes>20</minutes>\n" +
            "<platform>2</platform>\n" +
            "<direction>South</direction>\n" +
            "<length>6</length>\n" +
            "<color>ORANGE</color>\n" +
            "<hexcolor>#ff9933</hexcolor>\n" +
            "<bikeflag>1</bikeflag>\n" +
            "</estimate>\n" +
            "<estimate>\n" +
            "<minutes>40</minutes>\n" +
            "<platform>2</platform>\n" +
            "<direction>South</direction>\n" +
            "<length>6</length>\n" +
            "<color>ORANGE</color>\n" +
            "<hexcolor>#ff9933</hexcolor>\n" +
            "<bikeflag>1</bikeflag>\n" +
            "</estimate>\n" +
            "</etd>\n" +
            "<etd>\n" +
            "<destination>Richmond</destination>\n" +
            "<abbreviation>RICH</abbreviation>\n" +
            "<limited>0</limited>\n" +
            "<estimate>\n" +
            "<minutes>6</minutes>\n" +
            "<platform>1</platform>\n" +
            "<direction>North</direction>\n" +
            "<length>6</length>\n" +
            "<color>ORANGE</color>\n" +
            "<hexcolor>#ff9933</hexcolor>\n" +
            "<bikeflag>1</bikeflag>\n" +
            "</estimate>\n" +
            "<estimate>\n" +
            "<minutes>26</minutes>\n" +
            "<platform>1</platform>\n" +
            "<direction>North</direction>\n" +
            "<length>6</length>\n" +
            "<color>ORANGE</color>\n" +
            "<hexcolor>#ff9933</hexcolor>\n" +
            "<bikeflag>1</bikeflag>\n" +
            "</estimate>\n" +
            "<estimate>\n" +
            "<minutes>46</minutes>\n" +
            "<platform>1</platform>\n" +
            "<direction>North</direction>\n" +
            "<length>6</length>\n" +
            "<color>ORANGE</color>\n" +
            "<hexcolor>#ff9933</hexcolor>\n" +
            "<bikeflag>1</bikeflag>\n" +
            "</estimate>\n" +
            "</etd>\n" +
            "</station>\n" +
            "<message/>\n" +
            "</root>";
}
