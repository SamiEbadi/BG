package com.cartrack.android.bartshouldirun;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private static ArrayList<Station> mAllStations;

//    public static int convertDptoPixel(Context context, int dpValue){
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int)(dpValue * scale);
//    }

    public static Station getStation(Context context, int position) {
        if(mAllStations == null){
            fillAllStations(context);
        }
        return mAllStations.get(position);
    }

    private static void fillAllStations(Context context){
        mAllStations = new ArrayList<>();
        // Richmond
        mAllStations.add(new Station(context.getString(R.string.richmond),"rich", -122.3526, 37.9372 ));
        mAllStations.add(new Station(context.getString(R.string.delnorte), "deln", -122.3170, 37.9252));
        mAllStations.add(new Station(context.getString(R.string.plaza), "plza", -122.2990, 37.9027));
        mAllStations.add(new Station(context.getString(R.string.north_berkeley), "nbrk", 0, 0));
        mAllStations.add(new Station(context.getString(R.string.downtown_berkeley), "dbrk",0,0));
        mAllStations.add(new Station(context.getString(R.string.ashby), "ashb",0,0));
        // Pittsburg
        mAllStations.add(new Station(context.getString(R.string.pittsburg), "pitt",0,0));
        mAllStations.add(new Station(context.getString(R.string.north_concord), "ncon",0,0));
        mAllStations.add(new Station(context.getString(R.string.concord), "conc",0,0));
        mAllStations.add(new Station(context.getString(R.string.pleasant_hill), "phil",0,0));
        mAllStations.add(new Station(context.getString(R.string.walnut_creek), "wcrk",0,0));
        mAllStations.add(new Station(context.getString(R.string.lafayette), "lafy",0,0));
        mAllStations.add(new Station(context.getString(R.string.orinda), "orin",0,0));
        mAllStations.add(new Station(context.getString(R.string.rockridge), "rock",0,0));
        // MacArthur ina
        mAllStations.add(new Station(context.getString(R.string.macarthur), "mcar",0,0));
        mAllStations.add(new Station(context.getString(R.string.oakland19th), "19th",0,0));
        mAllStations.add(new Station(context.getString(R.string.oakland12th), "12th",0,0));
        mAllStations.add(new Station(context.getString(R.string.west_oakland), "woak",0,0));
        // Lake merrite
        mAllStations.add(new Station(context.getString(R.string.lake_merritte), "lake",0,0));
        mAllStations.add(new Station(context.getString(R.string.fruitvale), "ftvl",0,0));
        mAllStations.add(new Station(context.getString(R.string.coliseum), "cols",0,0));
        mAllStations.add(new Station(context.getString(R.string.san_leandro), "sanl",0,0));
        mAllStations.add(new Station(context.getString(R.string.bay_fair), "bayf",0,0));
        // Dublin
        mAllStations.add(new Station(context.getString(R.string.castro_valley), "cast",0,0));
        mAllStations.add(new Station(context.getString(R.string.west_dublin), "wdub",0,0));
        mAllStations.add(new Station(context.getString(R.string.dublin), "dubl",0,0));
        // Fremont
        mAllStations.add(new Station(context.getString(R.string.hayward), "hayw",0,0));
        mAllStations.add(new Station(context.getString(R.string.south_hayward), "shay",0,0));
        mAllStations.add(new Station(context.getString(R.string.union_city), "ucty",0,0));
        mAllStations.add(new Station(context.getString(R.string.fremont), "frmt",0,0));
        // West Coast
        mAllStations.add(new Station(context.getString(R.string.embarcadero), "embr",0,0));
        mAllStations.add(new Station(context.getString(R.string.montgomery), "mont",0,0));
        mAllStations.add(new Station(context.getString(R.string.powell), "powl",0,0));
        mAllStations.add(new Station(context.getString(R.string.civic_center), "civc",0,0));
        mAllStations.add(new Station(context.getString(R.string.sf16th), "16th",0,0));
        mAllStations.add(new Station(context.getString(R.string.sf24th), "24th",0,0));
        mAllStations.add(new Station(context.getString(R.string.glen_park), "glen",0,0));
        mAllStations.add(new Station(context.getString(R.string.balboa_park), "balb",0,0));
        mAllStations.add(new Station(context.getString(R.string.daly_city), "daly",0,0));
        mAllStations.add(new Station(context.getString(R.string.colma), "colm",0,0));
        mAllStations.add(new Station(context.getString(R.string.south_sf), "ssan",0,0));
        mAllStations.add(new Station(context.getString(R.string.san_bruno), "sbrn",0,0));
        mAllStations.add(new Station(context.getString(R.string.sfo), "sfia", 0,0));
        mAllStations.add(new Station(context.getString(R.string.millbrae), "mlbr",0,0));
    }

    public static Date addMinutesToDate(int minutes, Date beforeTime){
        final long ONE_MINUTE_IN_MILLIS = 60000;

        long curTimeInMs = beforeTime.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    public static String toShortTimeString(Date aDate){
        DateFormat df = new SimpleDateFormat("h:mm");
        return df.format(aDate);
    }
}
